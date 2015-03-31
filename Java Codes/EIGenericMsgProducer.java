/*
 * =================================================================
 * Copyright (c) 2001-2003 TIBCO Software Inc.
 * All rights reserved.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * $RCSfile: tibjmsMsgProducer.java,v $
 * $Revision: 1.9 $
 * $Date: 2004/02/06 00:05:02 $
 * =================================================================
 */

/*
 * This is a simple sample of a basic tibjmsMsgProducer.
 *
 * This sample publishes specified message(s) on a specified
 * destination and quits.
 *
 * Notice that the specified destination should exist in your configuration
 * or your topics/queues configuration file should allow
 * creation of the specified topic or queue. Sample configuration supplied with
 * the TIBCO Enterprise Message Service distribution allows creation of any
 * destination.
 *
 * If this sample is used to publish messages into
 * tibjmsMsgConsumer sample, the tibjmsMsgConsumer
 * sample must be started first.
 *
 * If -topic is not specified this sample will use a topic named
 * "topic.sample".
 *
 * Usage:  java tibjmsMsgProducer  [options]
 *                               <message-text1>
 *                               ...
 *                               <message-textN>
 *
 *  where options are:
 *
 *   -server    <server-url>  Server URL.
 *                            If not specified this sample assumes a
 *                            serverUrl of "tcp://localhost:7222"
 *   -user      <user-name>   User name. Default is null.
 *   -password  <password>    User password. Default is null.
 *   -topic     <topic-name>  Topic name. Default value is "topic.sample"
 *   -queue     <queue-name>  Queue name. No default
 *
 */

import javax.jms.*;
import java.util.*;
import java.io.*;

public class EIGenericMsgProducer {
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
	String ipAddress = "localhost";

    public EIGenericMsgProducer(String[] args) {
        parseArgs(args);
		
		try {
			File file = new File("config.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration enuKeys = properties.keys();
			ipAddress = properties.getProperty("ipaddress");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> argsArrayList = new ArrayList<String>(Arrays.asList(args));
		argsArrayList.add("-server");
		argsArrayList.add(ipAddress);
		String[] newParam = new String[argsArrayList.size()];
		newParam = argsArrayList.toArray(newParam);


        try {
            tibjmsUtilities.initSSLParams(serverUrl, newParam);
        }
        catch (JMSSecurityException e) {
            System.err.println("JMSSecurityException: " + e.getMessage() + ", provider=" + e.getErrorCode());
            e.printStackTrace();
            System.exit(0);
        }

        /* print parameters */
        System.err.println("\n------------------------------------------------------------------------");
        System.err.println("EIGenericMsgProducer");
        System.err.println("------------------------------------------------------------------------");
        System.err.println("Server....................... " + ((serverUrl != null) ? serverUrl : ipAddress));
        System.err.println("User......................... " + ((userName != null) ? userName : "(null)"));
        System.err.println("Destination.................. " + name);
        System.err.println("------------------------------------------------------------------------\n");

        prepareMessage(data);

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
                data.addElement(args[i]);
                i++;
            }
        }
    }

    /*-----------------------------------------------------------------------
    * prepareMessage - This is the method where you could change the code
    * to prepare the required message for your message consumers
    *----------------------------------------------------------------------*/
    public void prepareMessage(Vector v) {
        StringBuffer finalMessage = new StringBuffer();
        int i;

        if (data.size() == 0) {
            System.err.println("***Error: You must specify the message text\n");
            usage();
        } else {
            /* Preparing the message for transmission - appending all the text */
            finalMessage.append((String)data.elementAt(0));
            for (i = 1; i < data.size(); i++) {
                finalMessage.append(' ');
                finalMessage.append((String)data.elementAt(i));
            }
        }
        transmitMessage(finalMessage.toString());
    }

    public void transmitMessage(String m) {
        try {
            TextMessage msg;

            System.err.println("Publishing to destination '" + name + "'\n");

            ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);
            connection = factory.createConnection(userName, password);
            /* create the session */
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            /* create the destination */
            if (useTopic)
                destination = session.createTopic(name);
            else
                destination = session.createQueue(name);

            /* create the producer */
            msgProducer = session.createProducer(null);

            /* create text message */
            msg = session.createTextMessage();

            /* set message text */
            msg.setText(m);

            /* publish message */
            msgProducer.send(destination, msg);
            System.out.println("Message sent: " + msg.getText());
            /* close the connection */
            //connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    /*-----------------------------------------------------------------------
    * main
    *----------------------------------------------------------------------*/
    public static void main(String[] args) {
        EIGenericMsgProducer t = new EIGenericMsgProducer(args);
    }

}
