package Audio.Receiver;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class VoicePlayer extends Thread{
	
	private SourceDataLine sourceLine;
	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;
	private DataLine.Info info;
	private boolean muted = false;
	
	public void setupSound(InputStream in) {
        InputStream audioStream = in;
        try {
            if (audioStream instanceof AudioInputStream) {
                audioInputStream = (AudioInputStream) audioStream;
            } else {
                audioInputStream = AudioSystem.getAudioInputStream(audioStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        audioFormat = audioInputStream.getFormat();
 
        info = new DataLine.Info(SourceDataLine.class, audioFormat);
 
        if (!AudioSystem.isLineSupported(info)) {
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F,
                false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat,
                audioInputStream);
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(SourceDataLine.class, audioFormat);
        }
 
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat, 44100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
 
    public void run() {
    	int left = 0;
    	byte[] buffer = new byte[640];
    	boolean run = true;
    	sourceLine.start();
    	while (run) {
 
            try {
            	
                while ((left = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                    if (left > 0 && !muted) {
                        sourceLine.write(buffer, 0, left);
                    }
 
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            sourceLine.drain(); 
 
            sourceLine.close(); 
 
        }
    }
    
    public void setMuted(boolean mute) {
    	muted = mute;
    }
}
