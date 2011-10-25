package ServerGuiCommunicationInterface;

import java.util.ArrayList;

public interface UserInfoInterface {
	
	public UserList getUserList();
	public void loadUserDateilAdress(UserInfo infor);
	
	public IrcChannelList getChannelList();
	public UserInfo getCurrentUser();
	
}
