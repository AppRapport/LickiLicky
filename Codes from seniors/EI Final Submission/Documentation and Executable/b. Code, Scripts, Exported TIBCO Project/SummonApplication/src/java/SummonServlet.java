/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import javax.jms.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Chun Tat
 */
public class SummonServlet extends HttpServlet {

    static boolean requestSent = false;
    static boolean found = false;
    String carPlate = null;
    String carparkID = null;
    String lstOffence = null;
    String sParking = null;
    /*-----------------------------------------------------------------------
     * Parameters
     *----------------------------------------------------------------------*/
    String serverUrl = null;
    String userName = null;
    String password = null;
    String name = "topic.sample";
    Vector data = new Vector();
    boolean useTopic = true;

    /*-----------------------------------------------------------------------
     * Variables
     *----------------------------------------------------------------------*/
    Connection connection = null;
    Session session = null;
    MessageProducer msgProducer = null;
    Destination destination = null;
    Destination replyDestination = null; //for request-reply
    MessageConsumer replyConsumer; //for request-reply

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        PrintWriter out = response.getWriter();

        try {

            String id = "";


            if (!requestSent) {
                response.addHeader("Refresh", "5");
                out.println("<link href='./assets/css/bootstrap.css' rel='stylesheet'>");

                out.println("<style>");
                out.println("  body {");
                out.println("padding-top: 60px;");
                out.println("}");
                out.println(" </style>");
                out.println("<link href='./assets/css/bootstrap-responsive.css' rel='stylesheet'>");

                out.println("<script src='http://html5shim.googlecode.com/svn/trunk/html5.js'></script>");

                out.println("</head>");

                out.println("<body>");
                out.println("<div class='navbar navbar-fixed-top'>");
                out.println("<div class='navbar-inner'>");
                out.println("<div class='container'>");
                out.println("<a class='btn btn-navbar' data-toggle='collapse' data-target='.nav-collapse'>");
                out.println("<span class='icon-bar'></span>");
                out.println("<span class='icon-bar'></span>");
                out.println("<span class='icon-bar'></span>");
                out.println("</a>");



                out.println("<a class='brand' href='#'>LTA Summon Application</a>");
                out.println("<div class='nav-collapse'>");
                out.println("<ul class='nav'>");
                out.println("<li class='active'><a href='index.jsp'>Summon</a></li>");

                out.println("</ul>");
                out.println("</div><!--/.nav-collapse -->");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");

                out.println("<div class='container'>");


                out.println("<div class='hero-unit'>");
                out.println("<center><b>Please wait while we process your request...</b></center>");
                out.println("<center><img src='./assets/img/ajax-loader.gif' /></center>");
                out.println("</div></div></body></html>");


                carPlate = request.getParameter("carplate");
                carparkID = request.getParameter("cpid");
                lstOffence = request.getParameter("lstOffence");
                sParking = request.getParameter("sParking");

                ModifyXML xml = new ModifyXML(carPlate, carparkID, sParking, lstOffence);
                //change this content
                String[] args = {"-server", "127.0.0.1", "-queue", "request.summon", carPlate + "," + carparkID};
                parseArgs(args);

                try {
                    tibjmsUtilities.initSSLParams(serverUrl, args);
                } catch (JMSSecurityException e) {
                    System.err.println("JMSSecurityException: " + e.getMessage() + ", provider=" + e.getErrorCode());
                    e.printStackTrace();
                    System.exit(0);
                }

                /* print parameters */
//                out.println("<br />------------------------------------------------------------------------");
//                out.println("<br />Summon System");
//                out.println("<br />------------------------------------------------------------------------");
//                out.println("<br />Server....................... " + ((serverUrl != null) ? serverUrl : "localhost"));
//                out.println("<br />User......................... " + ((userName != null) ? userName : "(null)"));
//                out.println("<br />Destination.................. " + name);
//                out.println("<br />Message Text................. ");
//                for (int i = 0; i < data.size(); i++) {
//                    out.println(data.elementAt(i));
//                }
//                out.println("<br />------------------------------------------------------------------------");
//
                TextMessage msg;
                int i;
//
//                if (data.size() == 0) {
//                    out.println("***Error: must specify at least one message text\n");
//                    usage();
//                }
//
//                out.println("<br /><br />Publishing to destination '" + name + "'");


                ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);

                connection = factory.createConnection(userName, password);

                /* create the session */
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                /* create the destination */
                if (useTopic) {
                    destination = session.createTopic(name);
                    //by default is "reply.topic"
                    replyDestination = session.createTopic("reply.summon"); //for request-reply
                } else {
                    destination = session.createQueue(name);

                    //by default is "reply.queue"
                    replyDestination = session.createQueue("reply.summon"); //for request-reply
                }

                /* create the producer */
                msgProducer = session.createProducer(destination); //changed for request-reply
                   /* start the connection */
                connection.start();


                /* publish messages */
                //for (i = 0; i < data.size(); i++) {
                    /* create text message */
                    msg = session.createTextMessage();

                    /* set message text */
                    msg.setText(this.convertXMLFileToString("C:\\EI\\summon.xml"));//(String) data.elementAt(i));
                    msg.setJMSReplyTo(replyDestination); //for request-reply

                    /* publish message */
                    msgProducer.send(msg);   //changed for request-reply

//                    out.println("<br />Published message: " + data.elementAt(i));
//                    out.println("<br />Sent request");
//                    out.println("<br />Time:       " + System.currentTimeMillis() + " ms");
//                    out.println("<br />Message ID: " + msg.getJMSMessageID());
//                    out.println("<br />Correl. ID: " + msg.getJMSCorrelationID());
//                    out.println("<br />Reply to:   " + msg.getJMSReplyTo());
//                    out.println("<br />Contents:   " + msg.getText());
                //}

                requestSent = true;


            } else {
                ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory("127.0.0.1");

                connection = factory.createConnection("", "");

                /* create the session */
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                destination = session.createQueue("q.summon");

                //by default is "reply.queue"
                replyDestination = session.createQueue("reply.summon"); //for request-reply

                replyConsumer = session.createConsumer(replyDestination); //for request-reply

                /* start the connection */
                connection.start();

                // Send a request and wait for a reply. Code also can be added to time-out the wait
                Message reply = replyConsumer.receive();


                // Process the reply.
                if (reply instanceof TextMessage) {
                    TextMessage replyMessage = (TextMessage) reply;
                    out.println("<br />Received reply ");
                    out.println("<br />Time:       " + System.currentTimeMillis() + " ms");
                    out.println("<br />Message ID: " + replyMessage.getJMSMessageID());
                    out.println("<br />Correl. ID: " + replyMessage.getJMSCorrelationID());
                    out.println("<br />Reply to:   " + replyMessage.getJMSReplyTo());
                    out.println("<br />Contents:   " + replyMessage.getText());


                    //retrieve values from the xmlString in the reply
                    //stores the values in the database
                    XMLParser parse = new XMLParser(replyMessage.getText());
                    id = parse.getId();
                    System.out.println("summon: " + replyMessage.getText());
                    found = true;
                } else {
                    out.println("Invalid message detected");
                    out.println("\tType:       " + reply.getClass().getName());
                    out.println("\tTime:       " + System.currentTimeMillis() + " ms");
                    out.println("\tMessage ID: " + reply.getJMSMessageID());
                    out.println("\tCorrel. ID: " + reply.getJMSCorrelationID());
                    out.println("\tReply to:   " + reply.getJMSReplyTo());

                    reply.setJMSCorrelationID(reply.getJMSMessageID());

                }

                requestSent = false;

            }


