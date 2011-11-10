package de.fhh.CapstoneIRC.Video;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.Player;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.TrackControl;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.EncryptionInfo;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.RemoteListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SendStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewParticipantEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.NewSendStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.ReceiverReportEvent;
import javax.media.rtp.event.RemoteEvent;
import javax.media.rtp.event.SendStreamEvent;
import javax.media.rtp.event.SessionEvent;
import javax.media.rtp.event.StreamClosedEvent;
import javax.media.rtp.rtcp.Feedback;
import javax.media.rtp.rtcp.ReceiverReport;
import javax.media.rtp.rtcp.SourceDescription;

public class RTPConnection implements ControllerListener,
SessionListener, SendStreamListener, RemoteListener, ReceiveStreamListener
{
	private		DataSource 			m_dataSource;
	private		VideoPlayer			m_player;
	private		String				m_remoteIP;
	private		int					m_localRtpPort				= 0;
	private		int					m_remoteRtpPort				= 0;
	private		SessionAddress		m_localSenderAddress		= null;
	private		SessionAddress		m_remoteReceiverAddress		= null;
	private		Processor			m_processor					= null;
	private		RTPManager			m_rtpManager				= null;
	private		SendStream			m_outStream					= null;
	private		VideoConnection		m_videoConnection			= null;
	
	public RTPConnection(VideoConnection vc, DataSource ds, String IP, int LocalPort, int RemotePort)
	{
		m_videoConnection = vc;
		m_dataSource = ds;
		m_remoteIP = IP;
		m_localRtpPort = LocalPort;
		m_remoteRtpPort = RemotePort;
		m_processor = null;
		if(m_dataSource != null)
		try
		{
			m_processor = Manager.createProcessor(m_dataSource);
			m_processor.addControllerListener(this);
		} catch (Exception e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void start()
	{
		startUpRTPManager();
		if(m_processor != null)
			m_processor.configure();
	}
	
	public void stop()
	{
		if(m_processor != null)
		{
			m_processor.stop();
			m_processor.deallocate();
			m_processor = null;
			System.out.println("Processor stopped");
		}
		if(m_rtpManager != null)
		{
			m_rtpManager.removeTargets("Session ended");
			m_rtpManager.dispose();
			m_rtpManager = null;
			System.out.println("rtpManager stopped");
		}
	}
	
	public Player getVideoProcessor()
	{
		return m_processor;
	}
	
	private void startUpRTPManager()
	{
		m_rtpManager = RTPManager.newInstance();
		m_rtpManager.addSessionListener(this);
		m_rtpManager.addSendStreamListener(this);
		m_rtpManager.addReceiveStreamListener(this);
		m_rtpManager.addRemoteListener(this);
		SourceDescription[] sdes =
			{
				new SourceDescription(SourceDescription.SOURCE_DESC_CNAME, SourceDescription.generateCNAME(), 1, false),
				new SourceDescription(SourceDescription.SOURCE_DESC_EMAIL, "julian.junghans@gmail.com", 1, false),
				new SourceDescription(SourceDescription.SOURCE_DESC_NAME, "Julian Junghans", 1, false)
			};
		InetAddress receiver = null;
		try
		{
			receiver = InetAddress.getByName(m_remoteIP);
		} catch (UnknownHostException e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		try
		{
			m_localSenderAddress = new SessionAddress(InetAddress.getLocalHost(), m_localRtpPort);
			m_remoteReceiverAddress = new SessionAddress(receiver, m_remoteRtpPort);
			m_rtpManager.initialize(new SessionAddress[] { m_localSenderAddress }, sdes, 0.03, 1.0, new EncryptionInfo(EncryptionInfo.NO_ENCRYPTION, new byte[] {}));
			m_rtpManager.addTarget(m_remoteReceiverAddress);
		} catch (InvalidSessionAddressException e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}catch (IOException e)
		{
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("RTP Manager started!");
	}

	@Override
	public void controllerUpdate(ControllerEvent event)
	{
		if(m_processor == null)
			return;
		if (event instanceof ConfigureCompleteEvent)
		{
			m_processor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			TrackControl tracks[] = m_processor.getTrackControls();
			boolean found = false;
			for (int i = 0; i < tracks.length; i++)
			{
				if (!found && (tracks[i].getFormat() instanceof VideoFormat))
				{
						if(null == tracks[i].setFormat(new VideoFormat(VideoFormat.JPEG_RTP)))
							System.err.println("Unsupported VideoFormat!");
						System.out.println("FrameRate: "+((VideoFormat)tracks[i].getFormat()).getFrameRate());
						found = true;
				}
				else
					tracks[i].setEnabled(false);
			}
			if (found)
				m_processor.realize();
			else
				System.err.println("No Videotrack!");
		}
		else if (event instanceof RealizeCompleteEvent)
		{
			try
			{
				DataSource dataOutput = m_processor.getDataOutput();
				m_outStream = m_rtpManager.createSendStream(dataOutput, 0);
				m_outStream.start();
			} catch(UnsupportedFormatException | IOException e)
			{
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
				return;
			}
			m_processor.start();
			System.out.println("Processor started");
		}
		if (event instanceof EndOfMediaEvent)
		{
			System.out.println("EndOfMediaEvent!");
			if(m_player.getVideoPlayer() == (Player)((EndOfMediaEvent)event).getSource())
			{
				m_player.stop();
				m_player = null;
			}
		}
	}
	
	@Override
	public void update(RemoteEvent re)
	{
		if (re instanceof ReceiverReportEvent)
		{
			ReceiverReport rr = ((ReceiverReportEvent) re).getReport();
			Participant participant =  rr.getParticipant();
			String reportSender = "unknown";
			if(participant != null)
				reportSender = participant.getCNAME();
			@SuppressWarnings("rawtypes")
			Vector infos = rr.getFeedbackReports();
			System.out.println("Feedback from Participant " + reportSender + ":");
			Feedback fb = null;
			for (int i = 0; i < infos.size(); i++)
			{
				fb = (Feedback) infos.elementAt(i);
				System.out.println("Lost Packages: " + fb.getNumLost()
						+ "   Jitter: " + fb.getJitter());
			}
		}
	}

	@Override
	public void update(SendStreamEvent sse)
	{
		if (sse instanceof NewSendStreamEvent)
		{
			System.out.println("New RTP DataStream has been created!");
		} else if (sse instanceof StreamClosedEvent)
		{
			System.out.println("RTP DataStream has been closed!");
		}
	}

	@Override
	public void update(SessionEvent se)
	{
		if (se instanceof NewParticipantEvent)
		{
			Participant newReceiver = ((NewParticipantEvent) se).getParticipant();
			String cname = newReceiver.getCNAME();
			System.out.println("New Participant: " + cname);
		}
	}
	
	public synchronized void update(ReceiveStreamEvent event)
	{
		if (event instanceof NewReceiveStreamEvent)
		{
			Participant newReceiver = ((NewReceiveStreamEvent) event).getParticipant();
			String cname = "unknown";
			if(newReceiver != null)
				cname = newReceiver.getCNAME();
			System.out.println("New incoming Stream: " + cname);
			ReceiveStream newStream = ((NewReceiveStreamEvent)event).getReceiveStream();
			try
			{
				m_player = new VideoPlayer(m_videoConnection.m_parent, newStream.getDataSource(),true);
				m_player.start();
			} catch (Exception e)
			{
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		else if(event instanceof ByeEvent)
		{
			Participant participant = ((ByeEvent) event).getParticipant();
			String cname = "unknown";
			if(participant != null)
				cname = participant.getCNAME();
			System.out.println(cname + " closed the stream: " + ((ByeEvent) event).getReason());
			if(m_player != null)
			{
				m_player.stop();
				m_player = null;
			}
		}
	}
}
