import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import javax.swing.*;
/*****************************************************************************
 * The TFTP Client to work with our server. Each side is to have an instance of both
 * 
 * @author Craig Graham
 * @version Fall 2011
 ****************************************************************************/
public class TFTPClient
{
    /**Set to false if the timer for receiveing an ACK runs out**/
    boolean haveTime, last;

    /**File to send**/
    byte[] fileToSend;

    /**The port that the server is using**/
    int serverPort, returnPort;

    /**The Ip Address of the sever**/
    InetAddress serverIP, returnIP;  

    /**Datagram socket to act as the TFTP connection for the server**/
    DatagramSocket clientSocket;

    /**Used for error recovery purposes**/
    String type, localFileName, remoteFileName;

    /**Byte Array to hold an incoming file**/
    byte[] fileBuilder;

    /**pointer for the fileBuilder array**/
    int filePointer;

    /**keep track if we have move data to recieve from server yet or not**/
    boolean moreToSave;

    /**How long the file is the server is going to send**/
    int inFileLength;

    /**String for the type of transfer requsted**/
    String mode;

    /**Bytes to represent block numbers for sending data**/
    byte one, two;

    /**Bytes to represent last received ack**/
    byte ackOne, ackTwo;
    /*****************************************************************************
     * Constructor for objects of class TFTPClient
     ****************************************************************************/
    public TFTPClient()
    {
        filePointer = 4;
        fileBuilder = new byte[33554432];
        haveTime = true;
        last = false;
        serverPort = 69;
        moreToSave = true;
        String input = "";
        fileToSend = new byte[33554432];
        one = (byte)0;
        two = (byte)0;
        while(input == null || input.equals("")) {
            input = JOptionPane.showInputDialog(null, "Please, Enter the IP Address Of The Server");
        }
        try {
            serverIP = InetAddress.getByName(input.trim());
            //clientSocket = new DatagramSocket(serverPort, serverIP);
            clientSocket = new DatagramSocket();
        }catch(Exception e) {
            System.out.println("Error Opening Socket " + e);
        }

    }

    /*****************************************************************************
     * Main Method
     ****************************************************************************/
    public static void main(String[] args) {
        TFTPClient tftpClient = new TFTPClient();
    }

    /***************************************************************************
     * 
     ***************************************************************************/
    public void sendRequest(String typep, boolean time, boolean error, String modep) {
        try {
            type = typep;
            haveTime = time;
            mode = modep;
            remoteFileName = remoteFileName + "    ";
            byte[] fileArray = remoteFileName.getBytes();
            remoteFileName = remoteFileName.trim();
            //send and wait for ack - if no ack in 30 sec send again
            byte[] out = new byte[512];
            if(type.equals("write")) {
                
                out[0] = (byte)0;
                out[1] = (byte)2;
            }
            else if(type.equals("read")) {

                out[0] = (byte)0;
                out[1] = (byte)1;
            }

            
            int i = 0;
            for(i = 0; i < fileArray.length; i++) {
                out[i+2] = fileArray[i];
            }
            //out[i++] = (byte)1;
            i = i+2;
            //Seperator between fileName and mode in the header
            out[i] = (byte)0;

            String tmpMode = mode;
            byte[] modeArray = tmpMode.getBytes();
            int q = 0;
            i++;
            while(q < modeArray.length) {
                out[i+2] = modeArray[q];
                q++;
                i++;
            }
            i = i+3;
            out[i] = (byte)0;
            System.out.println("Sending the Request " + new String(out));
            //Create a datagram packet with the header and data
            DatagramPacket sendPacket = new DatagramPacket(out, 512, 
                    serverIP, serverPort);

            try {
                //Send it                                                
                clientSocket.send(sendPacket);
            }catch(IOException e) {
                System.out.println("Error Sending Packet: " + e);
            }
            System.out.println("Sent a " + type + " request to the sever");
            //Wait and see if the client ack's what server sent - give client 15 seconds to do so
            boolean again = true;

            //request received by the server - do whats next
            if(typep.equals("write")) {

                if(!last) {
                    int numberOfMillisecondsInTheFuture = 10000; // 10 sec
                    Date timeToRun = new Date(System.currentTimeMillis()+numberOfMillisecondsInTheFuture);
                    final Timer timer = new Timer();

                    timer.schedule(new TimerTask() {
                            public void run() {
                                System.out.println("timeout occured");
                                haveTime = false;
                                sendRequest(type, true, true, "netascii"); 
                                timer.cancel();
                            }
                        }, timeToRun);
                    int count = 0;
                    while(again && haveTime) {
                        //another while loop to check timer and ack number??
                        byte[] receiveData = new byte [1024];

                        DatagramPacket returnPacket = new DatagramPacket(receiveData, receiveData.length);

                        //read in data sent from the client
                        clientSocket.receive(returnPacket);

                        String clientMessage = new String(returnPacket.getData());
                        //                System.out.println("Received In Send Packet: " + clientMessage);

                        byte[] temp = returnPacket.getData();
                        if(temp[1] == 4) {
                            byte ackOne = temp[2];
                            byte ackTwo = temp[3];
                            System.out.println("Received Client Ack for: " + ackOne + ackTwo);
                            if(ackTwo == (byte)0 && ackOne == (byte)0) {
                                again = false;
                            }
                        }
                        // count++;
                    }
                    haveTime = true;

                    timer.cancel();
                    writeFileToServer();
                }
            }
            else if(typep.equals("read")) {
                getFileFromServer();
            }
            //            System.out.println("Got The Right ACK From The Client");
        }catch(IOException e) {
            System.out.println("Error sending data packet: " + e);
        }   

    }

