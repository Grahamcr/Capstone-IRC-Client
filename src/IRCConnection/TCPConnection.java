package IRCConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * 
 * @author Holger Rocks
 *
 *	This class is the basic TCP-IP system. All commands will
 *	be received or sended by this class. It works as a buffered 
 *	Stream-reader, which returns the TCP-Stream line by line.
 *	If there is no new line, the class will wait until there is
 *	any new text. 
 *
 *	ATTENTION: This class can lock your code.
 */
public class TCPConnection implements Runnable {

	java.net.Socket socket = null;
	BufferedReader bufferedReader = null;
	

	@Override
	public void run() {
		
		try {
			
			BufferedReader bufferedReader = null;
			while(true)
			{
				if( socket != null)
				{
					if(bufferedReader == null)
					{
						bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
					}
			       
			       
			       String line = bufferedReader.readLine();
			       StringTokenizer st = new StringTokenizer(line, " ");
					
			       // Directly send the result for the PING-request
			       if( st.nextToken().equals("PING"))
			       {
			    	   String pongMsg = "PONG " + st.nextToken();
			    	   if(st.hasMoreTokens())
			    	   {
			    		   pongMsg = pongMsg + " " + st.nextToken();
			    	   }
			    	   this.sendMessage(pongMsg);
			       }
			    
			       System.out.println(line);
			       System.out.flush();
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	

	public String receiveLine()
	{
		String line = "";
		try
		{
			if( socket != null)
			{
				if(bufferedReader == null)
				{
					bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
				}
		      
		       line = bufferedReader.readLine();	       
		       System.out.println(line);
		       System.out.flush();
			}			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return line;
	}
	
	
	public TCPConnection(String ip, int port) {
		// TODO Auto-generated constructor stub
	
		try 
		{
			//ip = "127.0.0.1"; // localhost
	        //port = 11111;
			socket = new java.net.Socket(ip,port);
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String getHostname()
	{
		InetAddress addr;
		
		try 
		{
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();			
			return hostname;
			
		} 
		catch (UnknownHostException e) 
		{
	
			e.printStackTrace();
			return null;
		}
	}
	
//	public static void main(String[] args) throws IOException, InterruptedException {
//		  
//		String ip = "127.0.0.1";
//		
//		TCPConnection tcp = new TCPConnection(ip, 6667);
//		Thread t = new Thread(tcp);
//		t.start();
//		
//		String nachricht = "USER CapstoneIRC "+ tcp.getHostname() + " " + ip + " Holger";
//				
//		System.out.println(nachricht);
//		tcp.sendMessage(nachricht);
//		tcp.sendMessage("NICK Holger5");
//		
//		tcp.sendMessage("JOIN #test");
//		
//		while(true)
//		{
//			Thread.sleep(1000);
//			//tcp.sendMessageToChannel("test", "Dies ist eine Testnachricht");
//		}
//     }
	

	
	
	 public void sendMessage(String nachricht)
	 {
		DataOutputStream outToServer;
		try {
				outToServer = new DataOutputStream(socket.getOutputStream());
				outToServer.writeBytes(nachricht + '\n');   
		} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();	
		}
	}


	
}
