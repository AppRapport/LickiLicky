import javax.jms.*;
import javax.naming.*;

public class SAMSubscriber
{
    String      serverUrl       = null;
    String      userName        = null;
    String      password        = null;

    String      topicName       = "topic.sample";
    String      clientID        = null;
    String      durableName     = "subscriber";

    boolean     unsubscribe     = false;

    public SAMSubscriber(String[] args) {

        parseArgs(args);

        try {
            tibjmsUtilities.initSSLParams(serverUrl,args);
        }
        catch (JMSSecurityException e)
        {
            System.err.println("JMSSecurityException: "+e.getMessage()+", provider="+e.getErrorCode());
            e.printStackTrace();
            System.exit(0);
        }

        if (!unsubscribe && (topicName == null)) {
            System.err.println("Error: must specify topic name");
            usage();
        }

        if (durableName == null) {
            System.err.println("Error: must specify durable subscriber name");
            usage();
        }

		
				       /* print parameters */
        System.err.println("\n------------------------------------------------------------------------");
        System.err.println("SAM");
        System.err.println("------------------------------------------------------------------------");
        System.err.println("Server....................... "+((serverUrl != null)?serverUrl:"localhost"));
        
        System.err.println("Using Durable Name........................ "+durableName);
        System.err.println("------------------------------------------------------------------------\n");
		

        try
        {
            TopicConnectionFactory factory = new com.tibco.tibjms.TibjmsTopicConnectionFactory(serverUrl);

            TopicConnection connection = factory.createTopicConnection(userName,password);

            /* if clientID is specified we must set it right here */
            if (clientID != null)
                connection.setClientID(clientID);

            TopicSession session = connection.createTopicSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);

            if (unsubscribe) {
                System.err.println("Unsubscribing durable subscriber "+durableName);
                session.unsubscribe(durableName);
                System.err.println("Successfully unsubscribed "+durableName);
                connection.close();
                return;
            }

            System.err.println("Subscribing to topic: "+topicName);

            /*
             * Use createTopic() to enable subscriptions to dynamic topics.
             */
            javax.jms.Topic topic = session.createTopic(topicName);

            TopicSubscriber subscriber = session.createDurableSubscriber(topic,durableName);

            connection.start();

            /* read topic messages */
            while(true)
            {
                javax.jms.Message message = subscriber.receive();
                if (message == null)
                    break;

                System.err.println("\nReceived message: "+message);
            }

            connection.close();
        }
        catch(JMSException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String args[])
    {
        SAMSubscriber t = new SAMSubscriber(args);
    }

    void usage()
    {
        System.err.println("\nUsage: java SAMSubscriber [options]");
        System.err.println("");
        System.err.println("   where options are:");
        System.err.println("");
        System.err.println(" -server   <server URL> - EMS server URL, default is local server");
        System.err.println(" -user     <user name>  - user name, default is null");
        System.err.println(" -password <password>   - password, default is null");
        System.err.println(" -topic    <topic-name> - topic name, default is \"topic.sample\"");
        System.err.println(" -clientID <client-id>  - clientID, default is null");
        System.err.println(" -durable  <name>       - durable subscriber name,");
        System.err.println("                          default is \"subscriber\"");
        System.err.println(" -unsubscribe           - unsubscribe and quit");
        System.err.println(" -help-ssl              - help on ssl parameters\n");
        System.exit(0);
    }

    void parseArgs(String[] args)
    {
        int i=0;

        while(i < args.length)
        {
            if (args[i].compareTo("-server")==0)
            {
                if ((i+1) >= args.length) usage();
                serverUrl = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-topic")==0)
            {
                if ((i+1) >= args.length) usage();
                topicName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-user")==0)
            {
                if ((i+1) >= args.length) usage();
                userName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-password")==0)
            {
                if ((i+1) >= args.length) usage();
                password = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-durable")==0)
            {
                if ((i+1) >= args.length) usage();
                durableName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-clientID")==0)
            {
                if ((i+1) >= args.length) usage();
                clientID = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-unsubscribe")==0)
            {
                unsubscribe = true;
                i += 1;
            }
            else
            if (args[i].compareTo("-help")==0)
            {
                usage();
            }
            else
            if (args[i].compareTo("-help-ssl")==0)
            {
                tibjmsUtilities.sslUsage();
            }
            else
            if(args[i].startsWith("-ssl"))
            {
                i += 2;
            }
            else
            {
                System.err.println("Unrecognized parameter: "+args[i]);
                usage();
            }
        }
    }

}


