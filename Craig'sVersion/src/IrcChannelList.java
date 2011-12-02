 

import java.util.ArrayList;
import java.util.Iterator;

public class IrcChannelList {
		
	public ArrayList<IrcChannel> channelList = new ArrayList<IrcChannel>();
	
	
	public IrcChannelList() {
		// TODO Auto-generated constructor stub
	}
	
	public void removeUserFromChannels(String username)
	{		
		Iterator<IrcChannel> it = channelList.iterator();
		
		while( it.hasNext() )
		{
			IrcChannel chan = it.next();
			ChannelUser u = chan.getUserByName(username);
			if( u != null)
			{
				chan.removeUser(u);
			}
		}
	}
	
	public void addChannel(String channel, String password)
	{
		IrcChannel chan = new IrcChannel(channel);
		chan.loadChannelData(password);
		
		channelList.add(chan);
	}
	
	public IrcChannel getIrcChannel(String channel)
	{
		
		Iterator<IrcChannel> it = channelList.iterator();
		
		while( it.hasNext() )
		{
			IrcChannel chan = it.next();
			if(chan.getName().equals(channel))
			{
				return chan;
			}
		}
		
		return null;
	}
	
	
	public void removeChannel(String channel)
	{
		Iterator<IrcChannel> it = channelList.iterator();
		
		while( it.hasNext() )
		{
			IrcChannel chan = it.next();
			
			if(channel.equals(chan.getName()))
			{
				channelList.remove(chan);
				return;
			}
		}
	}
	
	
	
	
	
	
}
