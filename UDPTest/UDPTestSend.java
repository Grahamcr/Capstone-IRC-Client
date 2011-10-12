import java.net.*;
import sun.net.*;
/***************************************************************************************************
 * UDPTest is a class created to test if UDP Multicast will be a viable solution to send out
 * our audio data packets to multiple different end nodes.
 * 
 * @author Craig Graham
 * @version Fall 2011
 **************************************************************************************************/
public class UDPTestSend
{
    
    /**Port number to send out our test data on, 5555 choosen as it is in the Dynamic Port Range**/
    final int OUTPORT = 5555;  

    /**Time To Live for our data packets**/
    final int TTL = 1;
    
    /**Group IP Address to send to**/
    final String TOSEND = "225.4.5.6";
    
    /**Multicast Socket Used to send out data**/
    MulticastSocket mSocket;
    /********************************************************************************************
     * Constructor
     *******************************************************************************************/
    public UDPTestSend()
    {
        
    }
    /***********************************************************************************
     * Main method  
    ************************************************************************************/
    public static void main(String[] args) {
        UDPTestSend uts = new UDPTestSend();
        uts.start();
    }
    /***********************************************************************************
     * Start method - should be all we need for testing purposes
     ***********************************************************************************/
    public void start() {
        try{
            //No need to bind to a port, we are only sending
            mSocket = new MulticastSocket();
        
            /**No need to join others to the multicast because we are only sending**/
            
            //Keep sending out data just so that we can see if we receive any on the other
            //side or not
            while(1==1) {
            //create a buffer and fill it with some data to send as a test
            byte[] buffer = new byte[512];
            String dString = "This Is A Test";
            buffer = dString.getBytes();
            
            DatagramPacket data = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(TOSEND), OUTPORT);
        
            //Send out the data
            mSocket.send(data,(byte)TTL);
           }
        } catch (Exception e) {
            System.out.println("Error Occured");
        }
    }
    
}
