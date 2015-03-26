import javax.jms.*;
import java.sql.*;
import java.util.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WestPlant
implements ExceptionListener {
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
   	ArrayList <Order> orderList = null;

    /*-----------------------------------------------------------------------
    * Variables
    *----------------------------------------------------------------------*/
    javax.jms.Connection connection = null;
    Session session = null;
    MessageConsumer msgConsumer = null;
    Destination destination = null;

    public CRMS(String[] args) {
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
			System.err.println("Received message: " + msg);
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
            if ((message instanceof TextMessage) && (message.getJMSReplyTo() != null)) {
                TextMessage requestMessage = (TextMessage) message;
				System.out.println();
                System.out.println("Received request");
                System.out.println("\tTime:       " + System.currentTimeMillis() + " ms");
                System.out.println("\tMessage ID: " + requestMessage.getJMSMessageID());
                System.out.println("\tCorrel. ID: " + requestMessage.getJMSCorrelationID());
                System.out.println("\tReply to:   " + requestMessage.getJMSReplyTo());
                System.out.println("\tContents:   " + requestMessage.getText());

                 // Prepare reply message and send reply message
                String xmlString = requestMessage.getText();
                Destination replyDestination = message.getJMSReplyTo();
                MessageProducer replyProducer = session.createProducer(replyDestination);                
                TextMessage replyMessage = session.createTextMessage();

                parseXmlFile(xmlString);
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
        } catch (Exception err){
			err.printStackTrace();
			System.out.println("Error: " + err);
		}
    }
	
	public static void main(String args[]){
		new CRMS(args);
	}

	public void parseXmlFile(String xmlString) {
		System.out.println("XMLString: " + xmlString);

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			//variables
			String firstName;
			String lastName;
			String region;
			String orderId;
			String description;
			String street;
			String city;
			String state;
			int quantitydb;
			int postal;
			int item_id;
			int quantity;

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource inputStream = new InputSource(new StringReader(xmlString));

			//parse using builder to get DOM representation of the XML file
			Document dom = db.parse(inputStream);

            //Get the item element by tag name directly
            Node items = doc.getElementsByTagName("item").item(0);

            // loop the staff child node of item details
            NodeList itemList = items.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {
                Node node = itemList.item(i);

                if ("description".equals(node.getNodeName())) {
                    description = node.getTextContent();
                    System.out.println("description: " + description);
                }

                if ("order_quantity".equals(node.getNodeName())) {
                    quantity = node.getTextContent();
                    System.out.println("quantity: " + city);
                }

                if ("id".equals(node.getNodeName())) {
                    item_id = node.getTextContent();
                    System.out.println("item id: " + state);
                }
            }

            // Setting up database related variables
            String dbURL = "jdbc:mysql://localhost:3306/west_inventorydb";
            String username = "root";
            String password = "";
            java.sql.Connection conn = null;
            PreparedStatement pmst = null;
            ResultSet rs = null;

            Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(dbURL, username, password);

            String queryInventory = "SELECT `quantity` FROM `inventory` WHERE `item_id` = ?"

            try {
	            pmst = conn.prepareStatement(queryInventory);
				pmst.setString(1, item_id);
				rs = pmst.executeQuery();

				while (rs.next()) {
	              	quantitydb = rs.getInt(1);
	            }

	            if(quantitydb >= quantity) {
		            try {
		            	int quantityUpdate = quantitydb - quantity;
		            	String updateInventory = "UPDATE `inventory` SET `quantity`= ? WHERE `item_id`= ?";

			            psmt = conn.prepareStatement(updateInventory);
			            psmt.setInt(1, quantityUpdate);
			            psmt.setInt(2, item_id);
			            psmt.executeUpdate();

			            
	        		} 
	            } else {

	            }
            } finally {
            	ConnectionManager.close(conn, psmt, rs);
       		 }

           /*
	        pmst.close(); // close the Statement
	        conn.close(); // close the Connection
			*/
			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}catch(Exception e) {x-apple-ql-id://1ADD93EA-6C13-4981-86E2-E3FDEB4A459F/x-apple-ql-magic/3892FDC3-8F2F-4E10-AD61-DABEDE613922.png
			e.printStackTrace();
		}
	}

	/*
	private void parseDocument(Document dom){
		//get the root element
		Element docEle = dom.getDocumentElement();

		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("Order");

		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength(); i++) {

				//get the Order element
				Element el = (Element)nl.item(i);

				//get the Order object
				Order order = getOrder(el);

				//add it to list
				orderList.add(order);
			}
		}
	}

	//create Order object from the element and pass it back
	private Order getOrder (Element element) {
		//for each <order> element get text or double values of email, price, quantity, warehouse name
		String email = getTextValue(element,"email");
		int quantity = getIntValue(element,"quantity");
		double price = getDoubleValue(element,"price");
		String warehouse = element.getTextValue(element, "warehouse");
		
		/* used only when type is included in the xml   
			String email = element.getAttribute("email");
		*/

		/*
		//Create a new Employee with the value read from the xml nodes
		Order order = new Order(email,quantity, price, warehouse);

		return order;
	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}


	private int getDoubleValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Double.parseDouble(getTextValue(ele,tagName));
	}*/	
}