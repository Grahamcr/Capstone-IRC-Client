import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/****************************************************************
 * Write a description of class VoiceClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 *************************************************************/
public class VoiceCapture extends JFrame {
  
    TargetDataLine targetDataLine;
       
    AudioFormat audioFormat;
    
    ByteArrayOutputStream byteArrayOutputStream;
    
    boolean stopCapture = false;
       
    AudioInputStream audioInputStream;
    
    SourceDataLine sourceDataLine;
    
    /**Temp only**/
    JButton captureBtn;
    JButton stopBtn;
    JButton playBtn;
    
    
    /**************************************************************
     * Constructor - used only at this point to set up temp GUI screen
     ****************************************************************/
    public VoiceCapture() {
        setLayout(new FlowLayout());
        JButton captureBtn = new JButton("Capture");
        JButton stopBtn = new JButton("Stop");
        JButton playBtn = new JButton("Playback");
        add(captureBtn);
        add(stopBtn);
        add(playBtn);
        /**
        captureBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        playBtn.addActionListener(this);
        **/
        
      captureBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                captureAudio();
            }
       }
       );
       
      stopBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                stopCapture = true;
            }
      }
      );
      
      playBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                playRecorded();
            }
      }
      );
        
        setVisible(true);
    }
    /**********************************************************************************************
     * Main Method 
     **********************************************************************************************/
    public static void main(String[] args) {
       new VoiceCapture();
    }
    
    /**********************************************************************************************
     * Method used to setup a connection with client's mirophone and then start capturing voice 
     * data.  Data is written to a ByteArrayOutputStream to be potenitally transmitted to 
     * other client. 
     **********************************************************************************************/
    private void captureAudio(){
    try{
      //Get and display a list of available mixers.
      Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
      
      System.out.println("Available mixers:");
      for(int cnt = 0; cnt < mixerInfo.length; cnt++){
      	System.out.println(mixerInfo[cnt].getName());
      }

      //set everything set up for capture
      audioFormat = getAudioFormat();

      DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

      //Select one of the available mixers.
      Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
      
      //Get a TargetDataLine on the selected mixer.
      targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
      
      //Prepare the line for use.
      targetDataLine.open(audioFormat);
      targetDataLine.start();

      //Create a thread to capture the microphone
      Thread captureThread = new CaptureThread();
      captureThread.start();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
    
  /*********************************************************************************************
   * Playback captured audio - maybe really only for testing purposes now, but could be
   * modified later to maybe play audio from a transmitted source placed in a simular buffer
   *********************************************************************************************/
   private void playRecorded() {
        try{
      //Get everything set up for playback.
      //Get the previously-saved data into a byte array object.
      byte audioData[] = byteArrayOutputStream.toByteArray();
      
      //Get an input stream on the byte array containing the data
      InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
      AudioFormat audioFormat = getAudioFormat();
      
      audioInputStream = new AudioInputStream(byteArrayInputStream, 
                            audioFormat, audioData.length/audioFormat.
                            getFrameSize());
      DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
      sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
      sourceDataLine.open(audioFormat);
      sourceDataLine.start();

      //Create a thread to play back the data 
      Thread playThread = new PlayThread();
      playThread.start();
      
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  /*********************************************************************************************
   * Setup the type of audio that will be captured by the mixer and dataline. 
   **********************************************************************************************/
  private AudioFormat getAudioFormat(){
    
    //How many snapshots of sound pressure are taken per second (8000,11025,16000,22050,44100)
    float sampleRate = 8000.0F;
        
    //Number of bits used to store each snapshot (8 or 16)
    int sampleSizeInBits = 16;
        
    //1 for mono,2 for stero sound
    int channels = 2;
        
    //true,false
    boolean signed = true;
        
    //true,false
    boolean bigEndian = false;
        
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
  }
  /*********************************************************************************************
   * Class used to create a thread that will capture audio
   **********************************************************************************************/
  class CaptureThread extends Thread{
  //An arbitrary-size temporary holding buffer
  byte tempBuffer[] = new byte[10000];
  public void run(){
    byteArrayOutputStream = new ByteArrayOutputStream();
    stopCapture = false;
    
    try{
      while(!stopCapture){
          
        //Read data from the internal buffer of the data line.
        int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
        
        if(cnt > 0){
          
          //Save data in output stream object.
          byteArrayOutputStream.write(tempBuffer, 0, cnt);
        }
    }
      byteArrayOutputStream.close();
    }catch (Exception e) {
      System.out.println(e);
    }
  }
}
/*************************************************************************************************
 * Class which plays back audio captured by the captureThread
**************************************************************************************************/
class PlayThread extends Thread{
  byte tempBuffer[] = new byte[10000];

  public void run(){
    try{
      int cnt;
      //Keep looping until the input read method
      // returns -1 for empty stream.
      while((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1){
            if(cnt > 0){
                //Write data to the buffer of the data line
                sourceDataLine.write(tempBuffer,0,cnt);
            }
      }
      //Block and wait for buffer of the data line to empty.
      sourceDataLine.drain();
      sourceDataLine.close();
      
    }catch (Exception e) {
      System.out.println(e);
    }
  }
}
    /********************************************************************************************
     * Action Preformed Method - only temp 
     ********************************************************************************************
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == captureBtn) {
            captureAudio();
        }
        else if(e.getSource() == stopBtn) {
            //break out of capture while loop
            stopCapture = true;
        }
        else if(e.getSource() == playBtn) {
            playRecorded();
        }
    }
    **/
}
