package IRCConnection;

import FileTransfer.FileConnection;

import java.util.HashMap;
import java.util.StringTokenizer;

import Audio.AudioConnection;
import IRCGui.GetClientIP;
import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcChannelList;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfoInterface;



/**
 * 
 * @author Holger Rocks
 *
 *	This class is the main IRC-receiver.
 *	It opens a TCP-Connection to any given IRC-Server and
 *	is able to receive and send IRC-Messages
 *
 *	This class can be used several times side by side for multi-server
 *	IRC-Clients. 
 */
public class IRCConnectionMain implements IrcServerInterface, UserInfoInterface {

	private TCPConnection tcp = null;
	IrcGuiInterface guiConnection = null;
	UserInfo currentUser = null;
	IrcChannelList chanList = new IrcChannelList();
	String serverName = "";
	UserList globalUserList = new UserList();
	private HashMap<String, Boolean> videoRequstMap = new HashMap<String, Boolean>();
	private HashMap<String, AudioConnection> audioConnMap = new HashMap<String, AudioConnection>();
	private HashMap<String, FileConnection> fileConnMap = new HashMap<String, FileConnection>();
	
	
	
	@Override
	public void run() {
		
		while(true)
		{
			if( tcp == null )
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			String line = tcp.receiveLine();
			
			StringTokenizer st = new StringTokenizer(line, " ");
				
	       // Directly send the result for the PING-request
			String firstToken = st.nextToken();
			String commandToken = st.nextToken();
			
			
	       if(commandToken.equals("PRIVMSG"))
	       {  
	    	   String user = getNameFromToken(firstToken);	
	    	   
	    	   if(user != null)
	    	   {
		    	   String chan = st.nextToken();
		    	   String text = line.substring(line.indexOf(chan));
		    	   text = text.substring(text.indexOf(':') + 1);		    	   
		    	 
		    	   StringTokenizer tok = new StringTokenizer(text, " ");
	    		   String actionTok = tok.nextToken().trim();
	    		   System.out.println(actionTok);
		    	   
		    	   if(actionTok.equals("DVC") ) // it is a video connection, if the next parameters are ok!
		    	   {
		    		   try
		    		   {
			    		   String action = tok.nextToken();
			    		   int ip = Integer.parseInt(tok.nextToken());
		    			   int port = Integer.parseInt(tok.nextToken());
		    			   int audioPort = Integer.parseInt(tok.nextToken());
		    			   System.out.println(ip + " " + port);
			    		   
			    		   if(action.equals("REQ"))
			    		   {
			    			   videoRequstMap.put(user, true);
			    			   guiConnection.openVideoConnection(user, GetClientIP.intToIpAdress(ip), port, false);

		    				   guiConnection.openAudioConnection(user,GetClientIP.intToIpAdress(ip), audioPort);
			    			   
			    		   }
			    		   else
			    		   {
			    			   if(videoRequstMap.containsKey(user) && videoRequstMap.get(user) == true)
			    			   {
			    				   videoRequstMap.remove(user); // prevent that user opens again without request.
			    				   guiConnection.openVideoConnection(user, GetClientIP.intToIpAdress(ip), port, true);
			    				
			    				   guiConnection.openAudioConnection(user,GetClientIP.intToIpAdress(ip), audioPort);
			    			   }
			    			   
			    		   }
		    		   }
		    		   catch(Exception e)
		    		   {
		    			   
		    		   }
		    		   
		    	   }
		    	   else if(actionTok.equals("DAC") ) // it is a video connection, if the next parameters are ok!
		    	   {
		    		   try
		    		   {
			    		   int ip = Integer.parseInt(tok.nextToken());
		    			   int port = Integer.parseInt(tok.nextToken());
		    			   System.out.println(ip + " " + port);
			    		   
		    			   guiConnection.openAudioConnection(user,GetClientIP.intToIpAdress(ip), port);
		    			   
		    		   }
		    		   catch(Exception e)
		    		   {
		    			   
		    		   }
		    		   
		    	   } else if (actionTok.equalsIgnoreCase("dcc")) {
		    		   try {
		    			   String next = tok.nextToken();
		    			   if (next.equals("SEND")) {
		    				   String fileName = tok.nextToken();
		    				   fileName = fileName.substring(1, fileName.length()-1);
		    				   long ip = Long.parseLong(tok.nextToken());
			    			   int port = Integer.parseInt(tok.nextToken());
			    			   int size = Integer.parseInt(tok.nextToken().trim());
		    				   
			    			   System.out.println("eingehender DCC: \nDatei: " + fileName 
			    					   + "\nip: " + GetClientIP.longToIpAdress(ip)
			    					   + "\nport: " + port
			    					   + "\nsize: " + size);
		    				   
		    			   }
		    			   
		    		   } catch (Exception e) {
		    			   e.printStackTrace();
		    		   }
		    	   }
		    	   
		    	   guiConnection.writeString(chan, user, text);
	    	   }
	       }
	       else if( commandToken.equals("001"))
			{
				serverName = firstToken.substring(1);
				guiConnection.writeString(serverName, "connection established");
				
				this.sendText("join #test");
			}
		   else if(firstToken.equals("PING"))
	       {
	    	   tcp.sendMessage("PONG " + commandToken);
	       }
	       else if( commandToken.equals("JOIN") )
	       {
	    	   String user = getNameFromToken(firstToken);	   
	    	   userJoinsChannel(user, st);
	       }
	       else if (commandToken.equals("PART")) {
	    	   String user = getNameFromToken(firstToken);
	    	   userPartsChannel(user, st);
	       }
	       else if( commandToken.equals("QUIT"))
	       {
	    	   String user = getNameFromToken(firstToken);	
	    	   userQuitsChannel(user);
	       }
	       else if( commandToken.equals("353")) // Start userlist of a given Channel
	       {
	    	   openNewChannel(st);
	       }  
	       else if( commandToken.equals("366"))
	       {
	    	   openNewChannelReady(st);
	       }
	       else if( commandToken.equals("311") || commandToken.equals("312") ||
	    		    commandToken.equals("317") || commandToken.equals("318") ||
	    		    commandToken.equals("319"))
	       {
	    	   UserInfo u = globalUserList.readWhoIsInfo(line);
	    	   
	    	   if( u != null ) // if user != null, then we are ready with reading user info
	    	   {
	    		   guiConnection.writeString(u.getServer(), u.toString());
	    	   }
	       }
	       else if( commandToken.equals("401") )
	       {
	    	   st.nextToken();
	    	   String userInput = st.nextToken();
	    	   guiConnection.writeString( firstToken.substring(1), "User \"" + userInput + "\" not found");
	       }
	    	   
		}
	}

