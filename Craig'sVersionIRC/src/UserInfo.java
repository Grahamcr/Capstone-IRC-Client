 

import java.net.InetAddress;
import java.util.ArrayList;


public class UserInfo {
	
	private String nickName;
	private InetAddress adress;
	private String realName;
	private ArrayList<String> channelList = new ArrayList<String>();
	private String server;
	private int idleTimeInSeconds;
	private String benutzerInfo;
	
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public ArrayList<String> getChannelList() {
		return channelList;
	}

	public void setChannelList(ArrayList<String> channelList) {
		this.channelList = channelList;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getIdleTimeInSeconds() {
		return idleTimeInSeconds;
	}

	public void setIdleTimeInSeconds(int idleTimeInSeconds) {
		this.idleTimeInSeconds = idleTimeInSeconds;
	}

	public String getBenutzerInfo() {
		return benutzerInfo;
	}

	public void setBenutzerInfo(String benutzerInfo) {
		this.benutzerInfo = benutzerInfo;
	}

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

	@Override
	public String toString() {
		
		return "User:" + getName() + " - Realname:" + getRealName() + " - Host:" + getBenutzerInfo();
		
	}
	
}
