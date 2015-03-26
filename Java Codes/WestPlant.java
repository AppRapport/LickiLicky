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

public class WestPlant implements ExceptionListener {
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

    public WestPlant(String[] args) {
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

			Destination replyDestination = message.getJMSReplyTo();
            MessageProducer replyProducer = session.createProducer(replyDestination);
            TextMessage replyMessage = session.createTextMessage();
			replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
			replyMessage.setText(messageToIM);
			replyProducer.send(replyMessage);

        }	catch (Exception err){
			err.printStackTrace();
			System.out.println("Error: " + err);
		}
    }
	
	public static void main(String args[]){
		new WestPlant(args);
	}

	public String processWest(String xmlString) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource inputStream = new InputSource(new StringReader(xmlString));
			Document dom = db.parse(inputStream);

			// Setting up database related variables
            String dbURL = "jdbc:mysql://localhost:3306/west_inventorydb";
            String username = "root";
            String password = "";
            java.sql.Connection conn = null;
            PreparedStatement pmst = null;
            ResultSet rs = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(dbURL, username, password);

			//variables
            int quantitydb;
			int quantity;
			int orderId;
			int postal;
			int lowerThreshold;
			int upperThreshold;
			int orderToSupplier;
			boolean isOrdered;
			String firstname;
			String lastname;
			String company;
			String region;
			String city;
			String street;
			String messageToPass;
			String state;
			String itemTemplate = "";

            Node items = dom.getElementsByTagName("item").item(0);

            Node order = dom.getElementsByTagName("order").item(0);

            Node address = dom.getElementsByTagName("ship_to_addr").item(0);

            NodeList orderList = order.getChildNodes();

            NodeList addressList = address.getChildNodes();

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
            }

			for (int i = 0; i < addressList.getLength(); i++) {
                Node node = addressList.item(i);

                if ("ship_to_city".equals(node.getNodeName())) {
                    city = node.getTextContent();
                    System.out.println("city: " + city);
                }

                if ("ship_to_street".equals(node.getNodeName())) {
                    street = node.getTextContent();
                    System.out.println("street: " + street);
                }

                if ("ship_to_zip_code".equals(node.getNodeName())) {
                    postal = Integer.parseInt(node.getTextContent());
                    System.out.println("postal: " + postal);
                }

                if ("ship_to_state".equals(node.getNodeName())) {
                    state = node.getTextContent();
                    System.out.println("state: " + state);
                }
            }            

            Element element = (Element) items;

            /*
            String description = element.getAttribute("description");
            System.out.println("description: " + description);*/

            int item_id = Integer.parseInt(element.getAttribute("id"));
			System.out.println("item id: " + item_id);	

            NodeList itemList = items.getChildNodes();

            for (int i = 0; i < itemList.getLength(); i++) {
                Node node = itemList.item(i);

                if ("order_quantity".equals(node.getNodeName())) {
                    quantity = Integer.parseInt(node.getTextContent());
                    System.out.println("quantity: " + quantity);
                }

                if ("description".equals(node.getNodeName())) {
                    description = node.getTextContent();
                    System.out.println("description: " + description);
                }

                String queryInventory = "SELECT * FROM `inventory` WHERE `item_id` = ?"

	            try {
		            pmst = conn.prepareStatement(queryInventory);
					pmst.setString(1, item_id);
					rs = pmst.executeQuery();

					while (rs.next()) {
		              	quantitydb = rs.getInt(3);
		              	is_ordered = rs.getBoolean(4);
		              	upperThreshold = rs.getInt(5);
		              	lowerThreshold = rs.getInt(6);
		            }

		            int quantityUpdate = quantitydb - quantity;
	            	String updateInventory = "UPDATE `inventory` SET `quantity`= ? WHERE `item_id`= ?";

		            psmt = conn.prepareStatement(updateInventory);
		            psmt.setInt(1, quantityUpdate);
		            psmt.setInt(2, item_id);
		            psmt.executeUpdate();

			        if(quantityUpdate < lowerThreshold && isOrdered == false) {
				        String updateIsOrdered = "UPDATE `inventory` SET `is_ordered`= ? WHERE `item_id`= ?";	
				        orderToSupplier = upperThreshold - quantityUpdate;

			          	psmt = conn.prepareStatement(updateIsOrdered);
			            psmt.setBoolean(1, true);
			            psmt.setInt(2, item_id);
			            psmt.executeUpdate();

		          		//create xml with additional element confirmed_quantity = quantity, order_to_supplier = orderToSupplier
		          		itemTemplate = itemTemplate + "<item id=\"" + item_id + \">"
									  + "<confirmed_quantity>" + quantity + "</confirmed_quantity>"
									  + "<order_from_supplier_quantity>" + orderToSupplier + "</order_from_supplier_quantity>"
									  + "</item>"
		          	} else {
		          		//create xml with additional element confirmed_quantity = quantity, order_to_supplier = 0
		           		itemTemplate = itemTemplate + "<item id=\"" + item_id + \">"
									  + "<confirmed_quantity>" + quantity + "</confirmed_quantity>"
									  + "<order_from_supplier_quantity>" + 0 + "</order_from_supplier_quantity>"
									  + "</item>"
		          	}

		            /*if(quantitydb >= quantity) {
			            try {
			            	int quantityUpdate = quantitydb - quantity;
			            	String updateInventory = "UPDATE `inventory` SET `quantity`= ? WHERE `item_id`= ?";

				            psmt = conn.prepareStatement(updateInventory);
				            psmt.setInt(1, quantityUpdate);
				            psmt.setInt(2, item_id);
				            psmt.executeUpdate();

	 			          	//create xml with additional element confirmed_quantity = quantity, order_to_supplier = 0
				            itemTemplate = itemTemplate + "<item id=\"" + item_id + \">"
								  + "<confirmed_quantity>" + quantity + "</confirmed_quantity>"
								  + "<order_from_supplier_quantity>" + 0 + "</order_from_supplier_quantity>"
								  + "</item>"


	 			          	if(quantityUpdate < lowerThreshold && isOrdered == false) {
	 			          		String updateIsOrdered = "UPDATE `inventory` SET `is_ordered`= ? WHERE `item_id`= ?";	
	 			          		orderToSupplier = upperThreshold - quantityUpdate;

	 			          		psmt = conn.prepareStatement(updateIsOrdered);
					            psmt.setBoolean(1, true);
					            psmt.setInt(2, item_id);
					            psmt.executeUpdate();

	 			          		//create xml with additional element confirmed_quantity = quantity, order_to_supplier = orderToSupplier
	 			          		itemTemplate = itemTemplate + "<item id=\"" + item_id + \">"
								  + "<confirmed_quantity>" + quantity + "</confirmed_quantity>"
								  + "<order_from_supplier_quantity>" + orderToSupplier + "</order_from_supplier_quantity>"
								  + "</item>"
	 			          	}
		        		} 
		            } else {
		            	if(isOrdered == false) {
		            		String updateIsOrdered = "UPDATE `inventory` SET `is_ordered`= ? WHERE `item_id`= ?";	
		            		psmt = conn.prepareStatement(updateIsOrdered);
				            psmt.setBoolean(1, true);
				            psmt.setInt(2, item_id);
				            psmt.executeUpdate();

				            	//create xml with additional element confirmed_quantity = quantity, order_to_supplier = orderToSupplier
				            	itemTemplate = itemTemplate + "<item id=\"" + item_id + \">"
								  + "<confirmed_quantity>" + quantity + "</confirmed_quantity>"
								  + "<order_from_supplier_quantity>" + orderToSupplier + "</order_from_supplier_quantity>"
								  + "</item>"

		            	} else {
		            		 	//create xml with additional element confirmed_quantity = quantity, order_to_supplier = 0

		            	}
		            	String updateInventory = "UPDATE `inventory` SET `quantity`= ? WHERE `item_id`= ?";

		            	psmt = conn.prepareStatement(updateInventory);
				        psmt.setInt(1, 0);
				        psmt.setInt(2, item_id);
				        psmt.executeUpdate();

				        //create xml with additional element confirmed_quantity = quantitydb, order_to_supplier = upperThreshold

		            }*/
	            } finally {
	            	ConnectionManager.close(conn, psmt, rs);
	       		}
            }

       		messageToPass = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
										+ "<!-- Created with Liquid XML 2014 Developer Bundle Edition (Education) 12.2.8.5459 (http://www.liquid-technologies.com) -->"
										+ "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:\EI\Labs\LickiLicky\reply_from_plant.xsd\">"
										+ 	"<first_name>" + firstname + "</first_name>"
										+ 	"<last_name>" + lastname + "</last_name>"
										+ 	"<region>" + region + "</region>"
										+ 	"<ship_to_addr>"
										+       "<ship_to_street>" + street + "</ship_to_street>"
										+		"<ship_to_city>" + city + "</ship_to_city>"
										+       "<ship_to_zip_code>" + postal + "</ship_to_zip_code>"
										+   "</ship_to_addr>"
										+ 	itemTemplate  
										+   "<order_id>" + orderId + "</order_id>"
										+   "<customer_company>" + company + "</customer_company>"
										+ "</order>"

           /*
	        pmst.close(); // close the Statement
	        conn.close(); // close the Connection
			*/
			
			return messageToPass;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}