	@Override
	public UserList getUserList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadUserDateilAdress(UserInfo infor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IrcChannelList getChannelList() {
		// TODO Auto-generated method stub
		return chanList;
	}

	@Override
	public UserInfo getCurrentUser() {
		return currentUser;
	}

	@Override
	public void sendText(String text) {
		
		// todo: add channelname
		tcp.sendMessage(text);
		
	}

	@Override
	public void setTextStyle(TextStyle style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextReceiver(IrcGuiInterface connection) {
		guiConnection = connection;
		
	}
	
	public String getNameFromToken(String token)
	{	
		token = token.substring(1);
 	   int index =  token.indexOf('!');
 	   if(index > 0)
 	   {
 		  token = token.substring(0, index); 	   
 		  return token;
 	   }
 	   return null;
	}
	

	@Override
	public void openConnection(String ip, int port, String nickname, String realName ) {
		
		// Save current userinfo
		currentUser = new UserInfo(nickname);
		currentUser.setRealName(realName);
		
		tcp = new TCPConnection(ip, port);
		serverName = ip;

		if( realName == null)
		{
			realName = nickname;
		}
		
		// send the userinfo to the server
		String message = "USER CapIRC "+ tcp.getHostname() + " " + ip + " " + realName;
		tcp.sendMessage(message);
		tcp.sendMessage("NICK " + nickname);
	}

	@Override
	public void sentTextToChannel(String channel, String message) {
		
		sendText("PRIVMSG " + channel + " :" + message);
		guiConnection.writeString(channel, getCurrentUser().getName(), message);
	}

	@Override
	public void sendCommandMessage(String username, String message) {
		sendText("PRIVMSG " + username + " :" + message);
		
	}
	
	public void userQuitsChannel(String user)
	{
		chanList.removeUserFromChannels(user);
 	    guiConnection.updateChannel(); 
	}
	
	public void userJoinsChannel(String user, StringTokenizer st)
	{
		 if(!user.equals(this.getCurrentUser().getName()))
  	   {
  		   String channel = st.nextToken().substring(1);
    	   IrcChannel c = chanList.getIrcChannel(channel);
    	   if( c != null)
    	   {
    		    c.addUser(  globalUserList.addUser(new UserInfo(user)), user );
    		    guiConnection.updateChannel();
    	   }
  	   }
	}
	
	public void userPartsChannel(String user, StringTokenizer st) {
		if(user.equals(this.getCurrentUser().getName())) {
			String channel = st.nextToken();
			guiConnection.closeChannel(channel);
		}
	}
	
	public void openNewChannel(StringTokenizer st)
	{
		st.nextToken();
 	   st.nextToken();
 	   
 	   String channel = st.nextToken();
 	   
 	   IrcChannel chan = chanList.getIrcChannel(channel);
 	   
 	   if( chan == null)
 	   {
 		   chanList.addChannel(channel, null);
 		   chan = chanList.getIrcChannel(channel);
 	   }
 	   
 	   // Clear all entrys and start a new list
 	   // the whole list will be received from the Server
 	   chan.getUserList().removeAllUser();
 	   
 	   // The first username starts with a ':'
 	   // clear this char
 	   String name = st.nextToken().substring(1);
 	   chan.addUser(globalUserList.addUser(new UserInfo(name)), name);
 	   
 	   // fill the list
 	   while(st.hasMoreTokens())
 	   {
 		   name = st.nextToken();
 		   chan.addUser(globalUserList.addUser(new UserInfo(name)), name);
 	   }
 	   
 	   guiConnection.openChannel(chan, false);
	}
	
	public void openNewChannelReady(StringTokenizer st)
	{
		st.nextToken();
 	   
 	   String channel = st.nextToken();
 	   IrcChannel chan = chanList.getIrcChannel(channel);
 	   guiConnection.openChannel(chan, false);
	}

	@Override
	public void openVideoConnection(String username, int port, int portAudio, Boolean firstRequest) {
		// TODO Auto-generated method stub
//		String ip = GetClientIP.getAdress();
		String req = "REQ";
		
		if( firstRequest == false)
		{
			req = "RET";
		}
		
    	String message = "DVC " + req + " " + GetClientIP.getAdresAsInt() + " " + port + " " + portAudio;
    
    	videoRequstMap.put(username, true);
    	
    	
    	this.sendCommandMessage(username, message);
	}

	public void openAudioConnection(final String username, final int port, boolean video)
	{
		openAudioConnectionInternal(username, port, video);
	}
	
	
	public void openAudioConnectionInternal(final String username, final int port, boolean video)
	{
    	String message = "DAC " + GetClientIP.getAdresAsInt() + " " + port;
    	
    	if( video == true)
    	{
    		message = message + " 6668";
    	}
    	
    	if(audioConnMap.containsKey(username) && audioConnMap.get(username).getConnectionOpened() == true)
    	{
    		
    	}
    	else
    	{
    		new Thread( new Runnable() {
    			  public void run() {
    				  AudioConnection audio = new AudioConnection();
    				  audio.waitForAudioConnection(port);
    				  audio.setConnectionOpened(true);
    				  audioConnMap.put(username, audio);
    			  };
    			} ).start();	
    	}
    	
    	this.sendCommandMessage(username, message);
	}
	
	@Override
	public void openAudioConnection(final String username, final int port) {
		// TODO Auto-generated method stub
//		String ip = GetClientIP.getAdress();
		openAudioConnection(username, port, false);

    	
	}
	
	@Override
    public void openFileConnection(final String username, final int port) {
        // TODO Auto-generated method stub
//        String ip = GetClientIP.getAdress();
        String message = "FILE" + GetClientIP.getAdresAsInt() + " " + port;
        
        if(fileConnMap.containsKey(username) && fileConnMap.get(username).getConnectionOpened() == true)
        {
            
        }
        else
        {
            new Thread( new Runnable() {
                  public void run() {
                      FileConnection filec = new FileConnection();
                      filec.waitForAudioConnection(port);
                      filec.setConnectionOpened(true);
                      filec.startFileConnection(username, port);
                      fileConnMap.put(username, filec);
                  };
                } ).start();    
        }
        
        this.sendCommandMessage(username, message);
        
    }
	

	@Override
	public String getServerName() {
		return serverName;
		
	}
	
}
