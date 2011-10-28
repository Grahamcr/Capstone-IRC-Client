package de.fhh.CapstoneIRC.Video;

import java.awt.Component;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.RealizeCompleteEvent;
import javax.media.protocol.DataSource;
import javax.media.protocol.SourceCloneable;

public class VideoConnection implements ControllerListener
{
	public  VideoChatWindow parent;
	private VideoSource vsource;
	private VideoPlayer vplayer;
	private RTPConnection rtpConn;
	private String		remoteIP;
	private int		rtpPort;
	private DataSource data;
	private DataSource dsc;
	
	public VideoConnection(VideoChatWindow parent, String IP, int Port)
	{
		this.parent = parent;
		remoteIP = IP;
		rtpPort = Port;
		vsource = new VideoSource_Webcam_JMF();
		vsource.initializeSource();
	}
	
	public void start()
	{
		System.out.println("VideoConnection->start()");
		vsource.openSource();
		data = vsource.getDataSource();
		data = Manager.createCloneableDataSource(data);
		dsc = ((SourceCloneable)data).createClone();
		vplayer = new VideoPlayer(parent, dsc, false);
		vplayer.start();
		rtpConn = new RTPConnection(this, data, remoteIP, rtpPort);
		rtpConn.start();
	}
	
	public void stop()
	{
		System.out.println("VideoConnection->stop()");
		rtpConn.stop();
		vplayer.stop();
		vsource.closeSource();
	}
	
	@Override
	public void controllerUpdate(ControllerEvent c)
	{
		if(c instanceof RealizeCompleteEvent)
		{
			System.err.println(c.getSource().toString());
			System.err.println(c.getSourceController().toString());
			Component visualComponent;
			if(c.getSourceController() == vplayer)
				if(vplayer.getVideoPlayer() != null)
			if((visualComponent = vplayer.getVideoPlayer().getVisualComponent()) != null)
			{
				parent.getPlayer2().add(visualComponent);
			}
			else
				System.err.println("vplayer.getVisualComponent() == null");

			if(c.getSourceController() == rtpConn)
				if(rtpConn.getVideoProcessor() != null)
			if((visualComponent = rtpConn.getVideoProcessor().getVisualComponent()) != null)
			{
				parent.getPlayer1().add(visualComponent);
			}
		}
	}
}
