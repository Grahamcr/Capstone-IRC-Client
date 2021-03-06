import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Popup;

/***************************************************************************************************
 * Write a description of class VoiceClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 ****************************************************************************************************/
public class VoiceCapture extends JFrame {
  
    TargetDataLine targetDataLine;
       
    AudioFormat audioFormat;
    
    ByteArrayOutputStream byteArrayOutputStream;
    
    boolean stopCapture = false;
       
    AudioInputStream audioInputStream;
    
    SourceDataLine sourceDataLine;
    
    String address;
    
 //   JFrame f;
    
 //   JLabel message;
    
    /**Temp only**/
    JButton captureBtn;
    JButton stopBtn;
    JButton playBtn;
    
    
    /*********************************************************************************************
     * Constructor - used only at this point to set up temp GUI screen
     *********************************************************************************************/
    public VoiceCapture() {
        String input = null;
        while(input == null || input.equals("")) {
            input = JOptionPane.showInputDialog(null, "Please, Enter the IP Address To Chat With");
         }
         address = input.trim();
        setLayout(new FlowLayout());
        JButton captureBtn = new JButton("Chat");
        JButton stopBtn = new JButton("Stop");
   
        add(captureBtn);
//        add(stopBtn);
        setSize(100,100);
  //      f = new JFrame();
  //      f.setSize(100,100);        
 //       message = new JLabel("Looking for input line....");
  //      f.add(message);
      captureBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
//                 f.setVisible(true);
                captureAudio(0);
            }
       }
       );
       
      stopBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                stopCapture = true;
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
    private void captureAudio(int mInt){       
       
    try{
     // Thread.sleep(1000);
      //Get and display a list of available mixers.
      Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
      
     // System.out.println("Available mixers:");
     // for(int cnt = 0; cnt < mixerInfo.length; cnt++){
    //    System.out.println(mixerInfo[cnt].getName());
    //  }

      //set everything set up for capture provided by javax.sound
      audioFormat = getAudioFormat();

      DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

      //Select one of the available mixers provided by javax.sound
      Mixer mixer = AudioSystem.getMixer(mixerInfo[mInt]);
   //  System.out.println("Found a line that works! Good To Start Chat");
      //Get a TargetDataLine on the selected mixer.
      targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
      
      //Prepare the line for use.
      targetDataLine.open(audioFormat);
      targetDataLine.start();

      //Create a thread to capture the microphone
 //     message.setText("All Set Up - Lets Chat !");
 //     Thread.sleep(2000);
 //     f.setVisible(false);
      Thread captureThread = new CaptureThread();
      captureThread.start();
      playRecorded();
    } catch (IllegalArgumentException e) {
        captureAudio(mInt+1);
//      message.setText("That Line Didn't work....trying another");
      
    } catch(Exception e) {
//        message.setText("Found an error I don't know how to handle" + e);
    }
  }
    
  /*********************************************************************************************
   * Playback captured audio - maybe really only for testing purposes now, but could be
   * modified later to maybe play audio from a transmitted source placed in a simular buffer
   *********************************************************************************************/
   private void playRecorded() {
        try{
      //Get everything set up for playback.
    
      AudioFormat audioFormat = getAudioFormat();
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
   * Class used to create a thread that will capture audio and send it out to other user
   **********************************************************************************************/
  class CaptureThread extends Thread{
  //An buffer of 512 bytes as that is the most we can send in a UDP packet
  byte tempBuffer[] = new byte[512];
  public void run(){
    //byteArrayOutputStream = new ByteArrayOutputStream();
    stopCapture = false;
      try{
        DatagramSocket clientSocket = new DatagramSocket();
  
      while(!stopCapture){
          
        //Read data from the internal buffer of the data line.
        int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
        
        //Name or explicit IP Address - right now just send it back to me for testing purposes
        //InetAddress ipAddress = InetAddress.getByName("127.0.0.1");
        InetAddress ipAddress = InetAddress.getByName(address);

        //For now just use a random port number
        int port = 9876;
        
        if(cnt > 0){
          
          //If have data - send it !
          DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, ipAddress, port);
            
          clientSocket.send(sendPacket);
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

  public void run(){
    try{
        
        //Only listen for data coming in on a certain port number
        DatagramSocket serverSocket = new DatagramSocket(9876);
        
        //byte array to receive data into
        byte[] receiveData = new byte [1024];
        
        //Buffer to hold received data until we have received 10 packets
        byte[] buffer = new byte [5120];
        
        //Used to keep track of where we are in the buffer
        int placeHolder = 0;
        
        //Keep track of how many packets we have received
        int count = 0;
        
        while(!stopCapture) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            //read in data sent from the client
            serverSocket.receive(receivePacket);
            
            //Get the data byte array from the packet
            byte[] data = receivePacket.getData();
            
            //Add the most recently received data to that which we are already holding
            int i = 0;
            for(i = 0; i < receivePacket.getLength(); i++) {
                 buffer[placeHolder+i] = data[i];
             }
             
             //Update The Placebolder
             placeHolder = placeHolder + i;
            
                    
             //If we have enough packets send them 
            if(count == 9 ) {
                sourceDataLine.write(buffer,0, placeHolder);
                count = 0;
                placeHolder = 0;
             }
             //Otherwise increase the packet count
             else {
                 count++;
             }
            // sourceDataLine.drain();
           // sourceDataLine.close();
        }
      
    }catch (Exception e) {
      System.out.println(e);
    }
  }
}

}
