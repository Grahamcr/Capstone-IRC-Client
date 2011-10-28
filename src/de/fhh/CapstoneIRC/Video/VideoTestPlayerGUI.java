package de.fhh.CapstoneIRC.Video;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class VideoTestPlayerGUI extends JFrame implements ActionListener
{
	private JTextField 	m_ipBox;
	private JTextField	m_portBox;
	private JButton		m_startVideoChatButton;
	private ArrayList<VideoChatWindow> m_playerList;
//	private PlayerPanel m_playerPanel;
	/**
	 * VM Arguments must be -Djava.library.path="path-to-native-librarys"
	 * example: -Djava.library.path="C:\Users\Julian\Documents\FHH\workspace\IRC-Project\fmj\native\win32-x86"
	 * @param args 
	 */
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		VideoTestPlayerGUI gui = new VideoTestPlayerGUI();
	}
	
	public VideoTestPlayerGUI()
	{
		super("Video Capturing & Playback Sample");
		m_playerList = new ArrayList<VideoChatWindow>();
//		setSize(640, 480);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
 
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				stop();
				System.exit(0);
			}
		});
		
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		
		/*
		 * ------------------------------
		 * | "Partner IP"  |   "Port"   |
		 * |----------------------------|
		 * |   IP-Field    | Port-Field |
		 * |----------------------------|
		 * |		|Start Button|      |
		 * ------------------------------ 
		 */
		Dimension d = new Dimension(100,20);
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(new JLabel("Partner IP"), c);
		c.gridx = 1;
		pane.add(new JLabel("RTP Port"), c);
		c.gridx = 0;
		c.gridy = 1;
		m_ipBox = new JTextField("192.168.178.31");
		m_ipBox.setMinimumSize(d);
		m_ipBox.setPreferredSize(d);
		pane.add(m_ipBox, c);
		c.gridx = 1;
		m_portBox = new JTextField("22224");
		m_portBox.setMinimumSize(d);
		m_portBox.setPreferredSize(d);
		pane.add(m_portBox, c);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		m_startVideoChatButton = new JButton("Start Video Chat");
		m_startVideoChatButton.addActionListener(this);
		m_startVideoChatButton.setActionCommand("Start!");
		pane.add(m_startVideoChatButton, c);
		setContentPane(pane);
		pack();
		setVisible(true);
	}
	
	public void stop()
	{
		for(VideoChatWindow pp: m_playerList)
		{
			if(pp!=null)
				pp.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals("Start!"))
		{
			VideoChatWindow pp = new VideoChatWindow(m_ipBox.getText(), Integer.parseInt(m_portBox.getText()));
			m_playerList.add(pp);
			pp.start();
		}		
	}

}
