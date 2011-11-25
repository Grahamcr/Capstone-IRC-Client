package IRCGui2;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class ChatWindow extends JSplitPane {
	
	private JEditorPane editPane;
	private JScrollPane scrollPane;
	
	public ChatWindow() {
		editPane = new JEditorPane();
		editPane.setPreferredSize(new Dimension(550, 480));
		editPane.setEditable(false);
		editPane.setText("hallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\nhallo\n");
		
		scrollPane = new JScrollPane(editPane);
		
		setOrientation(HORIZONTAL_SPLIT);
		setLeftComponent(scrollPane);
		setRightComponent(new JList());
		setDividerLocation(.8);
	}
}
