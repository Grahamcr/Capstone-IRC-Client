package Audio.Receiver;

import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.xiph.speex.spi.SpeexEncoding;

public class ClientReceiver {
	private VoicePlayer player;
	private VoiceDecoder decoder;
	private Socket clientSocket;
	private Thread receiver;
	private AudioInputStream in;
	
	public ClientReceiver(Socket client) {
		clientSocket = client;
		startReceiver();
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
				
				@Override
				public void run() {
			        try {
			        	in = new AudioInputStream(clientSocket.getInputStream(), new AudioFormat(SpeexEncoding.SPEEX_Q6, 44100.0F, 16, 1,
	            2, 44100.0F, false), AudioSystem.NOT_SPECIFIED);
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			};
			receiver.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public VoicePlayer getPlyer() {
		return player;
	}

}
