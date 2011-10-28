package Audio.Sender;

import java.io.IOException;

public class MainSender {
	public static void main(String[] args) {
		ClientSender sender = new ClientSender();
		try {
			sender.addConnection("127.0.0.1");
			sender.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
