/*
Errors I've found (donleyj):
/msg doesn't work
/quit throws an exception
*/

 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.*;

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
        
        JButton     openConnectionButton;
        TextField   chatInputBox;
        JTextArea   chatMessageBox;
        JButton     submit;
        JList   userList;
        JFrame  frame;

        //JLabel ipLabel;
        //JLabel portLabel;
            //JLabel chanLabel;
        //JLabel userNameLabel;
        
        
        

        public IRCGuiMain() 
        {
            //Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ContextMenue()); 
            this.setTitle("GVIRC");
            setSize(400, 450);
            Container content = this.getContentPane();
            content.setBackground(Color.black);
            content.setLayout(new BorderLayout());
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
            

            submit = new JButton("Send!");
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
                        menu.add(new ContextAudioAction(userList, ircServer)); 
                        menu.add(new ContextFileAction(userList, ircServer));
                        
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
                        "Enter the server name or IP", "stockholm.se.quakenet.org");
                    if (ipAddr == null) {
                        return;
                    }
                    tempPort = JOptionPane.showInputDialog(
                        "Enter the port number", "6667");
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
                    try {
                        userName = JOptionPane.showInputDialog(
                            "Enter your user name", InetAddress.getLocalHost().getHostName().substring(0, 7));
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
            
            
        
           
            
            


            

            
            openConnectionButton.setBounds(5, 5, 120, 20);
            openConnectionButton.setText("Connect");
            openConnectionButton.getAccessibleContext().setAccessibleDescription(
                "Connect to a network.");

            /** chat elements
            chatInputBox.setBounds(5, 400,500, 25);
            chatMessageBox.setBounds(5,30,500,365);
            chatMessageBox.setEditable(false);
            userList.setBounds(510, 30, 120, 365);
            submit.setBounds(510,400,120,30);
            **/
            
            openConnectionButton.setPreferredSize(new Dimension(100, 40));
            chatInputBox.setPreferredSize(new Dimension(500, 60));
            userList.setPreferredSize(new Dimension(125, 500));
            
            Border userListBorder = BorderFactory.createLineBorder(new Color(0, 53, 90), 3);            
            userList.setBorder(userListBorder);
            
            Border chatMessageBoxBorder = BorderFactory.createLineBorder(new Color(0, 53, 90), 3);
            chatMessageBox.setBorder(chatMessageBoxBorder);
            
            //Bottom Panel for the input box and submit buttons
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(chatInputBox);
            bottomPanel.add(submit);
            bottomPanel.setBackground(new Color(0, 53, 90));
            submit.setBackground(new Color(241, 238, 239));
            submit.setForeground(new Color(0, 53, 130));
            
            //Top Panel For The Connect Button
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout());
            topPanel.add(BorderLayout.WEST, openConnectionButton);
            
            ImageIcon gvIcon = new ImageIcon("GV_Logo.jpg");
            JButton gvLogo = new JButton();
            gvLogo.setPreferredSize(new Dimension(100, 40));
            gvLogo.setBorder(chatMessageBoxBorder);
            gvLogo.setIcon(gvIcon);
            topPanel.add(BorderLayout.EAST, gvLogo);
            topPanel.setBackground(new Color(0, 53, 90));
            
            openConnectionButton.setBackground(new Color(241, 238, 239));
            openConnectionButton.setForeground(new Color(0, 53, 130));
            
            SwingLink sw = new SwingLink("Visit Developer's Site", "http://www.wix.com/grahamcrgvsu/capstone");
            topPanel.add(BorderLayout.CENTER, sw);
            
            // Add Componets To JFrame
            this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
            this.getContentPane().add(chatMessageBox, BorderLayout.CENTER);
            this.getContentPane().add(userList, BorderLayout.EAST);
            this.getContentPane().add(topPanel, BorderLayout.NORTH);
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
        public void openVideoConnection(String username, String ip, int port, Boolean startGui) {
            // TODO Auto-generated method stub
            
            
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
            
            //VideoChatWindow videoWin = new VideoChatWindow(ip, port, port);
            //videoWin.start();
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
        Insets insets) 
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = x; gbc.gridy = y;
            gbc.gridwidth = width; gbc.gridheight = height;
            gbc.weightx = weightx; gbc.weighty = weighty;
            gbc.insets= insets;
            cont.add(comp, gbc);
        }

        AudioConnection audioConn = new AudioConnection();
        @Override
        public void openAudioConnection(String username, final String ip, final int port) {
            // TODO Auto-generated method stub
            
            
            new Thread( new Runnable() {
              public void run() {
                audioConn.startAudioConnection(ip, port);
              };
            } ).start();    
            
        }
        
        
        FileConnection filec = new FileConnection();
        
        @Override
        public void openFileConnection(String username, final String ip, final int port) {
            // TODO Auto-generated method stub
            
            
            new Thread( new Runnable() {
              public void run() {
                filec.startFileConnection(ip, port);
              };
            } ).start();    
            
        }
        
    }
