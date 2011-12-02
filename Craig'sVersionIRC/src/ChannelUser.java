 



public class ChannelUser {
	UserInfo user;
	boolean isAdmin;
	boolean isOperator;
	
	public ChannelUser(UserInfo loggedUser, String loggedName) {
			
		user = loggedUser;
		
		if(loggedName.charAt(0) == '+')
		{
			isOperator = true;
		}
		else if(loggedName.charAt(0) == '@')
		{
			isAdmin = true;
		}			
	}
	
	public UserInfo getUser() {
		return user;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isOperator() {
		return isOperator;
	}

	public void setOperator(boolean isOperator) {
		this.isOperator = isOperator;
	}
	
	
}
