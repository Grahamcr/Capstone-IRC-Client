import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import java.io.FileInputStream;
/***************************************************************
 * DCC File Transfer Class
 * 
 * @author Craig Graham 
 * @version Fall 2011
 ***************************************************************/
public class DCC
{

    /**Socket to act on behalf of the client**/
    Socket clientSocket;

    /**The address to send file to and the name of the file**/
    String address, fileName;

    /**Port to use for communications**/
    int port;

    /**Caclulated length of the file**/
    int fileLength;

    /**Contents of the file**/
    byte[] fileToSend;

    /**Stream out to server**/
    DataOutputStream outToServer;

    /**Read in from the socket**/
    BufferedReader inFromServer;

    /**IP of the server to send to -- not used right now as we get IP from user input**/
    String destIP;

    /**Read In The File Requested By The User**/
    FileInputStream requestedfile;

    /**********************************************************************
     * Constructor for objects of class DCC
     **********************************************************************/
    public DCC(String pIP, int pPort)
    {
        port = pPort;
        address = pIP;
        start();
    }

    /**********************************************************************
     * Main
     **********************************************************************/
    public static void main(String[] args) {
        DCC dc = new DCC(args[0].trim(), Integer.parseInt(args[1].trim()));

    }

    /**********************************************************************
     * Start Things Going
     ***********************************************************************/
    public void start() {

        try{
            
//             
//             String input = null;
//             while(input == null || input.equals("")) {
//                 input = JOptionPane.showInputDialog(null, "Please, Enter the IP Address");
//             }
//             address = input.trim();
            /**
            String input2 = null;
            while(input2 == null || input2.equals("")) {
                input2 = JOptionPane.showInputDialog(null, "Please, Enter the port");
            }
            port = Integer.parseInt(input2.trim());
            **/
            
            String input3 = null;
            while(input3 == null || input3.equals("")) {
                input3 = JOptionPane.showInputDialog(null, "Please, Enter the filename");
            }
            fileName = input3.trim();

            System.out.println("Trying to set up socket");

            try{
                //Open a Socket
                clientSocket = new Socket(address.trim(), port);
            }catch(Exception e) {
                System.out.println("Error Opening Client Socket: " + e);
            }
            //Send out the socket connection
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Read in from the socket
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // byte[] firstHeaderByte = createConnectionHeaderByte();

            String firstHeaderString = createConnectionHeaderString();

            InitSend(firstHeaderString);

        }catch(Exception e) {
        }

    }

    /*************************************************************
     * Ask the Server side if it is ready for a file by sending
     * the first header
     *************************************************************/
    public void InitSend(String header) {
        boolean again = true;
        try{
            //Send the first header to the server
            outToServer.writeBytes(header + '\n');
        }catch(IOException e) {
            System.out.println("Error Sending Header to Server: " + e);
        }
        System.out.println("Succesfully Sent Header: " + header + " to the server");

        waitForAck();
    }

    /*************************************************************
     * Wait for the acknowlege from the server
     **************************************************************/
    public void waitForAck() {
        System.out.println("Waiting for ACK from the Server");
        String reply = "";
        //Get what the server sent back
        try {
            reply = inFromServer.readLine();
        }catch(IOException e) {
            System.out.println("Error reading in ack from server: " + e);
        }

        System.out.println("Received following ack from server: " + reply);

        String[] results = reply.split(" ");
        if(results[1].trim().equals("FILE") && results[3].trim().equals(fileName)) {
            destIP = results[2].trim();
            getFile(fileName.trim());
            waitForAck(fileLength);
        }

    }

