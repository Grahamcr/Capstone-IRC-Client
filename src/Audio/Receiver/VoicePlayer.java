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
    	byte[] buffer = new byte[2200];
    	boolean run = true;
    	sourceLine.start();
       while (run) {
 
            try {
            	
                while ((left = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                	System.out.println(left + " Bytes gehšrt");
                    if (left > 0) // data still left to write
                    {
                        int writtenbytes = sourceLine.write(buffer, 0, left);
                        System.out.println(writtenbytes + " Bytes gesprochen");
                        // the line
                    }
 
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                run = false;
            }
 
            sourceLine.drain(); // clear buffer
 
            sourceLine.close(); // close line
 
        }
    }
}