    /***************************************************************************
     * Write A File To The Server
     ****************************************************************************/
    public void getFileFromServer() {
        try{
            while(moreToSave) {
                //byte array to receive data into
                byte[] receiveData = new byte [1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                //read in data sent from the client
                clientSocket.receive(receivePacket);

                //Make a string from the data received
                String serverMessage = new String(receivePacket.getData());
                System.out.println("Received: " + serverMessage);
                //Get the client's IP and port number
                returnIP = receivePacket.getAddress();
                returnPort = receivePacket.getPort();

                byte[] data = receivePacket.getData();
                saveData(data);
                byte first = data[2];
                byte second = data[3];
                sendAck(first, second);
            }
        }catch(Exception e) {
            sendErrorMessage(00, "Error Sending Ack");
        }
    }

    /***********************************************************************
     * Send an ack message to responed to a received WRQ (write) or DATA 
     * header
     ***********************************************************************/
    public void sendAck(byte first, byte second) {

        byte[] sendData = new byte[512];
        sendData[0] = (byte)0;
        sendData[1] = (byte)4;
        sendData[2] = (byte)first;
        sendData[3] = (byte)second;
        //Create packet to send back to the client
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
                serverIP, serverPort);

        try {
            //Send it                                                
            clientSocket.send(sendPacket);
        }catch(IOException e) {
            System.out.println("Error Sending Packet: " + e);
        }
        System.out.println("ACK sent, blockNumber: " + first + second);
    }

    /**********************************************************************
     * Method to save data as it comes in from the client and then use
     * the captured data to create a file
     **********************************************************************/
    public void saveData(byte[] toAdd) {
        try {
            if(toAdd[511] == (byte)0) {

                try{
                    int count = 0;
                    //while(toAdd[count] != (byte)0) {
                    while(count < 511) {    
                        fileBuilder[filePointer+count] = toAdd[count];
                        count++;
                    }
                    filePointer = filePointer+count;
                    FileOutputStream out = new FileOutputStream(new File(localFileName));
                    out.write(fileBuilder, 0, filePointer);
                    out.close();
                    for(int f = 0; f < fileBuilder.length; f++) {
                        fileBuilder[f] = (byte)0;
                    }
                     one = (byte)0;
                     two = (byte)0;
                    haveTime = true;
                    moreToSave = false;
                    System.out.println(localFileName + " Successfully Written To client!");
                }catch (IOException e3) {
                    System.out.println("Error writting file to server: " + e3);
                    sendErrorMessage(0, "Not Able to Write To Server");
                }
            }
            else {
                for(int q = 0; q < 512; q++) {
                    fileBuilder[filePointer+q] = toAdd[q];
                }
            }
            filePointer = filePointer+512;
        } catch(NullPointerException e) {
            try{
                FileOutputStream out = new FileOutputStream(new File(localFileName));
                out.write(fileBuilder, 0, filePointer+511);
                out.close();
                for(int f = 0; f < fileBuilder.length; f++) {
                    fileBuilder[f] = (byte)0;
                }
                  one = (byte)0;
                     two = (byte)0;
                haveTime = true;
                System.out.println(localFileName + " Successfully Written To Sever!");
            }catch (IOException e2) {
                System.out.println("Error writting file to server: " + e2);
                sendErrorMessage(0, "Not Able to Write To Server");
            }
        }
    }   

