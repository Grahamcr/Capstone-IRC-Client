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
	private VideoConnection videoConnection;
	private JPanel	player1;
	private JPanel	player2;
	
	public VideoChatWindow(String IP, int Port)
	{
		super("Video Chat with " + IP+":"+Port);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				stop();
			}
		});
		setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		Dimension d = new Dimension(640,480);
		c1.gridheight = 1;
		c1.gridwidth = 1;
		c1.gridx = 0;
		c1.gridy = 0;
		add(new JLabel(IP+":"+Port), c1);
		c1.gridx = 1;
		add(new JLabel("You"), c1);
		c1.gridx = 0;
		c1.gridy = 1;
		player1 = new JPanel();
		player1.setMinimumSize(d);
		player1.setPreferredSize(d);
		add(player1, c1);
		c1.gridx = 1;
		c1.gridy = 1;
		player2 = new JPanel();
		player2.setMinimumSize(d);
		player2.setPreferredSize(d);
		add(player2, c1);
		videoConnection = new VideoConnection(this, IP, Port);
		pack();
		setVisible(true);
	}
	
	public void start()
	{
		videoConnection.start();
	}
	
	public void stop()
	{
		videoConnection.stop();
	}

	public JPanel getPlayer1()
	{
		return player1;
	}
	public JPanel getPlayer2()
	{
		return player2;
	}

	
}
