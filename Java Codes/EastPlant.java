import javax.jms.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EastPlant implements ExceptionListener {
    /*-----------------------------------------------------------------------
     * Parameters
     *----------------------------------------------------------------------*/

    String serverUrl = null;
    String userName = "root";
    String password = "";
    String name = "topic.sample";
    boolean useTopic = true;
    PreparedStatement updatePstmt = null;
    PreparedStatement queryPstmt = null;

    /*-----------------------------------------------------------------------
    * Variables
    *----------------------------------------------------------------------*/
    javax.jms.Connection connection = null;
    Session session = null;
    MessageConsumer msgConsumer = null;
    Destination destination = null;

    public EastPlant(String[] args) {
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
		System.out.println("+-------------------------------------------------+");
		System.out.println("Customer Points Retrieval System");
		System.out.println("+-------------------------------------------------+");
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
		System.out.println("+-------------------------------------------------+");
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

        while (true) {
            /* receive the message */
            msg = msgConsumer.receive();

            if (msg == null)
                break;
			//System.err.println("Received message: " + msg);
            onMessage(msg);     //karway
            
        }

        /* close the connection */
        connection.close();
    }

    /*-----------------------------------------------------------------------
     * main
     *----------------------------------------------------------------------*/

    // Handle the message when received.
    public void onMessage(Message message) {
        try {
        	TextMessage requestMessage = (TextMessage) message;
			String messageToIM = processWest(requestMessage.getText()); 

			/*
			Destination replyDestination = message.getJMSReplyTo();
            MessageProducer replyProducer = session.createProducer(replyDestination);
            TextMessage replyMessage = session.createTextMessage();
			replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
			replyMessage.setText(messageToIM);
			replyProducer.send(replyMessage);*/

			String args2[] = {"-server", ipAddress, "-queue" , "q.back.plant", messageToIM};
			EIGenericMsgProducer producer = new EIGenericMsgProducer(args2);

        }	catch (Exception err){
			err.printStackTrace();
			System.out.println("Error: " + err);
		}
    }
	
	public static void main(String args[]){
		new EastPlant(args);
	}

	public String processWest(String xmlString) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String messageToPass = null;
		
		try {
			DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource inputStream = new InputSource(new StringReader(xmlString));
			Document dom = db.parse(inputStream);

			// Setting up database related variables
            String dbURL = "jdbc:mysql://localhost:3306/east_inventorydb";
            String username = "root";
            String password = "";
            java.sql.Connection conn = null;
            PreparedStatement pmst = null;
            ResultSet rs = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);

			//variables
            int quantitydb = 0;
            int quantity = 0;
            int orderId = 0;
            int postal = 0;
            int lowerThreshold = 0;
            int upperThreshold = 0;
            int orderToSupplier;
            boolean isOrdered = false;
            String firstname = null;
            String lastname = null;
            String company = null;
            String region = null;
            String city = null;
            String street = null;
            String state = null;
            String description = null;
            String itemTemplate = "";

            Node items = dom.getElementsByTagName("item").item(0);

            Node order = dom.getElementsByTagName("order").item(0);

            NodeList orderList = order.getChildNodes();

            for (int i = 0; i < orderList.getLength(); i++) {
                Node node = orderList.item(i);

                if ("first_name".equals(node.getNodeName())) {
                    firstname = node.getTextContent();
                    System.out.println("firstname: " + firstname);
                }

                if ("last_name".equals(node.getNodeName())) {
                    lastname = node.getTextContent();
                    System.out.println("lastname: " + lastname);
                }

                if ("order_id".equals(node.getNodeName())) {
                    orderId = Integer.parseInt(node.getTextContent());
                    System.out.println("order id: " + orderId);
                }

                if ("customer_company".equals(node.getNodeName())) {
                    company = node.getTextContent();
                    System.out.println("customer company: " + company);
                }

                if ("region".equals(node.getNodeName())) {
                    region = node.getTextContent();
                    System.out.println("region: " + region);
                }
                
                if ("ship_to_addr".equals(node.getNodeName())) {

                    NodeList addressList = node.getChildNodes();
                    
                    for (int j = 0; j < addressList.getLength(); j++) {
                        Node node2 = addressList.item(j);

                        if ("ship_to_city".equals(node2.getNodeName())) {
                            city = node2.getTextContent();
                            System.out.println("city: " + city);
                        }

                        if ("ship_to_street".equals(node2.getNodeName())) {
                            street = node2.getTextContent();
                            System.out.println("street: " + street);
                        }

                        if ("ship_to_zip_code".equals(node2.getNodeName())) {
                            postal = Integer.parseInt(node2.getTextContent());
                            System.out.println("postal: " + postal);
                        }

                        if ("ship_to_state".equals(node2.getNodeName())) {
                            state = node2.getTextContent();
                            System.out.println("state: " + state);
                        }
                    } 
                    
                }
                
                if ("item".equals(node.getNodeName())) {

                    NodeList itemList = node.getChildNodes();
                    int item_id = 0;
                    
                    if (node.hasAttributes()) {

                        Element e = (Element) node;
                        item_id = Integer.parseInt(e.getAttribute("id"));

                        System.out.println("ID: " + item_id);
                        
                    }
                    
                    for (int j = 0; j < itemList.getLength(); j++) {
                        Node node2 = itemList.item(j);

                        if ("description".equals(node2.getNodeName())) {
                            description = node2.getTextContent();
                            System.out.println("description: " + description);
                        }

                        if ("order_quantity".equals(node2.getNodeName())) {
                            quantity = Integer.parseInt(node2.getTextContent());
                            System.out.println("quantity: " + quantity);

                            String queryInventory = "SELECT * FROM inventory WHERE item_id = ?";

                            try {
                              pmst = conn.prepareStatement(queryInventory);
                              pmst.setInt(1, item_id);
                              rs = pmst.executeQuery();

                              while (rs.next()) {
                               quantitydb = rs.getInt(3);
                               isOrdered = rs.getBoolean(4);
                               upperThreshold = rs.getInt(5);
                               lowerThreshold = rs.getInt(6);
                              } 
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                           int quantityUpdate = quantitydb - quantity;
                           String updateInventory = "UPDATE inventory SET quantity= ? WHERE item_id = ?";

                           pmst = conn.prepareStatement(updateInventory);
                           pmst.setInt(1, quantityUpdate);
                           pmst.setInt(2, item_id);
                           pmst.executeUpdate();

                           System.out.println("Quantity Update: " +quantityUpdate);
                           System.out.println("Lower Threshold: " +lowerThreshold);

                           if(quantityUpdate < lowerThreshold && isOrdered == false) {
                            String updateIsOrdered = "UPDATE inventory SET is_ordered = ? WHERE item_id = ?";	
                            orderToSupplier = upperThreshold - quantityUpdate;

                            pmst = conn.prepareStatement(updateIsOrdered);
                            pmst.setBoolean(1, true);
                            pmst.setInt(2, item_id);
                            pmst.executeUpdate();

		          		    //create xml with additional element confirmed_quantity = quantity, order_to_supplier = orderToSupplier
                            itemTemplate = itemTemplate + "<item id=\"" + item_id + "\">\n"
                            + "<description>" + description + "</description>\n"
                            + "<order_quantity>" + quantity + "</order_quantity>\n"
                            + "<confirmed_quantity>" + quantity + "</confirmed_quantity>\n"
                            + "<order_from_supplier_quantity>" + orderToSupplier + "</order_from_supplier_quantity>\n"
                            + "</item>\n";
                            } else {

    		          		//create xml with additional element confirmed_quantity = quantity, order_to_supplier = 0
                             itemTemplate = itemTemplate + "<item id=\"" + item_id + "\">\n"
                             + "<description>" + description + "</description>\n"
                             + "<order_quantity>" + quantity + "</order_quantity>\n"
                             + "<confirmed_quantity>" + quantity + "</confirmed_quantity>\n"
                             + "<order_from_supplier_quantity>" + 0 + "</order_from_supplier_quantity>\n"
                             + "</item>\n";
                            }
                        }
                    }
                }
            }

            Element element = (Element) items;

            /*
            String description = element.getAttribute("description");
            System.out.println("description: " + description);*/

            //int item_id = Integer.parseInt(element.getAttribute("id"));
            //System.out.println("item id: " + item_id);    

            //NodeList itemList = items.getChildNodes();

            messageToPass = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                                        //+ "<!-- Created with Liquid XML 2014 Developer Bundle Edition (Education) 12.2.8.5459 (http://www.liquid-technologies.com) -->\n"
                                        + "<plant_order_reply xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/EI/Labs/LickiLicky/reply_from_plant.xsd\">\n"
                                        +   "<order_id>" + orderId + "</order_id>\n"
                                        +   "<customer_company>" + company + "</customer_company>\n"
                                        +   "<first_name>" + firstname + "</first_name>\n"
                                        +   "<last_name>" + lastname + "</last_name>\n"
                                        +   "<region>" + region + "</region>\n"
                                        +   "<ship_to_addr>\n"
                                        +       "<ship_to_street>" + street + "</ship_to_street>\n"
                                        +       "<ship_to_city>" + city + "</ship_to_city>\n"
                                        +       "<ship_to_state>" + state + "</ship_to_state>\n"
                                        +       "<ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                                        +   "</ship_to_addr>\n"
                                        +   itemTemplate + "\n"  
                                        + "</plant_order_reply>\n";
            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return messageToPass;
	}
}