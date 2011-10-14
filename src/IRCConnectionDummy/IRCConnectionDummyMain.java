package IRCConnectionDummy;

import java.util.ArrayList;

import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfo;
import ServerGuiCommunicationInterface.UserInfoInterface;
import ServerGuiCommunicationInterface.UserList;

public class IRCConnectionDummyMain implements IrcServerInterface, UserInfoInterface
{

	String arr[] = {"Jan", "Holger", "Craig", "Julian", "Steven", "James"};
	UserList userList = new UserList();
	
	
	IrcGuiInterface interfaceConnection = null;
	
	public IRCConnectionDummyMain() {
		
		for( String s : arr)
		{
			UserInfo info = new UserInfo(s, null, s);
			userList.addUser(info);
		}
		
		
	}
	
	@Override
	public void setTextReceiver(IrcGuiInterface connection) {
		
		// TODO Auto-generated method stub
		interfaceConnection = connection;
		
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
		UserInfo info = new UserInfo("Holger", null, "Holger Rocks");
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
	public ArrayList<String> getChannelList() {
		// TODO Auto-generated method stub
		return null;
	}



	
	
	
	
	
	

}
