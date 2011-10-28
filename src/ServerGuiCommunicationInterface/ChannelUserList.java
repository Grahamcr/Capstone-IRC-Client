package ServerGuiCommunicationInterface;

import java.util.ArrayList;
import java.util.Iterator;

public class ChannelUserList {

	private ArrayList<ChannelUser> userList = new ArrayList<ChannelUser>();
	
	
	public void removeAllUser()
	{
		userList.clear();
	}
	
	public ChannelUser addUser(UserInfo user, String loggedName)
	{
		if(getUserByName(user.getName()) == null)
		{			
			userList.add( new ChannelUser(user, loggedName));
		}
		
		return getUserByName(user.getName());
	}
	
	public void removeUser(ChannelUser user)
	{
		userList.remove(user);
	}
	
	public ArrayList<ChannelUser> getUserListArray()
	{
		return userList;
	}
	
	public ChannelUser getUserByName(String name)
	{
		Iterator<ChannelUser> it = userList.iterator();
		
		while( it.hasNext() )
		{
			ChannelUser u = it.next();
			if(u.getUser().getName().equals(name))
			{
				return u;
			}
		}
		
		return null;
	}
}
