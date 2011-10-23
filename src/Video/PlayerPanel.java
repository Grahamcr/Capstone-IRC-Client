package de.fhh.CapstoneIRC.Video;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.RealizeCompleteEvent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel implements ControllerListener
{
	private Component visualComponent;
	private VideoSource vsource;
	
	public PlayerPanel()
	{
		setLayout(new BorderLayout());
		vsource = new VideoSource_Webcam_JMF();
		vsource.initializeSource();
	}
	
	public void start()
	{
		vsource.openSource();
		vsource.getVideoPlayer().addControllerListener(this);
	}
	
	public void stop()
	{
		vsource.closeSource();
	}

	@Override
	public void controllerUpdate(ControllerEvent c)
	{
		if(vsource.getVideoPlayer() == null)
			return;
 
		if(c instanceof RealizeCompleteEvent)
		{
			if((visualComponent = vsource.getVideoPlayer().getVisualComponent()) != null)
			{
				add(visualComponent, BorderLayout.CENTER);
//				add(vsource.getVideoPlayer().getControlPanelComponent(), BorderLayout.SOUTH);
			}
		}
	}
}
