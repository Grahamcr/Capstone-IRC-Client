package IRCGui;
import java.net.*;
import java.io.*;
import java.applet.*;

public class GetClientIP {

public static String getAdress() {
    try {
     InetAddress thisIp =
        InetAddress.getLocalHost();
     	return thisIp.getHostAddress(	);
     }
    catch(Exception e) {
    	return null;
     }
    }

	public static final String intToIpAdress(int input) {
	            
	    		int[] conv = new int[4];
	    		conv[3] = conv[3] | (input & 0xff);
	    		input >>= 8;
	    		conv[2] = conv[2] | (input & 0xff);
	    		input >>= 8;
	    		conv[1] = conv[1] | (input & 0xff);
	    		input >>= 8;
	    		conv[0] = conv[0] | (input & 0xff);

	    		return conv[0] + "." +conv[1] + "." +conv[2] + "." + conv[3];
	    	}
	public static final int byteArrToInt(byte[] b) 
	{
	    int l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    return l;
	}
	
	
	public static final int getAdresAsInt()
	{
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			byte arr[] = thisIp.getAddress();
			int adress = byteArrToInt(arr);
			return adress;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
		
	}

}