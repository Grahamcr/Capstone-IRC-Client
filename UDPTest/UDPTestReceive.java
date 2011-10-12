import sun.net.*;
import java.net.*;
/***************************************************************************************************
 * UDPTestReceive is class created to test receiving a multicast UDP packet
 * 
 * @author Craig Graham
 * @version Fall 2011
 ***************************************************************************************************/
public class UDPTestReceive
{
    /**5555 choosen as it is in the dynamic port range - same value as sending class**/
    final int INPORT = 5555;
    
    /**IP address to listen to, same as the one choosen in the sending class**/
    final String listen = "225.4.5.6";
    
    /**Multicast socket which will bind to INPORT**/
    MulticastSocket mSocket;
     
    /**
     * Constructor for objects of class UDPTestReceive
     */
    public UDPTestReceive()
    {
        
    }

    /*********************************************************************************************
     * Main Method
     *********************************************************************************************/
     public static void main(String[] args) {
         UDPTestReceive utr = new UDPTestReceive();
         utr.start();
         
     } 
    /*********************************************************************************************
     * Start method - should be all we need for testing purposes
     *********************************************************************************************/
     public void start() {
         try{
            
            //Create a new MulticastSocket and bind it to the same port we send to in UDPSendTest
            mSocket = new MulticastSocket(INPORT);
         
            //Join the Socket to our listen group
            mSocket.joinGroup(InetAddress.getByName(listen));
         
            //Datagram packet used to receive
            byte[] buffer = new byte[1024];
            DatagramPacket data = new DatagramPacket(buffer, buffer.length);
            
            
            //See if we can't get some data
            mSocket.receive(data);
         
            //Print out if we find anything
            System.out.println("Received data from: " + data.getAddress().toString() +
		    ":" + data.getPort() + " with length: " +
		    data.getLength());
            System.out.write(data.getData(), 0, data.getLength());
            
            //leave and close the door on the way out
            mSocket.leaveGroup(InetAddress.getByName(listen));
            mSocket.close();
        } catch(Exception e) {
            System.out.println("Error Occured");
        }
         
     }

     
     
}
