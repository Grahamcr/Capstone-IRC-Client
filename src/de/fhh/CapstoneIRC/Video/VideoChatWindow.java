package de.fhh.CapstoneIRC.Video;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VideoChatWindow extends JFrame
{
	private		VideoConnection		m_videoConnection;
	private		JPanel				m_player1;
	private		JPanel				m_player2;
	private		VideoTestPlayerGUI	m_parent;
	
	public VideoChatWindow(VideoTestPlayerGUI parent, String IP, int Port)
	{
		super("Video Chat with " + IP+":"+Port);
		m_parent = parent;
		final VideoChatWindow self = this;
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				m_parent.removeFromList(self);
				stop();
			}
		});
		buildGUI(IP, Port);
	}
	
	public void start()
	{
		m_videoConnection.start();
	}
	
	public void stop()
	{
		
		m_videoConnection.stop();
	}

	public JPanel getPlayer1()
	{
		return m_player1;
	}
	
	public JPanel getPlayer2()
	{
		return m_player2;
	}

	private void buildGUI(String IP, int Port)
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		Dimension d = new Dimension(640,480);
		c1.gridheight = 1;
		c1.gridwidth = 1;
		c1.gridx = 0;
		c1.gridy = 0;
		this.add(new JLabel(IP+":"+Port), c1);
		c1.gridx = 1;
		this.add(new JLabel("You"), c1);
		c1.gridx = 0;
		c1.gridy = 1;
		m_player1 = new JPanel();
		m_player1.setMinimumSize(d);
		m_player1.setPreferredSize(d);
		this.add(m_player1, c1);
		c1.gridx = 1;
		c1.gridy = 1;
		m_player2 = new JPanel();
		m_player2.setMinimumSize(d);
		m_player2.setPreferredSize(d);
		this.add(m_player2, c1);
		m_videoConnection = new VideoConnection(this, IP, Port);
		this.pack();
		this.setVisible(true);
	}	
}
