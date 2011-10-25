package ICGuiDummy;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;

import IRCConnection.IRCConnectionMain;
import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfo;
import ServerGuiCommunicationInterface.UserInfoInterface;

public class IRCGuiDummyMain extends JFrame implements IrcGuiInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


		
	
		private UserInfoInterface userInfo = null;
		private DefaultListModel listModel = new DefaultListModel();
		private IrcServerInterface ircServer = null;
		 
		IrcChannel currentChannel;
		TextField 	ipAdressBox;
		TextField 	portBox;
		//TextField 	channelBox;
		TextField	userNameBox;
		
		JButton		openConnectionButton;
		TextField 	chatInputBox;
		JTextArea 	chatMessageBox;
		JButton		submit;
		JList 	userList;

		JLabel ipLabel;
		JLabel portLabel;
		//JLabel chanLabel;
		JLabel userNameLabel;
		
		
		

		public IRCGuiDummyMain() 
		{
			this.getContentPane().setLayout(null);
			this.initWindow();
			this.addWindowListener(new WindowListener() {

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
			// TODO Auto-generated method stub
			
			this.setBounds(10, 10, 540, 580);
			this.setVisible(true);	
			
		}
		
		protected void initWindow() 
		{
				
			// Instanzieren:
			ipLabel = new JLabel();
			portLabel = new JLabel();
			//chanLabel = new JLabel();
			userNameLabel = new JLabel();
			
			userNameBox = new TextField();
			chatInputBox = new TextField();
			chatMessageBox = new JTextArea();
			ipAdressBox = new TextField();
			portBox = new TextField();
			//channelBox = new TextField();
			openConnectionButton = new JButton();
			userList = new JList(listModel);
			

			submit = new JButton("Senden");
			submit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					submitClicked();
				}

			});
			
			
			openConnectionButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					openConnection();
				}

			});
			
			
			
		
			
			chatInputBox.addKeyListener(new KeyAdapter() {
		          public void keyPressed(KeyEvent e) {
			            int key = e.getKeyCode();
			            if (key == KeyEvent.VK_ENTER) {
			            	submitClicked();
			               }
			            }
			          });
			
			
			


	        
			
			
			// connection elements
			ipLabel.setBounds(5,20, 80, 20);
			ipLabel.setText("IP Adress:");
			ipAdressBox.setBounds(95, 20, 130, 20);
			
			
			portLabel.setBounds(235,20, 40, 20);
			portLabel.setText("Port:");
			portBox.setBounds(285, 20, 70, 20);
			
			
			//chanLabel.setBounds(5,50, 80, 20);
			//chanLabel.setText("Channel:");
			//channelBox.setBounds(95, 50, 80, 20);
			
			userNameLabel.setBounds(5,50, 80, 20);
			userNameLabel.setText("Name:");
			userNameBox.setBounds(95, 50, 80, 20);
			
			openConnectionButton.setBounds(365, 50, 120, 20);
			openConnectionButton.setText("open Connection");
			
			
			

			ipAdressBox.setText("irc.quakenet.org");
			portBox.setText("6667");
			//channelBox.setText("#test");
			userNameBox.setText("Holger30");

			// chat elements
			chatInputBox.setBounds(5, 505,300, 25);
			chatMessageBox.setBounds(5,90,400,400);
			chatMessageBox.setEditable(false);
			userList.setBounds(430, 90, 100, 435);
			submit.setBounds(310,505,100,30);
			
			

			// Elemente dem Fenster hinzufügen:
			this.getContentPane().add(chatInputBox);
			this.getContentPane().add(chatMessageBox);
			this.getContentPane().add(submit);
			this.getContentPane().add(userList);

			this.getContentPane().add(ipLabel);
			this.getContentPane().add(ipAdressBox);
			
			this.getContentPane().add(portLabel);
			this.getContentPane().add(portBox);
			
			//this.getContentPane().add(chanLabel);
			//this.getContentPane().add(channelBox);
			
			this.getContentPane().add(openConnectionButton);
			this.getContentPane().add(userNameBox);
			this.getContentPane().add(userNameLabel);

			this.pack();
		
		}
		
		
		public void openConnection()
		{
			IrcServerInterface ircConnection;
			ircConnection = new IRCConnectionMain();
			Thread t = new Thread(ircConnection);
			t.start();
			
			addIrcServer(ircConnection);
			ircConnection.setTextReceiver(this);
			
			ircConnection.openConnection(ipAdressBox.getText(), Integer.parseInt(portBox.getText()), userNameBox.getText(), userNameBox.getText());
			
			
			//ircConnection.sendText("join "+ channelBox.getText());
		}

		public void submitClicked()
		{
			String text = chatInputBox.getText();
			if(text.indexOf('/') == 0)
			{
				System.out.println(text);
				ircServer.sendText(text.substring(1));
			}
			else
			{
				this.ircServer.sentTextToChannel(currentChannel.getName(),text);
			}
			
			chatInputBox.setText("");
		}

		@Override
		public void writeString(String name, String text) {
			// TODO Auto-generated method stub
			System.out.println(name + ": " + text);
			
			chatMessageBox.append(name + ": " + text + "\n");
		}

		@Override
		public void setTextStyle(TextStyle style) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addIrcServer(IrcServerInterface sender) {
			this.ircServer = sender;
			
		}

		@Override
		public void openChannel(IrcChannel channel, boolean privChat) 
		{	
			currentChannel = channel;
			listModel.removeAllElements();
			
			for( UserInfo u: channel.getUserList().getUserListArray())
			{
				listModel.addElement(u.getName());
			}	
		}

		@Override
		public void closeChannel(String channel) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateChannel() {
						
			listModel.removeAllElements();
			
			
			for( UserInfo u: currentChannel.getUserList().getUserListArray())
			{
				listModel.addElement(u.getName());
			}	
		}

		@Override
		public void addUserInfoInterface(UserInfoInterface info) {
			userInfo = info;
			
		}
	}