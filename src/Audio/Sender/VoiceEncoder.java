package Audio.Sender;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.xiph.speex.spi.Pcm2SpeexAudioInputStream;

public class VoiceEncoder {
	
	private AudioInputStream encode;
	private AudioFormat audioFormat;
	private Thread encoder;
	
	public VoiceEncoder() {
		
	}
	
	public void start(final AudioInputStream in) {
		encoder = new Thread() {
			 
            @Override
            public void run() {
                encode = new Pcm2SpeexAudioInputStream(in, audioFormat,
                    AudioSystem.NOT_SPECIFIED);
            }
        };
        encoder.start();
	}

	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

	public AudioInputStream getEncode() {
		return encode;
	}

	public Thread getEncoder() {
		return encoder;
	}
}
