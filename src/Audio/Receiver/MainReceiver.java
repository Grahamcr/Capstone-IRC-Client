package Audio.Receiver;

public class MainReceiver {
	public static void main(String[] args) {
		ClientReceiver receiver = new ClientReceiver();
		receiver.start();
	}
}
