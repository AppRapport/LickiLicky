/*
 * =================================================================
 * Copyright (c) 2001-2003 TIBCO Software Inc.
 * All rights reserved.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * $RCSfile: tibjmsMsgConsumer.java,v $
 * $Revision: 1.7 $
 * $Date: 2004/02/06 00:05:02 $
 * =================================================================
 */

/*
 * This is a simple sample of a basic tibjmsMsgConsumer.
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
 * Usage:  java tibjmsMsgConsumer [options]
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
 
import java.util.*;
import javax.jms.*;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;

public class COEAppRRConsumer
        implements ExceptionListener {
    /*-----------------------------------------------------------------------
     * Parameters
     *----------------------------------------------------------------------*/

    String serverUrl = null;
    String userName = null;
    String password = null;
    String name = "topic.sample";
    boolean useTopic = true;

    /*-----------------------------------------------------------------------
    * Variables
    *----------------------------------------------------------------------*/
    javax.jms.Connection connection = null;
    Session session = null;
    MessageConsumer msgConsumer = null;
    Destination destination = null;

    public COEAppRRConsumer(String[] args) {
        parseArgs(args);

        try {
            tibjmsUtilities.initSSLParams(serverUrl, args);
        }
        catch (JMSSecurityException e) {
            System.err.println("JMSSecurityException: " + e.getMessage() + ", provider=" + e.getErrorCode());
            e.printStackTrace();
            System.exit(0);
        }

        /* print parameters */
        System.err.println("\n------------------------------------------------------------------------");
        System.err.println("COE Application");
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
        System.err.println("\nUsage: tibjmsMsgConsumer [options] [ssl options]");
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

        System.err.println("Subscribing to destination: " + name + "\n");
        while (true) {
            ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);

            /* create the connection */
            connection = factory.createConnection(userName, password);

            /* create the session */
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            /* set the exception listener */
            connection.setExceptionListener(this);

            /* create the destination */
            if (useTopic)
                destination = session.createTopic(name);
            else
                destination = session.createQueue(name);

            /* create the consumer */
            msgConsumer = session.createConsumer(destination);

            /* start the connection */
            connection.start();

            /* receive the message */
			//there will be a message
            msg = msgConsumer.receive();
			
            if (msg == null){
                break;
				}
            onMessage(msg);     //karway
			
            //System.err.println("Received message: " + msg);
        }

        /* close the connection */
        connection.close();
    }

    /*-----------------------------------------------------------------------
     * main
     *----------------------------------------------------------------------*/
    public static void main(String[] args) {
        new COEAppRRConsumer(args);
    }

    // Handle the message when received.
    public void onMessage(Message message) {
        try {
            if ((message instanceof TextMessage) && (message.getJMSReplyTo() != null)) {
                TextMessage requestMessage = (TextMessage) message;

                System.out.println("Received request from IM:");
                System.out.println("Time:\t\t" + System.currentTimeMillis() + " ms");
                System.out.println("Reply to:\t" + requestMessage.getJMSReplyTo());
                System.out.println("Car Plate No:\t" + requestMessage.getText());

                // Prepare reply message and send reply message
                String carPlateNo = requestMessage.getText();
                Destination replyDestination = message.getJMSReplyTo();
                MessageProducer replyProducer = session.createProducer(replyDestination);
				
				// query from the database
				String dbURL = "jdbc:mysql://localhost:3306/coedb";
                String userName = "root";
				String password = "";
				java.sql.Connection dbConn = null;
				Statement statement = null;
				String selectSQL= "SELECT * from userdatabase where CarplateNo=\""+ carPlateNo + "\"";
				String carOwnerName="";
				String carOwnerIC="";
				String carOwnerAddress="";
				String carType="";

				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					dbConn = DriverManager.getConnection(dbURL, userName, password);

					statement = dbConn.createStatement();
					statement.executeQuery(selectSQL);

				ResultSet rs = statement.getResultSet();
				//int count = 0;
				
				while (rs.next()) {
					carOwnerName = rs.getString("CarOwnerName");
					carOwnerIC= rs.getString("CarOwnerIC");
					carOwnerAddress=rs.getString("CarOwnerAddress");
					carType = rs.getString("CarType");
				}

				rs.close();
				statement.close(); // close the Statement
				dbConn.close(); // close the Connection

				} catch (Exception e) {
				e.printStackTrace();                                       
				}
				
                TextMessage replyMessage = session.createTextMessage();
                // Hardcoded the replyMessage to for this example.
				
                replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());// the correlation id of the reply messgae is being set to the request message id
                // sending reply message.
				
				ModifyXML(carPlateNo, carOwnerName, carOwnerIC, carOwnerAddress, carType);
				
				replyMessage.setText(this.convertXMLFileToString("C:\\EI\\COEinfo.xml"));
				
                replyProducer.send(replyMessage);
				
				System.out.println();
                System.out.println("Sent xmlString to IM:");
                System.out.println("Time:\t\t" + System.currentTimeMillis() + " ms");
                System.out.println("Contents:");
				System.out.println(replyMessage.getText());
            } else {
                System.out.println("Invalid message detected");
                System.out.println("\tType:       " + message.getClass().getName());
                System.out.println("\tTime:       " + System.currentTimeMillis() + " ms");
                System.out.println("\tMessage ID: " + message.getJMSMessageID());
                System.out.println("\tCorrel. ID: " + message.getJMSCorrelationID());
                System.out.println("\tReply to:   " + message.getJMSReplyTo());

                message.setJMSCorrelationID(message.getJMSMessageID());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
	
	public void ModifyXML(String carPlateNo, String carOwnerName, String carOwnerIC, String carOwnerAddress, String carType) {
        try {
            String filepath = "C:\\EI\\COEinfo.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node root = doc.getFirstChild();

            // Get the staff element by tag name directly
            Node coe = doc.getElementsByTagName("coe").item(0);

            // loop the staff child node
            NodeList list = coe.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if ("carplateno".equals(node.getNodeName())) {
                    node.setTextContent(carPlateNo);
                }

                if ("carownername".equals(node.getNodeName())) {
                    node.setTextContent(carOwnerName);
                }

                if ("carowneric".equals(node.getNodeName())) {
                    node.setTextContent(carOwnerIC);
                }
                if ("carowneraddress".equals(node.getNodeName())) {
                    node.setTextContent(carOwnerAddress);
                }

                if ("cartype".equals(node.getNodeName())) {
                    node.setTextContent(carType);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }
	
	public String convertXMLFileToString(String fileName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            InputStream inputStream = new FileInputStream(new File(fileName));
            org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stw));
            return stw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
