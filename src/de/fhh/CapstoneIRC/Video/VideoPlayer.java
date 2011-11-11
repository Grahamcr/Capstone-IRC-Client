package de.fhh.CapstoneIRC.Video;
/**
 * @author Julian Junghans
 * Class that contains the video player logic
 */

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.protocol.DataSource;

public class VideoPlayer implements ControllerListener
{
	private			DataSource		m_data;
	private 		Player			m_player;
	private 		boolean			m_rtp;
	static private	VideoChatWindow m_vcw;
	
	public VideoPlayer(DataSource ds, boolean rtp)
	{
		this(m_vcw,ds,rtp);
	}
	public VideoPlayer(VideoChatWindow vcw, DataSource ds, boolean rtp)
	{
		m_vcw = vcw;
		m_data = ds;
		m_rtp = rtp;
		try
		{
			m_player = Manager.createPlayer(m_data);
			m_player.addControllerListener(this);
			m_player.realize();
		} catch (Exception e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		if(m_player != null)
		{
			m_player.start();
			System.out.println("Player started");
		}
		else
			System.err.println("Player could not be started, because Player is null!");
	}
	
	public void stop()
	{
		if(m_player != null)
		{
			if(m_rtp)
				m_vcw.setPlayer1(null);
			else
				m_vcw.setPlayer2(null);
			m_player.stop();
			m_player.deallocate();
			m_player.close();
			m_player = null;
			System.out.println("Player stopped");
		}	
	}
	
	public Player getVideoPlayer()
	{
		return m_player;
	}
	
	@Override
	public void controllerUpdate(ControllerEvent c)
	{
		if(c instanceof RealizeCompleteEvent)
		{
			if(getVideoPlayer() != null)
			if(getVideoPlayer().getVisualComponent() != null)
			{
				if(m_rtp)
					m_vcw.setPlayer1(this);
				else
					m_vcw.setPlayer2(this);
			}
			else
				System.err.println("VideoPlayer.getVisualComponent() == null");
		}
	}

}
