/*
Errors I've found (donleyj):
/msg doesn't work
/quit throws an exception
*/

package IRCGui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.fhh.CapstoneIRC.Video.VideoChatWindow;

import IRCConnection.IRCConnectionMain;
import ServerGuiCommunicationInterface.ChannelUser;
import ServerGuiCommunicationInterface.IrcChannel;
import ServerGuiCommunicationInterface.IrcGuiInterface;
import ServerGuiCommunicationInterface.IrcServerInterface;
import ServerGuiCommunicationInterface.TextStyle;
import ServerGuiCommunicationInterface.UserInfoInterface;

public class IRCGuiMain extends JFrame implements IrcGuiInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		private UserInfoInterface userInfo = null;
		private DefaultListModel listModel = new DefaultListModel();
		private IrcServerInterface ircServer = null;
		
		private String ipAddr = new String("");
		private int portNumber = 0;
		private String userName = new String("");
		
		// the program name; feel free to change it
		private final String progName = new String("GVIRC");
		// version; same as above
		private final String progVersion = new String("no_version");
		 
		IrcChannel currentChannel;
		
		JButton		openConnectionButton;
		TextField 	chatInputBox;
		JTextArea 	chatMessageBox;
		JButton		submit;
		JList 	userList;
		JMenuBar	menuBar;
		JMenu	file, help;
		JMenuItem	connect, exit, cliHelp, about;
		JFrame	frame;

		//JLabel ipLabel;
		//JLabel portLabel;
		//JLabel chanLabel;
		//JLabel userNameLabel;
		
		
		

		public IRCGuiMain() 
		{
			//Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ContextMenue()); 
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
			
			this.setBounds(10, 10, 640, 480);
			this.setVisible(true);	
			
		}
		
		protected void initWindow() 
		{
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			chatInputBox = new TextField();
			chatMessageBox = new JTextArea();
			openConnectionButton = new JButton();
			userList = new JList(listModel);
			

			submit = new JButton("Senden");
			submit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					submitClicked();
				}

			});
			
			
			userList.addMouseListener( new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					if ( SwingUtilities.isRightMouseButton(e) )
					{	
						int index = userList.locationToIndex(e.getPoint());
				        userList.setSelectedIndex(index);
						
						
						final JPopupMenu menu = new JPopupMenu(); 
						menu.add(new ContextWhoisAction(userList, ircServer)); 
						menu.add(new ContextVideoAction(userList, ircServer)); 
						//menu.addSeparator(); 
					 
	 
						Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), userList);
						menu.show(userList, pt.x, pt.y);
					}
				}
	 
				public void mouseReleased(MouseEvent e)
				{
					if ( SwingUtilities.isRightMouseButton(e) )
					{
						JList list = (JList)e.getSource();
						System.out.println(list.getSelectedValue() + " selected");
					}
				}
			});
	 
			//getContentPane().add( new JScrollPane(userList) );
			
			
			openConnectionButton.addActionListener(new ActionListener() {
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
			
			
			
		
			
			chatInputBox.addKeyListener(new KeyAdapter() {
		          public void keyPressed(KeyEvent e) {
			            int key = e.getKeyCode();
			            if (key == KeyEvent.VK_ENTER) {
			            	submitClicked();
			               }
			            }
			          });
			
			
		
			menuBar = new JMenuBar();
			
			file = new JMenu("File");
			file.setMnemonic(KeyEvent.VK_F);
			file.getAccessibleContext().setAccessibleDescription(
				"File Menu. Options are related to the program's behavior.");
			
			connect = new JMenuItem("Connect");
			connect.getAccessibleContext().setAccessibleDescription(
				"Connect to a network.");
			
			exit = new JMenuItem("Exit");
			exit.getAccessibleContext().setAccessibleDescription(
				"Exit the program.");
			
			help = new JMenu("Help");
			help.setMnemonic(KeyEvent.VK_H);
			help.getAccessibleContext().setAccessibleDescription(
				"Help Menu. Program help and version information.");
			
			cliHelp = new JMenuItem("Help");
			cliHelp.getAccessibleContext().setAccessibleDescription(
				"Help documentation.");
				
			about = new JMenuItem("About " + progName);
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


			exit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					if(JOptionPane.showConfirmDialog(null,
						"Exit?",
						"Exit Confirmation",
						JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION) {
						dispose();
					}
				}

			});

			cliHelp.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(null,
						"Not implemented.", // body
						"Application Help", // title
						JOptionPane.WARNING_MESSAGE);
				}

			});

			about.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(frame,
						progName + ", version " + progVersion, // body
						"Version Information", // title
						JOptionPane.INFORMATION_MESSAGE);
				}

			});

			
			openConnectionButton.setBounds(5, 5, 120, 20);
			openConnectionButton.setText("Connect");
			openConnectionButton.getAccessibleContext().setAccessibleDescription(
				"Connect to a network.");

			// chat elements
			chatInputBox.setBounds(5, 400,500, 25);
			chatMessageBox.setBounds(5,30,500,365);
			chatMessageBox.setEditable(false);
			userList.setBounds(510, 30, 120, 365);
			submit.setBounds(510,400,120,30);
			
			

			// Elemente dem Fenster hinzuf?gen:
			this.getContentPane().add(chatInputBox);
			this.getContentPane().add(chatMessageBox);
			this.getContentPane().add(submit);
			this.getContentPane().add(userList);
			this.getContentPane().add(openConnectionButton);

			this.pack();
		
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
			
			for( ChannelUser u: channel.getUserList().getUserListArray())
			{
				listModel.addElement(u.getUser().getName());
			}	
		}

		@Override
		public void closeChannel(String channel) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateChannel() {
						
			listModel.removeAllElements();
			
			
			for( ChannelUser u: currentChannel.getUserList().getUserListArray())
			{
				listModel.addElement(u.getUser().getName());
			}	
		}

		@Override
		public void addUserInfoInterface(UserInfoInterface info) {
			userInfo = info;
			
		}

		@Override
		public void openVideoConnection(String username, String ip, int port) {
			// TODO Auto-generated method stub
			
			VideoChatWindow videoWin = new VideoChatWindow(ip, port, port);
			videoWin.start();
		}
		
		/**

		* @param x x-Position
		* @param y y-Position
		* @param width Breite in Zellen
		* @param height Hšhe in Zellen
		* @param weightx Gewicht
		* @param weighty Gewicht
		* @param cont Container
		* @param comp Hinzuzufugende Komponente
		* @param insets Abstaende rund um die Komponente
		*/
		private static void addComponent(int x, int y,
		int width, int height,
		double weightx, double weighty,
		Container cont, Component comp,
		Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x; gbc.gridy = y;
		gbc.gridwidth = width; gbc.gridheight = height;
		gbc.weightx = weightx; gbc.weighty = weighty;
		gbc.insets= insets;
		cont.add(comp, gbc);
		}
	}
