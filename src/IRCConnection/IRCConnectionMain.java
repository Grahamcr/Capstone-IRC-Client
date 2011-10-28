package IRCConnection;

import java.util.StringTokenizer;

import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcChannelList;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfo;
import ServerGuiCommunicationInterface.UserInfoInterface;
import ServerGuiCommunicationInterface.UserList;

public class IRCConnectionMain implements IrcServerInterface, UserInfoInterface {

	private TCPConnection tcp = null;
	IrcGuiInterface guiConnection = null;
	UserInfo currentUser = null;
	IrcChannelList chanList = new IrcChannelList();
	String serverName = "";
	UserList globalUserList = new UserList();
	
	
	
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
		    	 
		    	   guiConnection.writeString(user, text);
	    	   }
	       }
	       else if( commandToken.equals("001"))
			{
				serverName = firstToken.substring(1);
				guiConnection.writeString(serverName, "connection established");
			}
		   else if(firstToken.equals("PING"))
	       {
	    	   tcp.sendMessage("PONG " + commandToken);
	       }
	       else if( commandToken.equals("JOIN") )
	       {
	    	   String user = getNameFromToken(firstToken);	   
	    	   
	    	   if(user != this.getCurrentUser().getName())
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
	       else if( commandToken.equals("QUIT"))
	       {
	    	   String user = getNameFromToken(firstToken);	
	    	   chanList.removeUserFromChannels(user);
	    	   guiConnection.updateChannel(); 
	       }
	       else if( commandToken.equals("353")) // Start userlist of a given Channel
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
	       else if( commandToken.equals("366"))
	       {
	    	   st.nextToken();
	    	   
	    	   String channel = st.nextToken();
	    	   IrcChannel chan = chanList.getIrcChannel(channel);
	    	   guiConnection.openChannel(chan, false);
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
		guiConnection.writeString(getCurrentUser().getName(), message);
	}

	@Override
	public void sendPrivateMessage(String username, String message) {
		//tcp.sendMessage()
		
	}
}
