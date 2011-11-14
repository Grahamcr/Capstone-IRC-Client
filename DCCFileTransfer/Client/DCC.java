import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import java.io.FileInputStream;
/*************************************************************************
 * DCC File Transfer Class
 * 
 * @author Craig Graham 
 * @version Fall 2011
 **************************************************************************/
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

    /**********************************************************************
     * Constructor for objects of class DCC
     **********************************************************************/
    public DCC()
    {

    }

    /**********************************************************************
     * Main
     **********************************************************************/
    public static void main(String[] args) {
        DCC dc = new DCC();
        dc.start();

    }

    /**********************************************************************
     * Start Things Going
     ***********************************************************************/
    public void start() {

        try{

            String input = null;
            while(input == null || input.equals("")) {
                input = JOptionPane.showInputDialog(null, "Please, Enter the IP Address");
            }
            address = input.trim();
            String input2 = null;
            while(input2 == null || input2.equals("")) {
                input2 = JOptionPane.showInputDialog(null, "Please, Enter the port");
            }
            port = Integer.parseInt(input2.trim());
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

            getFile(fileName.trim());

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
    }

    /*************************************************************
     * Make first header String 
     *************************************************************/
    public String createConnectionHeaderString() {
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
                    BufferedReader input =  new BufferedReader(new FileReader(fileName));

                    String line = null; 
                    int count = 0;

                    //Get every line, one line at a time and create a datastruc from it
                    while(( line = input.readLine()) != null){
                        String toAdd = line.trim();
                        byte[] bytes = toAdd.getBytes();
                        for(int i = 0; i < bytes.length; i++) {
                            fileToSend[bytesRead + i] = bytes[i];
                            bytesRead++;
                        }
                    }

                    input.close();
                    System.out.println("Succesfully Read-in File");
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

