package ServerGuiCommunicationInterface;

public interface IrcServerInterface extends Runnable{
	
	public void sendText(String text);
	public void setTextStyle(TextStyle style);
	public void setTextReceiver(IrcGuiInterface connection);

}
