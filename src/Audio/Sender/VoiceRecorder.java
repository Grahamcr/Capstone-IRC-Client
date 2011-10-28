package Audio.Sender;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class VoiceRecorder {

	private TargetDataLine targetLine;
	private AudioInputStream audioInputStream;
	private DataLine.Info info;
	
	public VoiceRecorder() {
		init();
	}
	
	private void init() {
		AudioFormat audioFormat = new AudioFormat(
	            AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F,
	            false);
	 
	        info = new DataLine.Info(TargetDataLine.class, audioFormat);
	        try {
	            targetLine = (TargetDataLine) AudioSystem.getLine(info);
	            targetLine.open(audioFormat, targetLine.getBufferSize());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        audioInputStream = new AudioInputStream(targetLine);
	 
	        audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
	            audioInputStream);
	        audioFormat = audioInputStream.getFormat();
	}

	public void startLine() {
		targetLine.start();
	}

	public void stopLine() {
		targetLine.stop();
	}
	
	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}

	public DataLine.Info getInfo() {
		return info;
	}
	
}
