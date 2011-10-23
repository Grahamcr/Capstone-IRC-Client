package de.fhh.CapstoneIRC.Video;

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
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.EncryptionInfo;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.RemoteListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SendStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionListener;
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
	private DataSource 			data;
	private String				remoteIP;
	private VideoPlayer			player;
	private int					rtpPort					= 0;
	private SessionAddress		localSenderAddress		= null;
	private SessionAddress		remoteReceiverAddress	= null;
	private Processor			p						= null;
	private RTPManager			mgr						= null;
	private SendStream			outStream				= null;
	private VideoConnection		videoConnection			= null;
	
	public RTPConnection(VideoConnection vc, DataSource ds, String IP, int Port)
	{
		videoConnection = vc;
		data = ds;
		remoteIP = IP;
		rtpPort = Port;
		try
		{
			p = Manager.createProcessor(data);
			p.addControllerListener(this);
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	public void start()
	{
		p.configure();
	}
	
	public void stop()
	{
		if(p != null)
		{
			p.stop();
			System.out.println("Processor stopped");
			p.deallocate();
			p = null;
		}
		if(mgr != null)
		{
			mgr.removeTargets("Session ended");
			mgr.dispose();
			mgr = null;
		}
	}
	
	public Player getVideoProcessor()
	{
		return p;
	}

	@Override
	public void controllerUpdate(ControllerEvent event)
	{
		if (event instanceof ConfigureCompleteEvent)
		{
			// Output-Processor-Format setzen
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			// Videotrack suchen und Output-Format (+ Codec-Chain) Setzen
			TrackControl tracks[] = p.getTrackControls();
			boolean found = false;
			for (int i = 0; i < tracks.length; i++)
			{
				if (!found && (tracks[i].getFormat() instanceof VideoFormat))
				{
					try
					{
						if(null == tracks[i].setFormat(new VideoFormat(VideoFormat.JPEG_RTP)))
							System.err.println("Unsupported VideoFormat!");
						System.out.println("FrameRate: "+((VideoFormat)tracks[i].getFormat()).getFrameRate());
						found = true;
					} catch (Exception e)
					{
						System.err.println(e.getLocalizedMessage());
					}
				}
				else
					tracks[i].setEnabled(false);
			}
			if (found) // wir habe ein Videotrack mit RGB & JPEG Konversion
				p.realize();
			else
				System.err.println("Kein Videotrack!");
			// Processor realized event => jetzt kann rtp-manager erzeugt und
			// konfiguriert werden
		}
		else if (event instanceof RealizeCompleteEvent)
		{
			DataSource dataOutput = p.getDataOutput(); // Output des Processors
			mgr = RTPManager.newInstance();
			mgr.addSessionListener(this); // Add „update“-Listeners
			mgr.addSendStreamListener(this);
			mgr.addReceiveStreamListener(this);
			mgr.addRemoteListener(this);
			SourceDescription[] sdes =
				{ // Sender-Name (geht auch mit Default-Namen)
					new SourceDescription(SourceDescription.SOURCE_DESC_CNAME, SourceDescription.generateCNAME(), 1, false),
					new SourceDescription(SourceDescription.SOURCE_DESC_EMAIL, "julian.junghans@gmail.com", 1, false),
					new SourceDescription(SourceDescription.SOURCE_DESC_NAME, "Julian Junghans", 1, false)
				};
			InetAddress receiver = null;
			try
			{
				receiver = InetAddress.getByName(remoteIP); // Emfänger-IP
			} catch (UnknownHostException e)
			{
				System.err.println(e.getLocalizedMessage());
			}
			try
			{
				localSenderAddress = new SessionAddress(InetAddress.getLocalHost(), rtpPort);
				remoteReceiverAddress = new SessionAddress(receiver, rtpPort, 0);
				mgr.initialize(new SessionAddress[] { localSenderAddress }, sdes, 0.03, 1.0, new EncryptionInfo(EncryptionInfo.NO_ENCRYPTION, new byte[] {}));
				mgr.addTarget(remoteReceiverAddress);
				outStream = mgr.createSendStream(dataOutput, 0);
				outStream.start();
			} catch (Exception e)
			{
				System.err.println(e.getLocalizedMessage());
			}
			p.start(); // Starte Processor
			System.out.println("Processor started");
		}
		if (event instanceof EndOfMediaEvent)
		{
			if(player.getVideoPlayer() == (Player)((EndOfMediaEvent)event).getSource())
			{
				player.stop();
				player = null;
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
			System.out.println("Feedback von Teilnehmer " + reportSender + ":");
			Feedback fb = null;
			for (int i = 0; i < infos.size(); i++)
			{
				fb = (Feedback) infos.elementAt(i);
				System.out.println("Verlorene Pakete: " + fb.getNumLost()
						+ "   Jitter: " + fb.getJitter());
			}
		}
	}

	@Override
	public void update(SendStreamEvent sse)
	{
		if (sse instanceof NewSendStreamEvent)
		{
			System.out.println("Ein neuer RTP Datenstrom wurde erzeugt!");
		} else if (sse instanceof StreamClosedEvent)
		{
			System.out.println("RTP Datenstrom wurde geschlossen!");
		}
	}

	@Override
	public void update(SessionEvent se)
	{
		if (se instanceof NewParticipantEvent)
		{
			Participant newReceiver = ((NewParticipantEvent) se).getParticipant();
			String cname = newReceiver.getCNAME();
			System.out.println("Neuer Teilnehmer: " + cname);
		}
	}
	
	public synchronized void update(ReceiveStreamEvent event)
	{
		if (event instanceof NewReceiveStreamEvent)
		{
			ReceiveStream newStream = ((NewReceiveStreamEvent)event).getReceiveStream();
			try
			{
				player = new VideoPlayer(videoConnection.parent, newStream.getDataSource(),true);
				player.start();
			} catch (Exception e)
			{
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
}
