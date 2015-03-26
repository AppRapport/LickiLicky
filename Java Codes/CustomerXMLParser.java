
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

    public CustomerXMLParser(String xmlString) {
	
		String full_name = "";
		String order_id = "";
		String total_price = "";
		String invoice_date = "";
        System.out.println("xmlString: " + xmlString);

        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);
            // Get the staff element by tag name directly
            Node order = doc.getElementsByTagName("order").item(0);


            // loop the child node
            NodeList list = order.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

				if ("invoice_date".equals(node.getNodeName())) {

					invoice_date =  node.getTextContent();
					System.out.println("Date: " + invoice_date);

				}
				if ("order_id".equals(node.getNodeName())) {

					order_id =  node.getTextContent();
					System.out.println("Order Id: " + order_id);

				}
				
                if ("full_name".equals(node.getNodeName())) {
					
					full_name =  node.getTextContent();
					System.out.println("Full name: " + full_name);
				
				}
				
				if ("ship_to_addr".equals(node.getNodeName())){
					
                    System.out.println("Shipping to: ");
					NodeList addressList = node.getChildNodes();
					
					for (int j = 0; j < addressList.getLength(); j++){
						Node addNode = addressList.item(j);
						
						String ship_address = "";
						String ship_to_state = "";
						String ship_to_zip_code = "";
						
						if("ship_address".equals(addNode.getNodeName())){
							
							ship_address = addNode.getTextContent();
							System.out.println("\tShipping Address: " + ship_address);
							
						}
						
						if("ship_to_state".equals(addNode.getNodeName())){
							
							ship_to_state = addNode.getTextContent();
							System.out.println("\tShipping State: " + ship_to_state);
							
						}
						
						if("ship_to_zip_code".equals(addNode.getNodeName())){
							
							ship_to_zip_code = addNode.getTextContent();
							System.out.println("\tShipping Zip Code: " + ship_to_zip_code);
							
						}
					}
					
				}
				
				if ("item".equals(node.getNodeName())) {
					
					NodeList itemList = node.getChildNodes();
						
					if(node.hasAttributes()){

						Element e = (Element)node;
						String id = e.getAttribute("id");
						String price = e.getAttribute("price");
						System.out.println("Item ID :" + id);
						System.out.println("\tPrice :" + price);

					}
					
					for (int k = 0; k < itemList.getLength(); k++){
						
						Node itemNode = itemList.item(k);
						
						String confirmed_quantity = "";
						String subtotal_price = "";
						String description = "";
						
						if ("description".equals(itemNode.getNodeName())){
							
							description = itemNode.getTextContent();
							System.out.println("\tDescription: " + description);
							
						}
						
						if ("subtotal_price".equals(itemNode.getNodeName())){
							
							subtotal_price = itemNode.getTextContent();
							System.out.println("\tSubtotal Price: " + subtotal_price);
							
						}
						
						if ("confirmed_quantity".equals(itemNode.getNodeName())){
							
							confirmed_quantity = itemNode.getTextContent();
							System.out.println("\tConfirmed Quantity: " + confirmed_quantity);
							
						}
						
					}	
				}
				
				if ("total_price".equals(node.getNodeName())) {
					total_price =  node.getTextContent();
					System.out.println("Total Price: " + total_price);
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

