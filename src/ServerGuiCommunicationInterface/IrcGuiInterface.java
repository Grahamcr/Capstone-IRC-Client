package ServerGuiCommunicationInterface;



public interface IrcGuiInterface extends Runnable {

	public void writeString(String name, String text);
	public void writeString(String channel, String name, String text);
	
	// currently not implemented
	public void setTextStyle(TextStyle style);
	
	public void addIrcServer(IrcServerInterface sender);
	public void addUserInfoInterface(UserInfoInterface info);
	
	public void openChannel(IrcChannel channel, boolean privChat);
	public void closeChannel(String channel);
	public void updateChannel();
	
	public void openVideoConnection(String username, String ip, int port, Boolean startGui);
	public void openAudioConnection(String username, String ip, int port);
}
