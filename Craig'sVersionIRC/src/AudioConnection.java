 

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class AudioConnection{

	Boolean connectionOpened = false;
	private VoiceChat v;
	
	public Boolean getConnectionOpened() {
		return connectionOpened;
	}

	public void setConnectionOpened(Boolean connectionOpened) {
		this.connectionOpened = connectionOpened;
	}

	public void startAudioConnection(String ip, int port) {
		//startAudioConnection(ip, port, "");
		v = new VoiceChat(ip, port);
	}
	
	public void startAudioConnection(String ip, int port, String name) {
	    v = new VoiceChat(ip, port);
	    
		/**
	    try {
			Socket socket = new Socket(InetAddress.getByName(ip), port);
			
			receiver = new ClientReceiver(socket);
			// starts decoder and player
			receiver.start();	
			
			sender = new ClientSender(socket);
			sender.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		***/
	}
	
	public void waitForAudioConnection(int port) {
		/***
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			// waits for connection
			Socket clientSocket = serverSocket.accept();
			
			receiver = new ClientReceiver(clientSocket);
			// starts decoder and player
			receiver.start();	
			
			sender = new ClientSender(clientSocket);
			sender.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		**/
	}
	/***
	public void muteSpeaker(boolean mute) {
		receiver.getPlyer().setMuted(mute);
	}
	
	public void muteMicrophone(boolean mute) {
		sender.setMuted(mute);
	}
	*****/
	
}





















/**
public class AudioConnection{

	Boolean connectionOpened = false;
	private ClientReceiver receiver;
	private ClientSender sender;
	
	public Boolean getConnectionOpened() {
		return connectionOpened;
	}

	public void setConnectionOpened(Boolean connectionOpened) {
		this.connectionOpened = connectionOpened;
	}

	public void startAudioConnection(String ip, int port) {
		startAudioConnection(ip, port, "");
	}
	
	public void startAudioConnection(String ip, int port, String name) {
		try {
			Socket socket = new Socket(InetAddress.getByName(ip), port);
			
			receiver = new ClientReceiver(socket);
			// starts decoder and player
			receiver.start();	
			
			sender = new ClientSender(socket);
			sender.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForAudioConnection(int port) {
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			// waits for connection
			Socket clientSocket = serverSocket.accept();
			
			receiver = new ClientReceiver(clientSocket);
			// starts decoder and player
			receiver.start();	
			
			sender = new ClientSender(clientSocket);
			sender.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void muteSpeaker(boolean mute) {
		receiver.getPlyer().setMuted(mute);
	}
	
	public void muteMicrophone(boolean mute) {
		sender.setMuted(mute);
	}
	
	
}

**/
