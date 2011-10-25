package ServerGuiCommunicationInterface;

import java.util.ArrayList;

public class IrcChannel {
	String name;
	String topic;
	String password;
	
	ArrayList<Character> modeArray = new ArrayList<Character>();
	//ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	UserList userList = new UserList();
	
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
	
	public UserList getUserList()
	{
		return userList;
	}
	
	public void addUser(UserInfo user)
	{
		userList.addUser(user);
	}
	
	public void removeUser(UserInfo user)
	{
		userList.removeUser(user);
	}
	
	public UserInfo getUserByName(String name)
	{
		return userList.getUserByName(name);
	}
}
