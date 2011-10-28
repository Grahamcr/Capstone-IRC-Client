package ServerGuiCommunicationInterface;

import IRCConnection.UserInfo;
import IRCConnection.UserList;

public interface UserInfoInterface {
	
	public UserList getUserList();
	public void loadUserDateilAdress(UserInfo infor);
	
	public IrcChannelList getChannelList();
	public UserInfo getCurrentUser();
	
}
