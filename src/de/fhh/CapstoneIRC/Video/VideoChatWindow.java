package de.fhh.CapstoneIRC.Video;
/**
 * @author Julian Junghans
 * This Class opens up a window with 2 video players
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VideoChatWindow extends JFrame
{
	private		VideoConnection		m_videoConnection;
	private		JPanel				m_playerPanel1;
	private		VideoPlayer			m_player1 = null;
	private		JPanel				m_playerPanel2;
	private		VideoPlayer			m_player2 = null;
	private		VideoTestPlayerGUI	m_parent;
	
	public VideoChatWindow(String IP, int LocalPort, int RemotePort)
	{
		this(null, IP, LocalPort, RemotePort);
	}
	public VideoChatWindow(VideoTestPlayerGUI parent, String IP, int LocalPort, int RemotePort)
	{
		super("Video Chat with " + IP+":"+RemotePort);
		m_parent = parent;
		final VideoChatWindow self = this;
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				if(m_parent != null)
					m_parent.removeFromList(self);
				stop();
			}
		});
		buildGUI(IP, LocalPort, RemotePort);
	}
	
	public void start()
	{
		m_videoConnection.start();
	}
	
	public void stop()
	{
		
		m_videoConnection.stop();
	}

	public void setPlayer1(VideoPlayer newPlayer)
	{
		if(newPlayer == null)
		{
			m_playerPanel1.removeAll();
			System.err.println("newPlayer == null");
			return;
		}
		if(m_player1 == newPlayer)
		{
			return;
		}
		if(m_player1 != null)
			m_playerPanel1.removeAll();
		m_player1 = newPlayer;
		Component comp = m_player1.getVideoPlayer().getVisualComponent();
		if(comp == null)
			System.err.println("m_player1.getVideoPlayer().getVisualComponent() == null");
		m_playerPanel1.add(m_player1.getVideoPlayer().getVisualComponent());
	}
	
	public void setPlayer2(VideoPlayer newPlayer)
	{
		if(newPlayer == null)
		{
			m_playerPanel2.removeAll();
			return;
		}
		if(m_player2 == newPlayer || newPlayer == null)
			return;
		if(m_player2 != null)
			m_playerPanel2.removeAll();
		m_player2 = newPlayer;
		m_playerPanel2.add(m_player2.getVideoPlayer().getVisualComponent());
	}

	private void buildGUI(String IP, int LocalPort, int RemotePort)
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		Dimension d = new Dimension(640,480);
		c1.gridheight = 1;
		c1.gridwidth = 1;
		c1.gridx = 0;
		c1.gridy = 0;
		this.add(new JLabel(IP+":"+RemotePort), c1);
		c1.gridx = 1;
		try
		{
			this.add(new JLabel("You@"+ InetAddress.getLocalHost()+":"+LocalPort), c1);
		}
		catch (UnknownHostException e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		c1.gridx = 0;
		c1.gridy = 1;
		m_playerPanel1 = new JPanel();
		m_playerPanel1.setMinimumSize(d);
		m_playerPanel1.setPreferredSize(d);
		this.add(m_playerPanel1, c1);
		c1.gridx = 1;
		c1.gridy = 1;
		m_playerPanel2 = new JPanel();
		m_playerPanel2.setMinimumSize(d);
		m_playerPanel2.setPreferredSize(d);
		this.add(m_playerPanel2, c1);
		m_videoConnection = new VideoConnection(this, IP, LocalPort, RemotePort);
		this.pack();
		this.setVisible(true);
	}	
}
