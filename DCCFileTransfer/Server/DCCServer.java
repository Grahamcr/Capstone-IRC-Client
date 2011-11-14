import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
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
    
    /*********************************************************
     * Constructor for objects of class DCCServer
     *********************************************************/
    public DCCServer()
    {
    }

    /***********************************************************
     * Start Things going
     ***********************************************************/
    public void start() {

        try {
            listenSocket = new ServerSocket(6789);            
            System.out.println("Attempting to bind to port 80");
            while(listenSocket == null) {
                //create a Socket to listen to port 80 - common port for internet browsers
                //if(listenSocket != null) {
                listenSocket = new ServerSocket(6789);
                //}
            }
        } catch (IOException io) {
            System.out.println("Fatal Error Binding To Port");
        }

        //Have the server keep accepting connection requests from client
        while(true) {
            System.out.println("Ready and waiting!...");
            connectionSocket = null;
            //accept connection request on port 80
            try {
                if(listenSocket != null) {
                    //Setup the ability to talk back and forth with the client
                    connectionSocket = listenSocket.accept();
                    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    //Read in what the client sent to the server
                    String clientMessage = inFromClient.readLine();
                    System.out.println("Received From Client: " + clientMessage);
                }
                else {
                    start();
                }
            } catch (IOException io2) {
                System.out.println("Fatal Error Accepting Connection Request");
            }

        }

    }

    /**********************************************************
     * Main
     **********************************************************/
    public static void main(String[] args) {

        DCCServer ds = new DCCServer();
        ds.start();
    }

}
