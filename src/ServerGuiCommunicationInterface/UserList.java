package ServerGuiCommunicationInterface;
import java.util.ArrayList;
import java.util.Iterator;



public class UserList {
	
	private ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	
	
	public void removeAllUser()
	{
		userList.clear();
	}
	
	public UserInfo addUser(UserInfo user)
	{
		if(getUserByName(user.getName()) == null)
		{			
			userList.add(user);
		}
		
		return getUserByName(user.getName());
	}
	
	public void removeUser(UserInfo user)
	{
		userList.remove(user);
	}
	
	public ArrayList<UserInfo> getUserListArray()
	{
		return userList;
	}
	
	public UserInfo getUserByName(String name)
	{
		Iterator<UserInfo> it = userList.iterator();
		
		while( it.hasNext() )
		{
			UserInfo u = it.next();
			if(u.getName().equals(name))
			{
				return u;
			}
		}
		
		return null;
	}
}