            connection.close();

            if (found) {
                found = false;
                response.sendRedirect("result.jsp?id=" + id);
            }


        } catch (JMSException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.close();
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

    /*-----------------------------------------------------------------------
     * usage
     *----------------------------------------------------------------------*/
    private void usage() {
        System.err.println("\nUsage: java tibjmsMsgProducer [options] [ssl options]");
        System.err.println("                                <message-text-1>");
        System.err.println("                                [<message-text-2>] ...");
        System.err.println("\n");
        System.err.println("   where options are:");
        System.err.println("");
        System.err.println("   -server   <server URL>  - EMS server URL, default is local server");
        System.err.println("   -user     <user name>   - user name, default is null");
        System.err.println("   -password <password>    - password, default is null");
        System.err.println("   -topic    <topic-name>  - topic name, default is \"topic.sample\"");
        System.err.println("   -queue    <queue-name>  - queue name, no default");
        System.err.println("   -help-ssl                 - help on ssl parameters");
        System.exit(0);
    }

    /*-----------------------------------------------------------------------
     * parseArgs
     *----------------------------------------------------------------------*/
    void parseArgs(String[] args) {
        int i = 0;

        while (i < args.length) {
            if (args[i].compareTo("-server") == 0) {
                if ((i + 1) >= args.length) {
                    usage();
                }
                serverUrl = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-topic") == 0) {
                if ((i + 1) >= args.length) {
                    usage();
                }
                name = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-queue") == 0) {
                if ((i + 1) >= args.length) {
                    usage();
                }
                name = args[i + 1];
                i += 2;
                useTopic = false;
            } else if (args[i].compareTo("-user") == 0) {
                if ((i + 1) >= args.length) {
                    usage();
                }
                userName = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-password") == 0) {
                if ((i + 1) >= args.length) {
                    usage();
                }
                password = args[i + 1];
                i += 2;
            } else if (args[i].compareTo("-help") == 0) {
                usage();
            } else if (args[i].compareTo("-help-ssl") == 0) {
                tibjmsUtilities.sslUsage();
            } else if (args[i].startsWith("-ssl")) {
                i += 2;
            } else {
                data.addElement(args[i]);
                i++;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
