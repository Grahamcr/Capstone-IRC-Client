package ServerGuiCommunicationInterface;

import java.util.ArrayList;

import IRCConnection.UserInfo;

public class IrcChannel {
	String name;
	String topic;
	String password;
	
	ArrayList<Character> modeArray = new ArrayList<Character>();
	//ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	ChannelUserList userList = new ChannelUserList();
	
	/**
	 * This funktion should be overwrited and load the userlist of the given channel
	 */
	public void loadChannelData(String password)
	{
		
	}
		
	public String getName()
	{
		return name;
	}
		
	public IrcChannel(String name) {
		
		this.name = name;		
	}
	
	public ChannelUserList getUserList()
	{
		return userList;
	}
	
	public void addUser(UserInfo user, String name)
	{
		userList.addUser(user, name);
	}
	
	public void removeUser(ChannelUser user)
	{
		userList.removeUser(user);
	}
	
	public ChannelUser getUserByName(String name)
	{
		return userList.getUserByName(name);
	}
}
