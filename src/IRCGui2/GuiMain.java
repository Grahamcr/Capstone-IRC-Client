package IRCGui2;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfoInterface;

public class GuiMain extends JFrame implements IrcGuiInterface {
	
	private IrcServerInterface ircServer;
	private IrcChannel curChannel;
	
	public GuiMain() {
		addWindowListener(new WindowListener() {

			public void windowClosed(WindowEvent arg0) {
			}
			public void windowActivated(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
			}
		});
	}

	@Override
	public void run() {
		setBounds(10, 10, 640, 480);
		setVisible(true);

	}

	@Override
	public void writeString(String name, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeString(String channel, String name, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTextStyle(TextStyle style) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIrcServer(IrcServerInterface sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUserInfoInterface(UserInfoInterface info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openChannel(IrcChannel channel, boolean privChat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChannel(String channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateChannel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openVideoConnection(String username, String ip, int port,
			Boolean startGui) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAudioConnection(String username, String ip, int port) {
		// TODO Auto-generated method stub

	}

}
