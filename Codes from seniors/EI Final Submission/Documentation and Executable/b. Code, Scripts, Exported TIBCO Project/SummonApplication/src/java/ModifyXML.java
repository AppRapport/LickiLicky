
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chun Tat
 */
public class ModifyXML {

    public ModifyXML(String cPlate, String cID, String seasonpark, String offence) {
        try {
            String filepath = "C:\\EI\\summon.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);


            // Get the root element
            Node root = doc.getFirstChild();


            // Get the staff element by tag name directly
            Node summon = doc.getElementsByTagName("summon").item(0);


            // loop the staff child node
            NodeList list = summon.getChildNodes();

            UUID summonID = UUID.randomUUID();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                
                if ("sid".equals(node.getNodeName())) {
                    node.setTextContent(summonID.toString());
                }

                if ("vehno".equals(node.getNodeName())) {
                    node.setTextContent(cPlate);
                }

                if ("carparkid".equals(node.getNodeName())) {
                    node.setTextContent(cID);
                }

                if ("seasonpark".equals(node.getNodeName())) {
                    node.setTextContent(seasonpark);
                }
                if ("offence".equals(node.getNodeName())) {
                    node.setTextContent(offence);
                }

                if ("time".equals(node.getNodeName())) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    node.setTextContent(dateFormat.format(date));
                }


            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            // System.out.println("Done");

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
}