    /************************************************************************
     * Send a error message 
     ************************************************************************/
    public void sendErrorMessage(int code, String message) {
        byte[] m = message.getBytes();
        byte[] out = new byte[m.length + 5]; 

        out[0] = (byte)0;
        out[1] = (byte)5;
        out[2] = (byte)0;
        out[3] = (byte)code;

        for(int g = 4; g < m.length; g++) {
            out[g] = m[g-4];
        }

        //Create a datagram packet with the header and data
        DatagramPacket sendPacket = new DatagramPacket(out, out.length, 
                serverIP, serverPort);

        try {
            //Send it                                                
            clientSocket.send(sendPacket);
        }catch(IOException e) {
            System.out.println("Error Sending Packet: " + e);
        }

    }

    /***************************************************************************
     * Get A File From The Server
     ****************************************************************************/
    public void writeFileToServer() {
        //get the file
        getFile();

        //Start sending data
        sendData();
    }

    /***************************************************************************
     * Get Request Coming in from the GUI
     ****************************************************************************/
    public void getRequest(String local, String remote, String host) {
        localFileName = local;
        remoteFileName = remote;
        sendRequest("read", true, false, "netascii");
    }

    /***************************************************************************
     * Put Request coming in from the GUI 
     ****************************************************************************/
    public void putRequest(String local, String remote, String host) {
        localFileName = local;
        remoteFileName = remote;
        moreToSave = true;
        sendRequest("write", true, false, "netascii");
    }

    /************************************************************************
     * Get the bytes that make up the file requested
     **************************************************************************/
    public void getFile() {

        //How many bytes have been read into the buffer
        int bytesRead = 0;

        //byte counter - keep track of packet size as we build it
        inFileLength = 0;

        try {
            String absFilePath = new File(localFileName).getAbsolutePath();
            File file =  new File(localFileName);
            inFileLength = (int)file.length();
            System.out.println("File Is Located At: " + absFilePath);
            if (!file.exists()) {
                System.out.println("Error- File Does Not Exist");
                sendErrorMessage(1, "File Not Found");
            }

            if (!file.canWrite()) {
                System.out.println("Access Violation");
                sendErrorMessage(2, "Access Violation");
            }

            if(mode.equalsIgnoreCase("netascii")) {
                netascii();
            }
            FileInputStream requestedfile = new FileInputStream(absFilePath);

            while((bytesRead = requestedfile.read(fileToSend)) != -1) {
            }
            System.out.println("Successfully Opened " + localFileName + " To Be Sent");
        }catch (Exception e) {
            System.out.println("Error opening file: " + e);
        }

    }

