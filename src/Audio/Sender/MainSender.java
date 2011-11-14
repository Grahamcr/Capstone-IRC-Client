package Audio.Sender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainSender {
	public static void main(String[] args) {

		try {
			Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 51234);
			ClientSender sender = new ClientSender(socket);
			sender.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
