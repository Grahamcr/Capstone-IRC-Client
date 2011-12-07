package IRCConnectionDummy;

import IRCConnection.UserInfo;
import IRCConnection.UserList;
import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcChannelList;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfoInterface;

public class IRCConnectionDummyMain implements IrcServerInterface, UserInfoInterface
{

	String arr[] = {"Jan", "Holger", "Craig", "Julian", "Steven", "James"};
	UserList userList = new UserList();
	
	
	IrcGuiInterface interfaceConnection = null;
	
	public IRCConnectionDummyMain() {
		
		for( String s : arr)
		{
			UserInfo info = new UserInfo(s);
			userList.addUser(info);
		}
		
		
	}
	
	@Override
	public void setTextReceiver(IrcGuiInterface connection) {
		
		// TODO Auto-generated method stub
		interfaceConnection = connection;
		
		IrcChannel chan = new IrcChannel("#test");
		chan.addUser(new UserInfo("Holger"), "Holger");
		chan.addUser(new UserInfo("Holger2"), "Holger2");
		chan.addUser(new UserInfo("Holger3"), "Holger3");
		chan.addUser(new UserInfo("Holger4"), "Holger4");
		
		connection.openChannel(chan, false);
	}
		
	@Override
	public UserList getUserList() {
		
		// Return UserInfo to the GUI
		return userList;
	}

	@Override
	public void loadUserDateilAdress(UserInfo infor) {
		

		// Load all UserDetails for a given User from the Server
		
	}

	@Override
	public void sendText(String text) {
		
		if(interfaceConnection != null)
		{
			interfaceConnection.writeString(this.getCurrentUser().getName(), text);
		}
		
	}

	@Override
	public void setTextStyle(TextStyle style) {
		
		// TextStyle for the Text
		
	}

	@Override
	public UserInfo getCurrentUser() {
		UserInfo info = new UserInfo("Holger");
		info.setRealName("Holger Rocks");
		return info;		
	}

	public void run() {
		
		// TODO Auto-generated method stub
		String arr[] = {"Jan", "Holger", "Craig", "Julian", "Steven", "James"};
		
		int curLine = 0;
		
		while(true) 
		{
			
			curLine ++;
			String user = arr[curLine % arr.length];
			if(interfaceConnection != null)
			{
				interfaceConnection.writeString(user, "bla bla bla");
			}
			
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public IrcChannelList getChannelList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openConnection(String ip, int port, String nickname, String realname ){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sentTextToChannel(String channel, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendCommandMessage(String username, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openVideoConnection(String username, int port, Boolean dummy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openAudioConnection(String username, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getServerName() {
		return "Name des Servers";
	}

	@Override
	public void openFileConnection(String username, int port) {
		// TODO Auto-generated method stub
		
	}



	
	
	
	
	
	

}
