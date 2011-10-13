package de.fhh.CapstoneIRC.Video;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class VideoTestPlayerGUI extends JFrame
{
	private PlayerPanel m_playerPanel;
	/**
	 * VM Arguments must be -Djava.library.path="path-to-native-librarys"
	 * example: -Djava.library.path="C:\Users\Julian\Documents\FHH\workspace\IRC-Project\fmj\native\win32-x86"
	 * @param args 
	 */
	public static void main(String[] args)
	{
		VideoTestPlayerGUI gui = new VideoTestPlayerGUI();
		gui.start();
	}
	
	public VideoTestPlayerGUI()
	{
		super("Video Capturing & Playback Sample");
		
		setSize(640, 480);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
 
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				stop();
				System.exit(0);
			}
		});
		
		m_playerPanel = new PlayerPanel();
		setContentPane(m_playerPanel);
		setVisible(true);
	}
	
	public void start()
	{
		if(m_playerPanel != null)
			m_playerPanel.start();
	}
	
	public void stop()
	{
		if(m_playerPanel != null)
			m_playerPanel.stop();
	}

}