    /************************************************************************
     * Format file so that it meets netascii mode requirements
     ************************************************************************/
    public void netascii() {
        StringBuilder contents = new StringBuilder();
        try {
            BufferedReader input =  new BufferedReader(new FileReader(localFileName));
            try {
                String line = null; //not declared within while loop
                while (( line = input.readLine()) != null){
                    if(line.contains("\r\n")) {
                        //do nothing
                    }
                    else if(line.contains("\n")) {
                        line.replace("\n", "\r\n");
                    }
                    else if(line.contains("\r")) {
                        line.replace("\n", "\r\n");
                    }
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }

            }
            finally {
                input.close();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        String fileContents = contents.toString();
        FileOutputStream fout;       

        try {
            fout = new FileOutputStream ((localFileName));
            new PrintStream(fout).println (fileContents);
            fout.close();       
        }
        // Catches any error conditions
        catch (IOException e) {
            System.err.println ("Unable to write to file");
            sendErrorMessage(4, "Illegal TFTP Operation");
        }

    }

    /**************************************************************************
     * Method for dealing with the error messages we could receive
     **************************************************************************/
    public void sendData() {

        last = false;

        //keep track of where we are currently in the file to be sent
        int byteCounter = 0;

        //keep track of how many packets we have sent so far
        int index = 0;

        int packetAmount = inFileLength / 512;

        try {

            while(index < packetAmount) {
                //not the last packet, so we know that it should be 512 bytes

                //buffer to hold file before being sent
                byte[] buffer = new byte[513];

                //get the next byte of the file
                for(int i = byteCounter; i < byteCounter+512; i++) {
                    buffer[i - byteCounter] = fileToSend[i];
                }
                byteCounter = byteCounter + 512;
                index++;
                sendPacket(512, buffer, true, false);
            }
            //send the last packet
            last = true;
            int remainingBytes = inFileLength - byteCounter;
            byte[] buff = new byte[remainingBytes];
            for(int g = byteCounter; g < byteCounter+remainingBytes; g++) {
                buff[g - byteCounter] = fileToSend[g];
            }
            sendPacket(remainingBytes, buff, true, false);
             one = (byte)0;
             two = (byte)0;
            haveTime = true;
            System.out.println(localFileName + " Successfully Written To Client!");
        } catch (Exception e) {
            System.out.println("Error sending file " + e);
            sendErrorMessage(0, "Transfer Failed");
        }

    }

    /***********************************************************************
     * Send each data packet of the file to the client - keep watch for a 
     * timeout that could occur.   
     ***********************************************************************/
    public void sendPacket(final int packetSize, final byte[] buffer, boolean time, boolean error) {
        try {
            haveTime = time;
            //send and wait for ack - if no ack in 30 sec send again
            byte[] out = new byte[packetSize+4];
            out[0] = (byte)0;
            out[1] = (byte)3;
            if(!error) {
                generateBlockNumbers();
            }
            out[2] = (byte)two;
            out[3] = (byte)one;
            for(int g = 4; g < packetSize; g++) {
                out[g] = buffer[g-4];
            }

            //Create a datagram packet with the header and data
            DatagramPacket sendPacket = new DatagramPacket(out, out.length, 
                    serverIP, serverPort);

            try {
                //Send it                                                
                clientSocket.send(sendPacket);
            }catch(IOException e) {
                System.out.println("Error Sending Packet: " + e);
            }
            System.out.println("Sent Data Packet " + two + one + " To The Client");
            //Wait and see if the client ack's what server sent - give client 15 seconds to do so
            boolean again = true;

            if(!last) {
                int numberOfMillisecondsInTheFuture = 10000; // 10 sec
                Date timeToRun = new Date(System.currentTimeMillis()+numberOfMillisecondsInTheFuture);
                final Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                        public void run() {
                            System.out.println("timeout occured");
                            haveTime = false;
                            sendPacket(packetSize, buffer, true, true); 
                            timer.cancel();
                        }
                    }, timeToRun);
                int count = 0;
                while(again && haveTime) {
                    //another while loop to check timer and ack number??
                    byte[] receiveData = new byte [1024];

                    DatagramPacket returnPacket = new DatagramPacket(receiveData, receiveData.length);

                    //read in data sent from the client
                    clientSocket.receive(returnPacket);

                    String clientMessage = new String(returnPacket.getData());
                    //                System.out.println("Received In Send Packet: " + clientMessage);

                    byte[] temp = returnPacket.getData();
                    if(temp[1] == 4) {
                        ackOne = temp[2];
                        ackTwo = temp[3];
                        System.out.println("Received Client Ack for: " + ackOne + ackTwo);
                        if(ackTwo == one && ackOne == two) {
                            again = false;
                        }
                    }
                    // count++;
                }
                haveTime = true;

                timer.cancel();
            }
            //            System.out.println("Got The Right ACK From The Client");
        }catch(IOException e) {
            System.out.println("Error sending data packet: " + e);
        }

    }

    /*************************************************************************
     * Genterate the next block number 
     *************************************************************************/
    public void generateBlockNumbers() {
        byte add = (byte)1;
        if(one == (byte)99) {
            one = (byte)0;
            two = (byte)(two+add);
        }
        else {
            one = (byte)(one+add);
        }

    }

}

