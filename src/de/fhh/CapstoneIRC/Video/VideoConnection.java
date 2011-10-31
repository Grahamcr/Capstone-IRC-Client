package de.fhh.CapstoneIRC.Video;

import java.io.IOException;

import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.protocol.DataSource;
import javax.media.protocol.SourceCloneable;

public class VideoConnection
{
	public		VideoChatWindow		m_parent;
	private		VideoSource			m_vsource;
	private		VideoPlayer			m_vplayer;
	private		RTPConnection		m_rtpConn;
	private		String				m_remoteIP;
	private		int					m_rtpPort;
	private		DataSource			m_dataSource;
	private		DataSource			m_dataSourceClone;
	
	public VideoConnection(VideoChatWindow parent, String IP, int Port)
	{
		m_parent = parent;
		m_remoteIP = IP;
		m_rtpPort = Port;
		m_vsource = new VideoSource_Webcam_JMF();
		m_vsource.initializeSource();
	}
	
	public void start()
	{
		System.out.println("VideoConnection->start()");
		/* NOTEBOOK WORKAROUND */
		// try to open the webcam for 5 times;
		int tries = 0;
		boolean successfull = false;
		do
		{
			try
			{
				++tries;
				if(m_vsource.openSource())
					successfull = true;
			} //catch (NoDataSourceException | IOException e)
			 catch (Exception e)
			{
				if(tries >= 5)
				{
					System.err.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}while(!successfull && tries < 5);
		if(!successfull)
		{
			System.err.println("Could not open the Webcam Device!");
			return;
		}
		if(tries > 1)
			System.out.println("Needed " + tries + " tries to open the Webcam Device!");
		m_dataSource = m_vsource.getDataSource();
		m_dataSource = Manager.createCloneableDataSource(m_dataSource);
		m_dataSourceClone = ((SourceCloneable)m_dataSource).createClone();
		m_vplayer = new VideoPlayer(m_parent, m_dataSourceClone, false);
		m_vplayer.start();
		m_rtpConn = new RTPConnection(this, m_dataSource, m_remoteIP, m_rtpPort);
		m_rtpConn.start();
	}
	
	public void stop()
	{
		System.out.println("VideoConnection->stop()");
		if(m_rtpConn != null)
		{
			m_rtpConn.stop();
			m_rtpConn = null;
		}
		if(m_vplayer != null)
		{
			m_vplayer.stop();
			m_vplayer = null;
		}
		if(m_vsource != null)
		{
			m_vsource.closeSource();
			m_vsource = null;
		}
	}
}
