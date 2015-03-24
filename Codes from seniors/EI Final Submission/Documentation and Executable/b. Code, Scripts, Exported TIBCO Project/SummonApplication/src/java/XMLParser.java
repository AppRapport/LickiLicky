
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
 * @author Chun Tat
 */
public class XMLParser {

    private String id = "";
    String cPlate = "";
    String cID = "";
    String time = "";
    String fineApplicable = "";
    String fine = "";
    String seasonpark = "";
    String offence = "";

    public XMLParser(String xmlString) {
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


            // loop the staff child node
            NodeList list = summon.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if ("sid".equals(node.getNodeName())) {

                    id = node.getTextContent();
                    System.out.println("id: " + id);
                }

                if ("vehno".equals(node.getNodeName())) {
                    cPlate = node.getTextContent();
                    System.out.println("vehno: " + cPlate);
                }

                if ("carparkid".equals(node.getNodeName())) {
                    cID = node.getTextContent();
                    System.out.println("cID: " + cID);
                }


                if ("time".equals(node.getNodeName())) {
                    time = node.getTextContent();
                    System.out.println("time: " + time);
                }

                if ("fineapplicable".equals(node.getNodeName())) {
                    fineApplicable = node.getTextContent();
                    System.out.println("fineApplicable: " + fineApplicable);
                }
                if ("seasonpark".equals(node.getNodeName())) {
                    seasonpark = node.getTextContent();
                    System.out.println("seasonpark: " + seasonpark);
                }
                if ("offence".equals(node.getNodeName())) {
                    offence = node.getTextContent();
                    System.out.println("offence: " + offence);
                }

                if ("fineamt".equals(node.getNodeName())) {
                    fine = node.getTextContent();
                    System.out.println("fine: " + fine);
                }
            }


            // Setting up database related variables
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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
}
