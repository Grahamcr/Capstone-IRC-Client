package ServerGuiCommunicationInterface;



public interface GuiToConnectionInterface extends Runnable {

	public void writeString(String name, String text);
	public void setTextStyle(TextStyle style);
	public void addTextSender(TextSenderInterface sender);
}
