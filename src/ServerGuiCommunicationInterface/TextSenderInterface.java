package ServerGuiCommunicationInterface;

public interface TextSenderInterface extends Runnable{
	
	public void sendText(String text);
	public void setTextStyle(TextStyle style);
	public void setTextReceiver(GuiToConnectionInterface connection);

}
