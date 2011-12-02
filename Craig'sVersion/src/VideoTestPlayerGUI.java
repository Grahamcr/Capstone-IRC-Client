 
/**
 * @author Julian Junghans
 * Main testing GUI for the Video Chat.
 * Prototype only!!!
 */

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
	private		JTextField					m_ipBox;
	private		JTextField					m_localPortBox;
	private		JTextField					m_remotePortBox;
	private		JButton						m_startVideoChatButton;
	private		ArrayList<VideoChatWindow>	m_playerList;

	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		VideoTestPlayerGUI gui = new VideoTestPlayerGUI();
	}
	
	public VideoTestPlayerGUI()
	{
		super("Video Capturing & Playback Sample");
		m_playerList = new ArrayList<VideoChatWindow>();
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
 
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				stop();
				System.exit(0);
			}
		});
		
		buildGUI();
		
	}
	
	public void stop()
	{
		for(VideoChatWindow window: m_playerList)
		{
			if(window!=null)
				window.stop();
		}
	}

	public boolean removeFromList(VideoChatWindow window)
	{
		return m_playerList.remove(window);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals("Start!"))
		{
			VideoChatWindow pp = new VideoChatWindow(this, m_ipBox.getText(), Integer.parseInt(m_localPortBox.getText()), Integer.parseInt(m_remotePortBox.getText()));
			m_playerList.add(pp);
			pp.start();
		}		
	}
	
	private void buildGUI()
	{
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
		pane.add(new JLabel("Local RTP Port"), c);
		c.gridx = 2;
		pane.add(new JLabel("Remote RTP Port"), c);
		c.gridx = 0;
		c.gridy = 1;
//		m_ipBox = new JTextField("224.0.0.1");
		m_ipBox = new JTextField("35.40.199.216");
		m_ipBox.setMinimumSize(d);
		m_ipBox.setPreferredSize(d);
		pane.add(m_ipBox, c);
		c.gridx = 1;
		m_localPortBox = new JTextField("3000");
		m_localPortBox.setMinimumSize(d);
		m_localPortBox.setPreferredSize(d);
		pane.add(m_localPortBox, c);

		c.gridx = 2;
		m_remotePortBox = new JTextField("3000");
		m_remotePortBox.setMinimumSize(d);
		m_remotePortBox.setPreferredSize(d);
		pane.add(m_remotePortBox, c);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 2;
		m_startVideoChatButton = new JButton("Start Video Chat");
		m_startVideoChatButton.addActionListener(this);
		m_startVideoChatButton.setActionCommand("Start!");
		pane.add(m_startVideoChatButton, c);
		this.setContentPane(pane);
		this.pack();
		this.setVisible(true);
	}
}
