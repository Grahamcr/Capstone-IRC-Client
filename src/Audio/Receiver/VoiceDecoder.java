package Audio.Receiver;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.xiph.speex.spi.Speex2PcmAudioInputStream;

public class VoiceDecoder {

	private Thread decoder;
	private AudioInputStream decode;
	private InputStream in;
	
	public void start(final AudioFormat format) {
		decoder = new Thread() {
			 
            @Override
            public void run() {
                // AudioInputStream in = AudioDecoder.in;
                decode = new Speex2PcmAudioInputStream(in, format,
                    AudioSystem.NOT_SPECIFIED);
 
            }
        };
        decoder.start();
	}

	public Thread getDecoder() {
		return decoder;
	}

	public AudioInputStream getDecode() {
		return decode;
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}
	
	
}
