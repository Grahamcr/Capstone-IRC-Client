package ServerGuiCommunicationInterface;

import java.util.ArrayList;

public class IrcChannel {
	String name;
	String topic;
	String password;
	
	ArrayList<Character> modeArray = new ArrayList<Character>();
	ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	
	
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
	
	public ArrayList<UserInfo> getUserList()
	{
		return userList;
	}
	
	public void addUser(UserInfo user)
	{
		userList.add(user);
	}
	
	public void removeUser(UserInfo user)
	{
		userList.remove(user);
	}
	
	public UserInfo getUserByName(String name)
	{
		for( UserInfo u : userList )
		{
			if(u.getName().equals(name))
			{
				return u;
			}
		}
		
		return null;
	}
}
