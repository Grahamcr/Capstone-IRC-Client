package IRCGui2;

import java.awt.BorderLayout;
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
	
	public ChannelList() {
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
				System.out.println(scroll.getHorizontalScrollBar().getValue() + " - " + scroll.getHorizontalScrollBar().getMinimum());
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
				System.out.println(scroll.getHorizontalScrollBar().getValue() + " - " + scroll.getHorizontalScrollBar().getMaximum() + " - " + scroll.getHorizontalScrollBar().getModel().getExtent());
			}
		});
		
		
		add(left,BorderLayout.WEST);
		add(scroll, BorderLayout.CENTER);
		add(right, BorderLayout.EAST);
	}
	
	public void addChannel(Channel c) {
		channelPanel.add(c);
		channelPanel.revalidate();
	}
}
