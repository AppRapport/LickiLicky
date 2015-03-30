package ei.g2t6.servlet;

/*
 * =================================================================
 * Copyright (c) 2001-2003 TIBCO Software Inc.
 * All rights reserved.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * $RCSfile: tibjmsMsgConsumer1.java,v $
 * $Revision: 1.7 $
 * $Date: 2004/02/06 00:05:02 $
 * =================================================================
 */

/*
 * This is a simple sample of a basic tibjmsMsgConsumer1.
 *
 * This sampe subscribes to specified destination and
 * receives and prints all received messages.
 *
 * Notice that the specified destination should exist in your configuration
 * or your topics/queues configuration file should allow
 * creation of the specified destination.
 *
 * If this sample is used to receive messages published by
 * tibjmsMsgProducer sample, it must be started prior
 * to running the tibjmsMsgProducer sample.
 *
 * Usage:  java tibjmsMsgConsumer1 [options]
 *
 *    where options are:
 *
 *      -server     Server URL.
 *                  If not specified this sample assumes a
 *                  serverUrl of "tcp://localhost:7222"
 *
 *      -user       User name. Default is null.
 *      -password   User password. Default is null.
 *      -topic      Topic name. Default is "topic.sample"
 *      -queue      Queue name. No default
 *
 *
 */

import javax.jms.*;
import java.sql.*;
import java.util.*;


