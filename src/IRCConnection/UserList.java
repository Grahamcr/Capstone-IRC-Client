package IRCConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;




public class UserList {
	
	private ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
	
	
	public UserInfo readWhoIsInfo(String line)
	{
		StringTokenizer tok = new StringTokenizer(line, " ");
		String part = tok.nextToken();
		part = tok.nextToken();
		int command = Integer.parseInt(part);
		
		tok.nextToken();
		part = tok.nextToken();
		
		UserInfo u = this.addUser(new UserInfo(part));
		
		
		switch(command)
		{
		case 311: 
			String clientProgramm = tok.nextToken();
			String host = tok.nextToken();
			
			String user = clientProgramm + "@" + host;
			u.setBenutzerInfo(user);
		
			
			String newLine = line.substring(line.indexOf("* :"));
			newLine = newLine.trim();
			u.setRealName( newLine.substring(1) );
			
			break;
		case 312: 
			String server = tok.nextToken();
			u.setServer(server);
			break;
		case 319: 
			String chan = tok.nextToken();
			chan = chan.substring(1);
			u.getChannelList().clear();
			u.getChannelList().add(chan);
			while(tok.hasMoreTokens())
			{
				chan = tok.nextToken();
				u.getChannelList().add(chan);
			}
			break;
		case 317:
			int idleTimeInSeconds = Integer.parseInt(tok.nextToken());
			u.setIdleTimeInSeconds(idleTimeInSeconds);
			break;
		case 318:
			
			return u;
		}
		
		return null;
	}
	
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
