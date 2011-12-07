package IRCGui2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Audio.AudioConnection;
import IRCConnection.IRCConnectionMain;
import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfoInterface;
import de.fhh.CapstoneIRC.Video.VideoChatWindow;

public class GuiMain extends JFrame implements IrcGuiInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5722659678363567891L;
	private IrcServerInterface ircServer;
	private UserInfoInterface userInfo;
	private AudioConnection audioConn = new AudioConnection();
	
	private ChannelList channelList;
	private ChatWindow chatWindow;
	private JTextField inputField;
	
	
	private String progName = new String("GVIRC");
	private String ipAddr;
	private int portNumber;
	private String userName;
	
	public GuiMain() {
		init();
	}
	
	private void init() {
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
		
		
		chatWindow = new ChatWindow(ircServer);
		channelList = new ChannelList(chatWindow);
		
		
		add(channelList, BorderLayout.NORTH);
		add(chatWindow, BorderLayout.CENTER);
		
		createInputBar();
		
		
		
		// -------------------
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
			"File Menu. Options are related to the program's behavior.");
		
		JMenuItem connect = new JMenuItem("Connect");
		connect.getAccessibleContext().setAccessibleDescription(
			"Connect to a network.");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.getAccessibleContext().setAccessibleDescription(
			"Exit the program.");
		
		JMenuItem help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		help.getAccessibleContext().setAccessibleDescription(
			"Help Menu. Program help and version information.");
		
		JMenuItem cliHelp = new JMenuItem("Help");
		cliHelp.getAccessibleContext().setAccessibleDescription(
			"Help documentation.");
			
		JMenuItem about = new JMenuItem("About " + progName);
		about.getAccessibleContext().setAccessibleDescription(
			"Program version information.");
		
		menuBar.add(file);
		file.add(connect);
		file.add(exit);
		
		menuBar.add(help);
		help.add(cliHelp);
		help.add(about);
		
		setJMenuBar(menuBar);
		
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tempPort = new String("");
				ipAddr = JOptionPane.showInputDialog(
					"Enter the server name or IP");
				if (ipAddr == null) {
					return;
				}
				tempPort = JOptionPane.showInputDialog(
					"Enter the port number");
				if (tempPort == null) {
					return;
				}
				try {
					portNumber = Integer.parseInt(tempPort);
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
						"Not a number.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
				userName = JOptionPane.showInputDialog(
					"Enter your user name");
				if (userName == null) {
					return;
				}
				// TODO Auto-generated method stub
				openConnection();
			}
		});
		
		
	}
	
	private void createInputBar() {
		JPanel inputBar = new JPanel(new BorderLayout());
		
		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(600, 20));
		inputField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyReleased(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					sendText();				
			}
		});
		
		
		JButton sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sendText();
			}
		});
		
		inputBar.add(inputField, BorderLayout.CENTER);
		inputBar.add(sendButton, BorderLayout.EAST);
		
		add(inputBar, BorderLayout.SOUTH);
	}
	
	private void sendText() {
		String text = inputField.getText();
		
		if(text.indexOf('/') == 0) {
			ircServer.sendText(text.substring(1));
		} else {
			this.ircServer.sentTextToChannel(channelList.getActiveChannel().getChannel().getName(),text);
		}
		
		inputField.setText("");
	}
	

	@Override
	public void run() {
		setBounds(10, 10, 640, 480);
		setVisible(true);

	}

	@Override
	public void writeString(String name, String text) {
		writeString(ircServer.getServerName(), name, text);

	}

	@Override
	public void writeString(String channel, String name, String text) {
		if (channel.equals(userName)) {
			channel = name;
		}
		channelList.appendText(channel, name + ": " + text);
	}

	@Override
	public void setTextStyle(TextStyle style) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIrcServer(IrcServerInterface sender) {
		ircServer = sender;
		chatWindow.setServer(ircServer);
	}

	@Override
	public void addUserInfoInterface(UserInfoInterface info) {
		userInfo = info;
	}

	@Override
	public void openChannel(IrcChannel channel, boolean privChat) {
		channelList.addChannel(new Channel(channel));
	}

	@Override
	public void closeChannel(String channel) {
		channelList.removeChannel(channel);
	}

	@Override
	public void updateChannel() {
		chatWindow.updateUserList(channelList.getActiveChannel().getChannel().getUserList());
	}

	@Override
	public void openVideoConnection(String username, String ip, int port,
			Boolean startGui) {
		if( startGui == false)
		{
			 int response = JOptionPane.showConfirmDialog(null, username + " requests a video connection", "Do you want to start a video session with " + username +"?",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				    if (response == JOptionPane.YES_OPTION) 
				    {
				        ircServer.openVideoConnection(username, port, false);
				    }
				    else
				    {
				    	return;
				    }
		}
		
    	VideoChatWindow videoWin = new VideoChatWindow(ip, port, port);
		videoWin.start();

	}

	@Override
	public void openAudioConnection(String username, final String ip, final int port) {
		new Thread( new Runnable() {
			  public void run() {
				audioConn.startAudioConnection(ip, port);
			  };
			} ).start();

	}
	
	public void openConnection()
	{
		IrcServerInterface ircConnection;
		//ircConnection = new IRCConnectionDummyMain();
		ircConnection = new IRCConnectionMain();
		Thread t = new Thread(ircConnection);
		t.start();
		
		addIrcServer(ircConnection);
		ircConnection.setTextReceiver(this);
		
		ircConnection.openConnection(ipAddr, portNumber, userName, userName);
	}

}
