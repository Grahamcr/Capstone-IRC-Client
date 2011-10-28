package Audio.Receiver;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.xiph.speex.spi.SpeexEncoding;

public class ClientReceiver {
	private VoicePlayer player;
	private VoiceDecoder decoder;
	private DatagramSocket socket;
	private Thread receiver;
	private AudioInputStream in;
	
	public ClientReceiver() {
		try {
			socket = new DatagramSocket(51234);
			startReceiver();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
        player = new VoicePlayer();
        decoder = new VoiceDecoder();

        while (in == null)
        	System.out.println("WAIT2");
        
        decoder.setInputStream(in);
        decoder.start(new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2,
                44100.0F, false));
        while (decoder.getDecode() == null)
        	System.out.println("WAIT");
        player.setupSound(decoder.getDecode());
        player.start();
	}
	
	private void startReceiver() {
		try {
			receiver = new Thread() {
				byte[] receiveData = new byte[512];
				
				PipedOutputStream pout = new PipedOutputStream();
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				@Override
				public void run() {
			        try {
			        	PipedInputStream pin = new PipedInputStream(pout);
			        	in = new AudioInputStream(pin, new AudioFormat(SpeexEncoding.SPEEX_Q7, 44100.0F, 8, 1,
	            2, 44100.0F, false), AudioSystem.NOT_SPECIFIED);
			        	while (true) {
							socket.receive(receivePacket);
							pout.write(receivePacket.getData(), 0, receivePacket.getLength());
							System.out.println(receivePacket.getLength() + " Bytes empfangen");
			        	}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			};
			receiver.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
