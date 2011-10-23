package de.fhh.CapstoneIRC.Video;

import java.awt.Component;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.protocol.DataSource;

public class VideoPlayer implements ControllerListener
{
	private DataSource data;
	private Player player;
	static private VideoChatWindow vcw;
	private boolean rtp;
	public VideoPlayer(DataSource ds, boolean rtp)
	{
		this(vcw,ds,rtp);
	}
	public VideoPlayer(VideoChatWindow vcw, DataSource ds, boolean rtp)
	{
		VideoPlayer.vcw = vcw;
		data = ds;
		this.rtp = rtp;
		try
		{
			player = Manager.createPlayer(data);
			player.addControllerListener(this);
			player.realize();
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		player.start();
		System.out.println("Player started");
	}
	
	public void stop()
	{
		if(player != null)
		{
			if(rtp)
				vcw.getPlayer1().removeAll();
			else
				vcw.getPlayer2().removeAll();
			player.close();
			System.out.println("Player stopped");
			player.deallocate();
			player = null;
		}	
	}
	
	public Player getVideoPlayer()
	{
		return player;
	}
	
	@Override
	public void controllerUpdate(ControllerEvent c)
	{
		if(c instanceof RealizeCompleteEvent)
		{
			Component visualComponent;
			if(getVideoPlayer() != null)
			if((visualComponent = getVideoPlayer().getVisualComponent()) != null)
			{
				if(rtp)
					vcw.getPlayer1().add(visualComponent);
				else
					vcw.getPlayer2().add(visualComponent);
			}
			else
				System.err.println("vplayer.getVisualComponent() == null");
		}
	}

}
