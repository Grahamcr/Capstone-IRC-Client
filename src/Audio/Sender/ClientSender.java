package Audio.Sender;

import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;

import org.xiph.speex.spi.SpeexEncoding;

public class ClientSender {
	private VoiceRecorder voiceRec;
	private VoiceEncoder voiceEnc;
	private Socket socket;
	
	public ClientSender(Socket socket) {
		this.socket = socket;
	}
	
//	private void send(byte[] data) throws IOException {
//		socket.getOutputStream().write(data);
//	}
	
	private void send(byte[] data, int length) throws IOException{
		if (!socket.isClosed())
			socket.getOutputStream().write(data, 0, length);
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
            int read = 0;
            read = voiceEnc.getEncode().read(buffer, 0, buffer.length);
            send(buffer,read);
		}
	}
	
}