public class EIInventoryConsumer
        implements ExceptionListener, MessageListener {
    /*-----------------------------------------------------------------------
     * Parameters
     *----------------------------------------------------------------------*/

    String serverUrl = null;
    String userName = null;
    String password = null;
    String name = "topic.sample";
    boolean useTopic = true;
    boolean     unsubscribe     = false;
    /*-----------------------------------------------------------------------
    * Variables
    *----------------------------------------------------------------------*/
    javax.jms.Connection connection = null;
    Session session = null;
    MessageConsumer msgConsumer = null;
    Destination destination = null;

    //add by Stanley
    TextMessage message = null;

    public EIInventoryConsumer(String[] args) {
        parseArgs(args);

        try {
            tibjmsUtilities.initSSLParams(serverUrl, args);
        }
        catch (Exception e) {
            //System.err.println("JMSSecurityException: " + e.getMessage() + ", provider=" + e.getErrorCode());
            e.printStackTrace();
            System.exit(0);
        }

        /* print parameters */
        System.err.println("\n------------------------------------------------------------------------");
        System.err.println("EIInventoryConsumer");
        System.err.println("------------------------------------------------------------------------");
        System.err.println("Server....................... " + ((serverUrl != null) ? serverUrl : "localhost"));
        System.err.println("User......................... " + ((userName != null) ? userName : "(null)"));
        System.err.println("Destination.................. " + name);
        System.err.println("------------------------------------------------------------------------\n");

        try {
            run();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    /*-----------------------------------------------------------------------
     * usage
     *----------------------------------------------------------------------*/
    void usage() {
        System.err.println("\nUsage: EIInventoryConsumer [options] [ssl options]");
        System.err.println("");
        System.err.println("   where options are:");
        System.err.println("");
        System.err.println(" -server   <server URL> - EMS server URL, default is local server");
        System.err.println(" -user     <user name>  - user name, default is null");
        System.err.println(" -password <password>   - password, default is null");
        System.err.println(" -topic    <topic-name> - topic name, default is \"topic.sample\"");
        System.err.println(" -queue    <queue-name> - queue name, no default");
        System.err.println(" -help-ssl              - help on ssl parameters\n");
        System.exit(0);
    }

    /*-----------------------------------------------------------------------
     * parseArgs
     *----------------------------------------------------------------------*/
    void parseArgs(String[] args) {
        int i = 0;

        while (i < args.length) {
            if (args[i].compareTo("-server") == 0) {
                if ((i + 1) >= args.length) usage();
                serverUrl = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-topic") == 0) {
                if ((i + 1) >= args.length) usage();
                name = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-queue") == 0) {
                if ((i + 1) >= args.length) usage();
                name = args[i + 1];
                i += 2;
                useTopic = false;
            } else if (args[i].compareTo("-user") == 0) {
                if ((i + 1) >= args.length) usage();
                userName = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-password") == 0) {
                if ((i + 1) >= args.length) usage();
                password = args[i + 1];
                i += 2;

            } else if (args[i].compareTo("-unsubscribe")==0) {
                unsubscribe = true;
                i += 1;
            } else if (args[i].compareTo("-help") == 0) {
                usage();
            } else if (args[i].compareTo("-help-ssl") == 0) {
                tibjmsUtilities.sslUsage();
            } else if (args[i].startsWith("-ssl")) {
                i += 2;
            } else {
                System.err.println("Unrecognized parameter: " + args[i]);
                usage();
            }
        }
    }


    /*---------------------------------------------------------------------
     * onException
     *---------------------------------------------------------------------*/
    public void onException(
            JMSException e) {
        /* print the connection exception status */
        System.err.println("CONNECTION EXCEPTION: " + e.getMessage());
    }

    /*-----------------------------------------------------------------------
     * run
     *----------------------------------------------------------------------*/
    void run()
            throws JMSException {
        Message msg = null;
        String msgType = "UNKNOWN";
        String orderId = null;
        String orderDes = null;
        String orderQuantity = null;
        TopicSubscriber subscriber = null;

        System.err.println("Subscribing to destination: " + name + "\n");

        ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);

        /* create the connection */
        connection = factory.createConnection(userName, password);

        /* create the session */
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (unsubscribe) {
                System.err.println("Unsubscribing durable subscriber "+name);
                session.unsubscribe(name);
                System.err.println("Successfully unsubscribed "+name);
                connection.close();
                return;
            }
        /* set the exception listener */
        connection.setExceptionListener(this);

        /* create the destination */
        if (useTopic) {
            javax.jms.Topic topic = session.createTopic(name);
            /* create the consumer */
            subscriber = session.createDurableSubscriber(topic,name);
            subscriber.setMessageListener(this);
        } else{
            destination = session.createQueue(name);
            /* create the consumer */
            msgConsumer = session.createConsumer(destination);
            /* set the message listener */
            msgConsumer.setMessageListener(this);

        }

        /* start the connection */
        connection.start();

    }

    /* Handle the message upon receipt */
    public void onMessage(Message message) {
        String contents;
        String productID = "";
        int orderQty = 0;

        // Setting up database related variables
        String dbURL = "jdbc:mysql://localhost:3306/inventory_db";
        String userName = "root";
        String password = "";
        java.sql.Connection dbConn = null;
        PreparedStatement queryPstmt = null;
        PreparedStatement updatePstmt = null;
        ResultSet rs = null;

        try {
            if (message instanceof TextMessage) {
                // First get the message
                TextMessage textMessage = (TextMessage) message;
                contents = textMessage.getText();

                // Getting the required parameters from the message received
                // Breaking the message up via the delimiter " ", which is one
                // space character
                StringTokenizer st = new StringTokenizer(contents, " ");
                System.out.println("Order details received: " + contents);
                if (st.countTokens() == 0) {
                    // Gives an error if the the results of tokenization returns
                    // no tokens, meaning, the data cannot be splitted up
                    // meaningfully in the way we understood
                    System.out.println("Invalid parameters in message received.");
                } else {
                    while (st.hasMoreTokens()) {
                        String currToken = st.nextToken();
                        if (currToken.startsWith("ProductID=")) {
                            String[] s = currToken.split("=");
                            productID = s[1];
                        } else if (currToken.startsWith("Quantity=")) {
                            String[] s = currToken.split("=");
                            orderQty = Integer.parseInt(s[1]);
                        } else {
                            //do nothing - not interested in other parameters
                        }
                    }
                }

                // If the productID is not empty, we need to query and update
                // the database with the new quantity
                if (!productID.equals("")) {
                    // Preparing SQL statements to select and update record
                    String selectSql = "SELECT * FROM inventory WHERE id = ?";
                    String updateSql = "UPDATE inventory SET quantity = ? WHERE id = ?";
                    int inventoryQty = -1;
                    int reorderQty = -1;

                    // Connection to database "inventory" with authentication details in "userName"
                    // and "password"
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    dbConn = DriverManager.getConnection(dbURL, userName, password);
  
                    // Creating the prepared statement
                    queryPstmt = dbConn.prepareStatement(selectSql);

                    // Set value into selectSql's ProductID variable.
                    queryPstmt.setString(1, productID);
                    // Execute the query
                    rs = queryPstmt.executeQuery();

                    while (rs.next()) {
                        // Check if orderQty is less than inventoryQty
                        inventoryQty = Integer.parseInt(rs.getString("quantity"));
                        reorderQty = Integer.parseInt(rs.getString("reorder_quantity"));
                        System.out.println("Current database entry: Product = " + productID + ", Quantity = " + inventoryQty);
                        
                        if (orderQty <= inventoryQty) {
                            // we have enough inventory to cover this order,
                            // process this order and deduct the necessary
                            // quantity
                            inventoryQty = inventoryQty - orderQty;

                            // Prepare statement to update quantity
                            updatePstmt = dbConn.prepareStatement(updateSql);

                            // Set the 2 variables required by updatePstmt
                            updatePstmt.setInt(1, inventoryQty);
                            updatePstmt.setString(2, productID);

                            // Execute the database update
                            updatePstmt.executeUpdate();
                            System.out.println("Updated database: Product = " + productID + ", Quantity = " + inventoryQty);
                            updatePstmt.close();
                            //UpdateCon.close();

                            // Now check if replenishment is required, if
                            // inventoryQty goes below reorderQty, replenishment
                            // required
                            if (inventoryQty <= reorderQty) {
                              System.out.println("Stock running low.");
                              approvalRequest(rs.getString("supplier_id"), productID);
                            }
                        } else {
                            System.out.println("Insufficient quantity for the order.");
                            approvalRequest(rs.getString("supplier_id"), productID);
                        }


                        System.out.println("--------------");
                    }
                    queryPstmt.close(); // close the Statement
                    dbConn.close(); // close the Connection
                } else {
                    System.out.println("ProductID is not found");
                }

            } else {
                System.out.println("Invalid message detected");
                System.out.println("\tType:       " + message.getClass().getName());
                System.out.println("\tTime:       " + System.currentTimeMillis() + " ms");
                System.out.println("\tMessage ID: " + message.getJMSMessageID());
            }
        } catch (Exception err) {
            err.printStackTrace();
            System.out.println("ERROR: " + err);
        } finally {
          try {
            if (queryPstmt != null) {
              queryPstmt.close();
            }
            if (updatePstmt != null) {
              updatePstmt.close();
            }
            if (dbConn != null) {
              dbConn.close();
            }
          } catch (Exception e) {
            // We don't need to do anything here
          }
        }

    }

    public void approvalRequest(String supplier, String productID) {
        String approvalText = "Product " + productID + " requires approval for replenishment from supplier " + supplier;
        String args[] = {"-server", serverUrl, "-queue", "approval.request", approvalText};

        // Instantiate a generic message producer to send a message to Purchase
        // Approval System
        EIGenericMsgProducer msgProducer = new EIGenericMsgProducer(args);
    }


    /*-----------------------------------------------------------------------
    * main
    *----------------------------------------------------------------------*/
    public static void main(String[] args) {
        new EIInventoryConsumer(args);
    }

}








