package Audio.Sender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import org.xiph.speex.spi.SpeexEncoding;

public class ClientSender {
	private VoiceRecorder voiceRec;
	private VoiceEncoder voiceEnc;
	private ArrayList<String> connections = new ArrayList<String>();
	private Socket socket;
	
	public ClientSender() {
		 try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 51234);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addConnection(String ip) throws SocketException {
		connections.add(ip);
	}
	
	private void send(byte[] data) throws IOException {
		socket.getOutputStream().write(data);
			
	}

	public void start() throws IOException {
		voiceRec = new VoiceRecorder();
		voiceEnc = new VoiceEncoder();

		voiceRec.startLine();
		voiceEnc.setAudioFormat(new AudioFormat(SpeexEncoding.SPEEX_Q6, 44100.0F, 16, 1,
	            2, 44100.0F, false));
		voiceEnc.start(voiceRec.getAudioInputStream());
		
		while (voiceEnc.getEncode() == null) {
			System.out.println("WAIT");
		}
		byte[] buffer = new byte[512];
		
		while (true) {
			
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
