 



public interface IrcGuiInterface extends Runnable {

	public void writeString(String name, String text);
	public void setTextStyle(TextStyle style);
	
	public void addIrcServer(IrcServerInterface sender);
	public void addUserInfoInterface(UserInfoInterface info);
	
	public void openChannel(IrcChannel channel, boolean privChat);
	public void closeChannel(String channel);
	public void updateChannel();
	
	public void openVideoConnection(String username, String ip, int port, Boolean startGui);
	public void openAudioConnection(String username, String ip, int port);
	public void openFileConnection(String username, String ip, int port);
}
