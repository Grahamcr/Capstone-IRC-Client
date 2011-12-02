package IRCGui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChannelList extends JPanel {
	private JButton left, right;
	private JScrollPane scroll;
	private JPanel channelPanel;
	private Channel activeChannel;
	private ChatWindow chat;
	
	public ChannelList() {
		this(null);
	}
	
	
	public ChannelList(ChatWindow cWindow) {
		chat = cWindow;
		setLayout(new BorderLayout());
		
		channelPanel = new JPanel(new FlowLayout());
		scroll = new JScrollPane(channelPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		left = new JButton("<");
		left.setEnabled(false);
		left.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				scroll.getHorizontalScrollBar().setValue(scroll.getHorizontalScrollBar().getValue()-100);
				if (scroll.getHorizontalScrollBar().getValue() <= scroll.getHorizontalScrollBar().getMinimum()) {
					left.setEnabled(false);
					right.setEnabled(true);
				}
			}
		});
		
		right = new JButton(">");
		right.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				scroll.getHorizontalScrollBar().setValue(scroll.getHorizontalScrollBar().getValue()+100);
				if (scroll.getHorizontalScrollBar().getValue() + scroll.getHorizontalScrollBar().getModel().getExtent() >= scroll.getHorizontalScrollBar().getMaximum()) {
					right.setEnabled(false);
					left.setEnabled(true);
				}
			}
		});
		
		
		add(left,BorderLayout.WEST);
		add(scroll, BorderLayout.CENTER);
		add(right, BorderLayout.EAST);
	}
	
	private void setActive(Object o) {
		if (o instanceof Channel) {
			if (activeChannel != null)
				activeChannel.setForeground(Color.GRAY);
			activeChannel = (Channel) o;
			
			chat.setEditPane(activeChannel.getEditPane());
			chat.updateUserList(activeChannel.getChannel().getUserList());
			
			activeChannel.setForeground(Color.BLUE);
			
		}
		
		revalidate();
	}
	
	public void addChannel(Channel c) {
		c.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setActive(e.getSource());
				
			}
		});
		if (getChannel(c.getChannel().getName()) == null) {
			channelPanel.add(c);
			channelPanel.revalidate();
			setActive(c);
		}
		
	}
	
	public void removeChannel(Channel c) {
		channelPanel.remove(c);
		channelPanel.revalidate();
	}
	
	public Channel getChannel(String s) {
		for (Component c : channelPanel.getComponents()) {
			if (c instanceof Channel) {
				Channel chan = (Channel) c;
				if (chan.getChannel().getName().equals(s))
					return chan;
			}
		}
		return null;
	}
	
	public void appendText(String chan, String text) {
		Channel channel;
		if ((channel = getChannel(chan)) != null) {
			channel.appendText(text);
			if (!channel.equals(activeChannel))
				channel.setForeground(Color.RED);
		}
	}
	
	public Channel getActiveChannel() {
		return activeChannel;
	}
}
