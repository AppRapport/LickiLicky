package ei.g2t6.servlet;

/*
 * This is a simple sample of a basic message producer that sends the contents of XML documents as string
 * Each XML document is sent in a separate JMS message
 * The XML documents are assumed to be stored in directory C:\EI\Labs\OdMs\corporateOrders
 * This program makes use of EIGenericMsgProducer which is able to handle both
 * JMS queue and topic but _not_ able to allow multiple durable topic
 * subscriber. 
 */

import java.util.*;
import java.io.*;

public class EIXMLMsgProducer {
    /*-----------------------------------------------------------------------
     * Parameters
     *----------------------------------------------------------------------*/

    String [] program_args;
	
	// Setter method to set the program_args variable
	public void setArgs (String [] args) {
		program_args = args;
	}
	
	/* 
	  * A method to initiate the activity to search thru the directory and 
	  * process all the files within
	  */
	public void processAllFiles() {
	   visitAllFiles(new File("C:\\EI\\Labs\\OdMS\\corporateOrders.xml"));
	}

	/*
	 * This method iterates through all files the directory
	 */
	public void visitAllFiles(File dir) {
	    if (dir.isDirectory()) {
	      String[] children = dir.list();
	      if (children.length == 0) {
	        System.out.println("There is no order to process.");
	      }
	      for (int i = 0; i < children.length; i++) {
	        visitAllFiles(new File(dir, children[i]));
	      }
		} else {
	      readXML(dir);
	    }
	}

    /*
     * This method reads an XML document and put the content into Vector data
     */
    public void readXML(File filename) {
        
        BufferedReader bufferedReader = null;
        try {
            //Construct the BufferedReader object
            bufferedReader = new BufferedReader(new FileReader(filename));
            String line = null;
            Vector data = new Vector();
            while ((line = bufferedReader.readLine()) != null) {
                //Add the line from XML file to the message to be sent as JMS msg
                data.addElement(line);
            }
          prepareAndSendMessage(data);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}		
	
    /*-----------------------------------------------------------------------
    * prepareAndSendMessage - This is the method where you could change the code
    * to prepare the required message for your message consumers
    *----------------------------------------------------------------------*/
    public void prepareAndSendMessage(Vector contents) {
        StringBuffer finalMessage = new StringBuffer();
        int i;

        if (contents.size() == 0) {
            System.err.println("***Error: Order file is empty\n");
        } else {
            /* Preparing the message for transmission - appending all the text */
            finalMessage.append((String)contents.elementAt(0));
            for (i = 1; i < contents.size(); i++) {
                finalMessage.append(' ');
                finalMessage.append((String)contents.elementAt(i));
            }
        }
        // Copying the args from the command line the args to be submitted to EIGenericMsgProducer
        int argSize = program_args.length;
        String myargs[] = new String[argSize+1];
        System.arraycopy(program_args, 0, myargs, 0, argSize);
        myargs[argSize] = finalMessage.toString();

        // Instantiate a generic message producer to send a message 
        EIGenericMsgProducer msgProducer = new EIGenericMsgProducer(myargs);
    }

    /*-----------------------------------------------------------------------
    * main
    *----------------------------------------------------------------------*/
    public static void main(String[] args) {
      EIXMLMsgProducer producer = new EIXMLMsgProducer();
      producer.setArgs(args);
      producer.processAllFiles();
    }

}
