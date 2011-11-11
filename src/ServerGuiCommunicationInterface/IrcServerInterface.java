package ServerGuiCommunicationInterface;

public interface IrcServerInterface extends Runnable{
	
	public void sendText(String text);
	public void sentTextToChannel(String channel, String message);
	public void sendCommandMessage(String username, String message);
	public void setTextStyle(TextStyle style);
	public void setTextReceiver(IrcGuiInterface connection);
	public void openConnection(String ip, int port, String nickname, String realname );
	public void openVideoConnection(String username, int port, Boolean firstRequest);
	public void openAudioConnection(String username, int port);

	

}
