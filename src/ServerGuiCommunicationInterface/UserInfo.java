package ServerGuiCommunicationInterface;

import java.net.InetAddress;


public class UserInfo {
	
	private String name;
	private InetAddress adress;
	private String realName;

	
	public UserInfo(String name, InetAddress adress, String realName) {
		super();
		this.name = name;
		this.adress = adress;
		this.realName = realName;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
