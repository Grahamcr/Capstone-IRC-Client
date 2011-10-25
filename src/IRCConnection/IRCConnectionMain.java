package IRCConnection;

import java.util.ArrayList;
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
			
			
			if( line.contains("001"))
			{
				serverName = firstToken.substring(1);
				guiConnection.writeString(serverName, "connection established");
			}
			else if(firstToken.equals("PING"))
	       {
	    	   tcp.sendMessage("PONG " + st.nextToken());
	       }
	       else if(line.contains("PRIVMSG"))
	       {
	    	   
	    	   
	    	   firstToken = firstToken.substring(1);
	    	   int index =  firstToken.indexOf('!');
	    	   if(index > 0)
	    	   {
		    	   firstToken = firstToken.substring(0, index);
		    	   
		    	   String user = firstToken;
		    	
		    	   
		    	   st.nextToken();
		    	   String chan = st.nextToken();
		    	   String text = line.substring(line.indexOf(chan));
		    	   text = text.substring(text.indexOf(':') + 1);
		    	   //String text = st.nextToken().substring(1);
		    	   
		    	   
		    	   guiConnection.writeString(user, text);
	    	   }
	       }
	       else if( line.contains("JOIN") )
	       {
	    	   firstToken = firstToken.substring(1);
	    	   firstToken = firstToken.substring(0, firstToken.indexOf('!'));
	    	   
	    	   String user = firstToken;
	    	   st.nextToken();
	    	   
	    	   
	    	   if(user != this.getCurrentUser().getName())
	    	   {
	    		   String channel = st.nextToken().substring(1);
		    	   IrcChannel c = chanList.getIrcChannel(channel);
		    	   if( c != null)
		    	   {
		    		    c.addUser(new UserInfo(user, null, null));
		    		    guiConnection.updateChannel();
		    	   }
	    	   }
	    	   
	    	   
	       }
	       else if( line.contains("QUIT"))
	       {
	    	   firstToken = firstToken.substring(1);
	    	   firstToken = firstToken.substring(0, firstToken.indexOf('!'));
	    	   
	    	   String user = firstToken;
	    	   chanList.removeUserFromChannels(user);
	    	   guiConnection.updateChannel();
	    	   
	    	   
	    	   
	       }
	       else if( line.contains("353")) // Start userlist of a given Channel
	       {
	    	   st.nextToken();
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
	    	  
	    	   chan.addUser(new UserInfo(st.nextToken().substring(1), null, null));
	    	   
	    	   // fill the list
	    	   while(st.hasMoreTokens())
	    	   {
	    		   chan.addUser(new UserInfo(st.nextToken(), null, null));
	    	   }
	    	   
	    	   guiConnection.openChannel(chan, false);
	       }  
	       else if( line.contains("366"))
	       {
	    	   st.nextToken();
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
	

	@Override
	public void openConnection(String ip, int port, String nickname, String realname ) {
		
		// Save current userinfo
		currentUser = new UserInfo(nickname, null, realname);
		tcp = new TCPConnection(ip, port);
		serverName = ip;
		
		// open the connection to the assigned server
		//Thread t = new Thread(tcp);
		//t.start();
		
		if( realname == null)
		{
			realname = nickname;
		}
		
		
		// send the userinfo to the server
		String message = "USER CapIRC "+ tcp.getHostname() + " " + ip + " " + realname;
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