    /************************************************************
     * Send File to the server in 1024 byte packets
     *************************************************************/
    public void sendFile(String serversIPAddress) {
        //This will be the size expected to tbe ack'd by the server
        int packetSizeSent = 1024;

        //Keep Track of how much of the file has been sent so far
        int filePointer = 0;
        while(filePointer+1023 < fileLength) {
            try{
                byte[] tmp = new byte[1024];
                for(int i = filePointer; i < 1023; i++) {
                    tmp[i - filePointer] = fileToSend[i];
                }
                tmp[1023] = (byte)027;
                //Send part of the array from filePointer to filePointer+1024
                outToServer.write(tmp, 0, 1024);
            }catch(IOException e) {
                System.out.println("Error sending data to the server: " + e);
            }
            waitForAck(packetSizeSent);
            filePointer = filePointer + 1023;
        }

        //Send the rest of the file (less then 1024 bytes)
        int leftToSend = fileLength - filePointer;
        try {
            byte[] tmp = new byte[1024];
            for(int i = filePointer; i < leftToSend; i++) {
                tmp[i - filePointer] = fileToSend[i];
            }
            tmp[leftToSend+1] = (byte)027;
            //Send part of the array from filePointer to filePointer+1024
            outToServer.write(tmp, 0, leftToSend+1);

        }catch(IOException e) {
            System.out.println("Error sending data to the server: " + e);
        }
        waitForAck(leftToSend+1);

        System.out.println("File Successfuly Sent To The Server :) ");
    }

    /************************************************************
     * Wait for the Server to acknowlege the client's latest 
     * data transfer
     ***********************************************************/
    public void waitForAck(int amount) {
        System.out.println("Waiting for ACK from the Server");
        String reply = "";
        //Get what the server sent back
        try {
            reply = inFromServer.readLine();
        }catch(IOException e) {
            System.out.println("Error reading in ack from server: " + e);
        }

        System.out.print("Received following ack from server: " + reply);
        if(Integer.parseInt(reply) == amount) {
            System.out.println(" Which is correct!");
        }
        else {
            //Take some action as the wrong ack was recieved
        }

    }

    /*************************************************************
     * Make first header String 
     *************************************************************/
    public String createConnectionHeaderString() {
        File file =  new File(fileName);
        fileLength = (int)file.length();
        String toReturn = ("DCC SEND " + address + " " + port + " " + fileName + " " + fileLength);
        return toReturn;
    }

    /**************************************************************
     * Create the first header that we will use to establish a
     * connection
     **************************************************************/
    public byte[] createConnectionHeaderByte() {
        byte[] toReturn = new byte[1024];
        byte[] byteAddress = address.getBytes();
        //byte bytePort = port.byteValue();
        byte[] byteFileName = fileName.getBytes();
        // byte byteFileSize = fileLength.byteValue();
        String dcc = "DCC";
        byte[] byteDcc = dcc.getBytes();
        int pointer = 0;
        int i = 0;
        for(i = 0; i < byteDcc.length; i++) {
            toReturn[pointer] = byteDcc[i];
            pointer++;
        }
        for(i = 0; i < byteAddress.length; i++) {
            toReturn[pointer] = byteDcc[i];
            pointer++;
        }
        toReturn[pointer] = (byte)port;
        pointer++;
        for(i = 0; i < byteFileName.length; i++) {
            toReturn[pointer] = byteDcc[i];
            pointer++;
        }
        toReturn[pointer] = (byte)fileLength;

        return toReturn;

    }

    /*************************************************************
     * Get File
     **************************************************************/
    public void getFile(String fileName) {

        //How many bytes have been read into the buffer
        int bytesRead = 0;

        try {
            String absFilePath = new File(fileName).getAbsolutePath();
            File file =  new File(fileName);
            fileLength = (int)file.length();
            fileToSend = new byte[fileLength+200];
            System.out.println("File Is Located At: " + absFilePath);
            boolean open = true;

            if (!file.exists()) {
                System.out.println("Error- File Does Not Exist");
                open = false;
                //sendErrorMessage(1, "File Not Found");
            }

            if (!file.canWrite()) {
                System.out.println("Access Violation");
                open = false;
                //sendErrorMessage(2, "Access Violation");
            }

            //File Exists - Open it and read in info
            if(open) {
                try {
                    requestedfile = new FileInputStream(absFilePath);

                    byte[] tempBuf = new byte[fileLength]; 
                        int data = requestedfile.read(tempBuf, 0, fileLength);
                     System.out.println("Succesfully Read-in File");
                      outToServer.write(tempBuf);
                        System.out.println("File Successfuly Sent To The Server :) ");
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
            else {
                System.out.println("Failed to open file");
            }
        }catch (Exception e) {
            System.out.println("Error opening file: " + e);
        }

    }    

}

