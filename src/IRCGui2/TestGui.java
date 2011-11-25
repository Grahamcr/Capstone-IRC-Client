package IRCGui2;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class TestGui extends JFrame {
	public TestGui() {
		Container pane = getContentPane();

		ChatWindow chat = new ChatWindow();
		
		ChannelList channelList = new ChannelList();
		channelList.addChannel(new Channel("erster Channel"));
		channelList.addChannel(new Channel("zweiter Channel"));
		channelList.addChannel(new Channel("dritter Channel"));
		channelList.addChannel(new Channel("vierter Channel"));
		channelList.addChannel(new Channel("fuenfer Channel"));
		channelList.addChannel(new Channel("sechster Channel"));
		channelList.addChannel(new Channel("siebter Channel"));
		
		pane.add(channelList, BorderLayout.NORTH);
		pane.add(chat, BorderLayout.CENTER);
		pane.add(new InputBar(), BorderLayout.SOUTH);
		
		setBounds(10, 10, 640, 480);
		setVisible(true);
		
	}
}
