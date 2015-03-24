
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import java.sql.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LickiLicky
 */
public class CustomerXMLParser {

    String order = "";
		//order sub elements
		/*
			
			
			String ship_to_addr ="";
			//ship_to_addr sub elements
				String ship_address = "";
				String ship_to_state = "";
				String ship_to_zip_code = "";
			
			String item = "";
			//item sub elements
				String description = "";
				String confirmed_quantity = "";
				String subtotal_price = "";
				String id = "";
				String price = "";
				
			String order_id = "";
			String total_price = "";
			String invoice_date = "";
		*/
    

    public CustomerXMLParser(String xmlString) {
        System.out.println("xmlString: " + xmlString);

        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);
            // Get the staff element by tag name directly
            Node summon = doc.getElementsByTagName("summon").item(0);


            // loop the child node
            NodeList list = summon.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if ("order".equals(node.getNodeName())) {

                    order = node.getTextContent();
                    System.out.println("Order: ");
					
					String full_name = "";
					String ship_to_addr ="";
					String item = "";
					String order_id = "";
					String total_price = "";
					String invoice_date = "";
					
					NodeList orderList = summon.getChildNodes();
					for (int j = 0; j < orderList.getLength(); j++){
						
						Node orderNode = orderList.item(j);
						
						if ("full_name".equals(orderNode.getNodeName())) {
							
							full_name =  orderNode.getTextContent();
							System.out.println("Full name: " + full_name);
							
						}
						
						NodeList addressList = summon.getChildNodes();
						if ("ship_to_addr".equals(orderNode.getNodeName())) {
							
							ship_to_addr =  orderNode.getTextContent();
							System.out.println("Ship to: ");
							
							NodeList shipmentAddressList = summon.getChildNodes();
							for (int k = 0; k < addressList.getLength(); k++) {
								Node addNode = addressList.item(k);
								
								String ship_address = "";
								String ship_to_state = "";
								String ship_to_zip_code = "";
								
								if ("ship_address".equals(addNode.getNodeName())) {
									
									ship_address = addNode.getTextContent();
									System.out.println("Ship to Address: " + ship_address);
								
								}
								
								if ("ship_to_state".equals(addNode.getNodeName())) {
									
									ship_to_state = addNode.getTextContent();
									System.out.println("Ship to State: " + ship_to_state);
								
								}
								
								if ("ship_to_zip_code".equals(addNode.getNodeName())) {
									
									ship_to_zip_code = addNode.getTextContent();
									System.out.println("Ship to zip code: " + ship_to_zip_code);
								
								}
								
							}
							
						}
						
						if ("item".equals(orderNode.getNodeName())) {
							
							item = orderNode.getTextContent();
							System.out.println("Item: ");
							NodeList itemList = summon.getChildNodes();
							
							for (int k = 0; k < addressList.getLength(); k++) {
								
								Node itemNode = itemList.item(k);
								String description = "";
								String confirmed_quantity = "";
								String subtotal_price = "";
								String id = "";
								String price = "";
								
								if ("description".equals(itemNode.getNodeName())) {
									
									description = itemNode.getTextContent();
									System.out.println("Description: " + description);
									
								}
								
								if ("confirmed_quantity".equals(itemNode.getNodeName())) {
									
									confirmed_quantity = itemNode.getTextContent();
									System.out.println("Confirmed Quantity: " + confirmed_quantity);
									
								}
								
								if ("subtotal_price".equals(itemNode.getNodeName())) {
									
									subtotal_price = itemNode.getTextContent();
									System.out.println("Subtotal Price: " + subtotal_price);
									
								}
								
								if ("id".equals(itemNode.getNodeName())) {
									
									id = itemNode.getTextContent();
									System.out.println("Id: " + id);
									
								}
								
								if ("price".equals(itemNode.getNodeName())) {
									
									price = itemNode.getTextContent();
									System.out.println("Price: " + price);
									
								}
							}
						}
						
						if ("order_id".equals(orderNode.getNodeName())) {
							
							order_id =  orderNode.getTextContent();
							System.out.println("Order Id: " + order_id);
							
						}
						
						if ("total_price".equals(orderNode.getNodeName())) {
							
							total_price =  orderNode.getTextContent();
							System.out.println("Total Price: " + total_price);
							
						}
						
						if ("invoice_date".equals(orderNode.getNodeName())) {
							
							invoice_date =  orderNode.getTextContent();
							System.out.println("Date: " + invoice_date);
							
						}
					}
                }

               
            }


            // Setting up database related variables
            /* 
		    String dbURL = "jdbc:mysql://localhost:3306/summondb";
            String userName = "root";
            String password = "";
            java.sql.Connection dbConn = null;
            Statement statement = null;

            String insertSql = "INSERT into summon_records(id,carplate,carparkid,Stime,fineapplicable,seasonpark,offence,fineamt) values ('"
                    + id + "','" + cPlate + "','" + cID + "','" + time + "','" + fineApplicable + "','"+ seasonpark + "','"+ offence + "','" + fine + "')";

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            dbConn = DriverManager.getConnection(dbURL, userName, password);

            statement = dbConn.createStatement();
            statement.executeUpdate(insertSql);

            statement.close(); // close the Statement
            dbConn.close(); // close the Connection
			*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the id 
    public String getId() {
        return id;
    }
	*/
}
