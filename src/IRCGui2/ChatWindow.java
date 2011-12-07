package IRCGui2;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import ServerGuiCommunicationInterface.ChannelUser;
import ServerGuiCommunicationInterface.ChannelUserList;
import ServerGuiCommunicationInterface.IrcServerInterface;

public class ChatWindow extends JSplitPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6090458829602760335L;

	private IrcServerInterface ircServer;
	
	private JEditorPane editPane;
	private JScrollPane scrollPane;
	private JList userList;
	private DefaultListModel listModel = new DefaultListModel();
	
	public ChatWindow() {
		this(null);
	}
	
	public ChatWindow(IrcServerInterface server) {
		ircServer = server;
		
		
		editPane = new JEditorPane();
		editPane.setPreferredSize(new Dimension(550, 480));
		editPane.setEditable(false);
		editPane.setText("start Window");
		
		userList = new JList(listModel);
		
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
		
		scrollPane = new JScrollPane(editPane);
		
		setOrientation(HORIZONTAL_SPLIT);
		setLeftComponent(scrollPane);
		setRightComponent(userList);
		setDividerLocation(.8);
	}


	public void setEditPane(JEditorPane editPane) {
		this.editPane = editPane;
		scrollPane.setViewportView(editPane);
	}


	public void setUserList(JList userList) {
		this.userList = userList;
	}
	
	public void updateUserList(ChannelUserList list) {
		listModel.removeAllElements();
		
		for (ChannelUser user : list.getUserListArray())
			listModel.addElement(user.getUser().getName());
	}
	
	public void setServer(IrcServerInterface server) {
		ircServer = server;
		
	}
	
}
