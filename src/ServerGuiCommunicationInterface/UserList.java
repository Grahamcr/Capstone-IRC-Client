package ServerGuiCommunicationInterface;
import java.util.ArrayList;



public class UserList {
	
	private ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	
	
	public void addUser(UserInfo user)
	{
		userList.add(user);
	}
	
	public void removeUser(UserInfo user)
	{
		userList.remove(user);
	}
	
	public ArrayList<UserInfo> getUserList()
	{
		return userList;
	}
	
}
