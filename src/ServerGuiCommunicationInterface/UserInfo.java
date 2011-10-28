package ServerGuiCommunicationInterface;

import java.net.InetAddress;


public class UserInfo {
	
	private String nickName;
	private InetAddress adress;
	private String realName;

	
	public UserInfo(String name) {
		super();
		
		if(name.charAt(0) == '+')
		{
			name = name.substring(1);
		}
		else if(name.charAt(0) == '@')
		{
			name = name.substring(1);
		}
		
		this.nickName = name;

	}
	
	
	
	public String getName() {
		return nickName;
	}
	public void setName(String name) {
		this.nickName = name;
	}
	public InetAddress getAdress() {
		return adress;
	}
	public void setAdress(InetAddress adress) {
		this.adress = adress;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}

	
	
}
