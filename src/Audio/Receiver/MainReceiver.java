package Audio.Receiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainReceiver {
	public static void main(String[] args) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(51234);
			// waits for connection
			Socket clientSocket = serverSocket.accept();
			
			ClientReceiver reciever = new ClientReceiver(clientSocket);
			// starts decoder and player
			reciever.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
