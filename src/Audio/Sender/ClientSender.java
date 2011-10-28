package Audio.Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import org.xiph.speex.spi.SpeexEncoding;

public class ClientSender {
	private VoiceRecorder voiceRec;
	private VoiceEncoder voiceEnc;
	private ArrayList<String> connections = new ArrayList<String>();
	private DatagramSocket socket;
	
	public ClientSender() {
		 try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addConnection(String ip) throws SocketException {
		connections.add(ip);
	}
	
	private void send(byte[] data) throws IOException {
		int port = 51234;
		for (String ip : connections) {
			InetAddress ipAddress = InetAddress.getByName(ip);
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, ipAddress, port);
			socket.send(sendPacket);
			System.out.println(data.length + " Bytes Send");
		}
			
	}

	public void start() throws IOException {
		voiceRec = new VoiceRecorder();
		voiceEnc = new VoiceEncoder();
		
		voiceRec.startLine();
		voiceEnc.setAudioFormat(new AudioFormat(SpeexEncoding.SPEEX_Q7, 44100.0F, 16, 1,
	            2, 44100.0F, false));
		voiceEnc.start(voiceRec.getAudioInputStream());
		
		while (voiceEnc.getEncode() == null)
			System.out.println("WAIT");
		
		while (true) {
				byte[] buffer = new byte[320];
	            int offset = 0;
	            int read = 0;
	            while (offset < buffer.length) {
	                read = voiceEnc.getEncode().read(buffer, offset, buffer.length - offset);
	                if (read != -1)
	                    offset = offset + read;
	            }
	            send(buffer);
		}
	}
	
}
