package Audio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Audio.Receiver.ClientReceiver;
import Audio.Sender.ClientSender;

public class AudioConnection {

	public void startAudioConnection(String ip, int port) {
		startAudioConnection(ip, port, "");
	}
	
	public void startAudioConnection(String ip, int port, String name) {
		try {
			Socket socket = new Socket(InetAddress.getByName(ip), port);
			
			ClientReceiver reciever = new ClientReceiver(socket);
			// starts decoder and player
			reciever.start();	
			
			ClientSender sender = new ClientSender(socket);
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
			
			ClientReceiver reciever = new ClientReceiver(clientSocket);
			// starts decoder and player
			reciever.start();	
			
			ClientSender sender = new ClientSender(clientSocket);
			sender.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
