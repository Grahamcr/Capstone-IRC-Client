package ServerGuiCommunicationInterface;



public interface IrcGuiInterface extends Runnable {

	public void writeString(String name, String text);
	public void setTextStyle(TextStyle style);
	public void addTextSender(IrcServerInterface sender);
}
