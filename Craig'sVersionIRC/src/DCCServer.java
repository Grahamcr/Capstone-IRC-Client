import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

/**********************************************************
 * Write a description of class DCCServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 *************************************************************/
public class DCCServer
{
    /**Socket to listen for requests coming in**/
    ServerSocket listenSocket;

    /**Connection Socket used to send data to the client and recive data back**/
    Socket connectionSocket;

    /**Get data in from the client**/
    BufferedReader inFromClient;

    /**Send Data out to the client**/
    DataOutputStream outToClient;

    /**Ip Address of the client**/
    String clientIP;

    /**Port used to communicate to the Client**/
    int port;

    /**Name of the file that will be received from the client**/
    String fileName, fileToSave;

    /**The length of the file that weill be received**/
    int fileLength;

    /**Array to read file data into - declare instance to be used in Timer Class**/
    byte[] toSave;

    boolean again;

    int byteCounter;
    /*********************************************************
     * Constructor for objects of class DCCServer
     *********************************************************/
    public DCCServer(String pIP, int pPort)
    {
        port = pPort;
        //clientIP = pIP;
        start();
    }

    /***********************************************************
     * Start Things going
     ***********************************************************/
    public void start() {

        try {

            listenSocket = new ServerSocket(port);            
            System.out.println("File Server attempting to bind to port 7000");
            while(listenSocket == null) {
                //if(listenSocket != null) {
                listenSocket = new ServerSocket(port);
                //}
            }
        } catch (IOException io) {
            System.out.println("Fatal Error Binding To Port");
        }

        //Have the server keep accepting connection requests from client
        while(true) {
            System.out.println("Ready and waiting!...");
            connectionSocket = null;
            try {
                if(listenSocket != null) {
                    //Setup the ability to talk back and forth with the client
                    connectionSocket = listenSocket.accept();
                    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    //Read in what the client sent to the server
                    String clientMessage = inFromClient.readLine();
                    System.out.println("Received From Client: " + clientMessage);

                    //Figure out what type of message this is and what to do with it
                    trafficDirector(clientMessage);
                }
                else {
                    start();
                }
            } catch (IOException io2) {
                System.out.println("Fatal Error Accepting Connection Request");
            }

        }

    }

    /*********************************************************
     * Traffic Director - received something from the client,
     * figure out what to do with it next
     **********************************************************/
    public void trafficDirector(String str) {
        if(str.startsWith("DCC SEND")) {
            breakHeader(str);
            //The Client wants to send a file, ask the user if
            //that is alright with them and take approaite action
            if(checkAcceptance()) {
                sendFirstAck();
            }

        }
    }

    /*********************************************************
     * Split the header doun into it's individual parts
     *********************************************************/
    public void breakHeader(String str) {

        String[] results = str.split(" ");
        //Ip Address of the client
        clientIP = results[2].trim();
        //Port used to communicate to the Client
        port = Integer.parseInt(results[3].trim());
        //Name of the file that will be received from the client
        fileName = results[4].trim();
        //The length of the file that weill be received
        fileLength = Integer.parseInt(results[5].trim());

    }

    /*********************************************************
     * THe Client would like to send a file, ask the user
     * if they are up for that
     **********************************************************/
    public boolean checkAcceptance() {
        boolean toReturn = false;

        //Text to display in the pop-up
        String displayText = "Do You accept the request to be sent " +
            "the file: " + fileName + "?";

        //create pop-up and get user's response
        JOptionPane pane = new JOptionPane(displayText);
        Object[] options = new String[] { "Accept", "Decline" };
        pane.setOptions(options);
        JDialog dialog = pane.createDialog(new JFrame(), "Dilaog");
        dialog.setVisible(true);
        Object obj = pane.getValue(); 
        int result = -1;
        for (int k = 0; k < options.length; k++)
            if (options[k].equals(obj))
                result = k;

        //User Accepts
        if(result == 0) {
            String input = null;
            while(input == null || input.equals("")) {
                input = JOptionPane.showInputDialog(null, "Please, Enter Path with File Name" + "\n" +
                    " Where You Like This File To Be Saved");
                fileToSave = input.trim();
            }

            System.out.println("You accepted the file transfer, request sent to client");
            toReturn = true;
        }
        //User Declines
        else {
            System.out.println("You have choosen the decline the file transfer request");
        }

        return toReturn;
    }

    /*********************************************************
     * Acknowlage the client's last transmittion 
     **********************************************************/
    public void sendFirstAck() {
        String hostname = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            byte[] ipAddr = addr.getAddress();

            // Get hostname
            hostname = addr.getHostAddress();
        } catch (UnknownHostException e) {
        }
        String ack = "DCC FILE " + hostname + " " + fileName;
        try {
            outToClient.writeBytes(ack + '\n');
        } catch(IOException e) {
            System.out.println("Error sending first ack: " + e);
        }

        //Get Ready for incoming data
        saveFile();
    }

    /*********************************************************
     * Take data packets coming in from the client and 
     * use them to make a byte array that can be used to
     * create a file onece all data is received
     **********************************************************/
    public void saveFile() {

        toSave = new byte[fileLength+1024];

        int filePointer = 0;

        byteCounter = 0;

        char[] tempBuf = new char[fileLength+1024];

        try {
            //try to avoid a race situation
            Thread.sleep(4000);
        }catch(Exception e) {
        }

        int numberOfMillisecondsInTheFuture = 5000; // 5 sec
        Date timeToRun = new Date(System.currentTimeMillis()+numberOfMillisecondsInTheFuture);
        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("timeout occured - assuming end of transfer");
                    again = false;
                    sendAck(byteCounter);
                    //filePointer = filePointer+ byteCounter;
                    writeFileToMem(toSave);
                    timer.cancel();
                }
            }, timeToRun);
            try{
                inFromClient.read(tempBuf, 0, fileLength);
                timer.cancel();
                sendAck(fileLength);
            }catch(Exception e) {
                System.out.println("Error Reading in from Client: " + e);
            }
            for(int i = 0; i < fileLength; i++) {
                toSave[i] = (byte)tempBuf[i];
            }
        writeFileToMem(toSave);
    }

    /**********************************************************
     * Tell the client that the server received a packet of 
     * the size in argument
     **********************************************************/
    public void sendAck(int size) {
        String ack = "" + size;
        try {
            outToClient.writeBytes(ack + '\n');
        } catch(IOException e) {
            System.out.println("Error sending ack: " + e);
        }
    }

    /*********************************************************
     * Create a file from the byte array full of data
     * received from the client
     **********************************************************/
    public void writeFileToMem(byte[] toSave) {
        try{
            System.out.println("Saving The File");
            byte[] tmp = new byte[fileLength];
            for(int i = 0; i < fileLength; i++) {
                tmp[i] = (byte) toSave[i];
            }
            FileOutputStream out = new FileOutputStream(new File(fileToSave));
            out.write(tmp, 0, fileLength);
            out.close();
        }catch(IOException e) {
            System.out.println("Error writting file to filesytem " + e);
        }
        
    }

    /**********************************************************
     * Main
     **********************************************************/
    public static void main(String[] args) {
        DCCServer d = new DCCServer(args[0].trim(), Integer.parseInt(args[1].trim()));
       
    }

}
