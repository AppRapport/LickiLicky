
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
public class SupplierXMLParser {

    String region = "";
	String item = "";
    /*
	String description = "";
    int order_quantity = "";
	byte id = "";
    */
    public SupplierXMLParser(String xmlString) {
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

                if ("region".equals(node.getNodeName())) {

                    region = node.getTextContent();
                    System.out.println("Region: " + region);
                }
			
                if ("item".equals(node.getNodeName())) {
                    item = node.getTextContent();
                    System.out.println("Item: ");
					NodeList itemList = summon.getChildNodes();
					
					//loop the elements in the item child node
					for (int j = 0; j < itemList.getLength(); j++){
					
						Node itemNode = itemList.item(j);
						
						String description = "";
						String order_from_supplier_quantity = "";
						String id = "";
						
						if("description".equals(itemNode.getNodeName())) {
							
							description = itemNode.getTextContent();
							System.out.println("Description: " + description);
							
						}
						
						if("order_from_supplier_quantity".equals(itemNode.getNodeName())) {
							
							order_from_supplier_quantity = itemNode.getTextContent();
							System.out.println("Order from Supplier Quantity: " + description);
							
						}
						
						if("id".equals(itemNode.getNodeName())) {
							
							id = itemNode.getTextContent();
							System.out.println("Id: " + description);
							
						}
					
					}
                }
            }


            // Setting up database related variables
            /*String dbURL = "jdbc:mysql://localhost:3306/summondb";
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
