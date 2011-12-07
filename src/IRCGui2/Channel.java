package IRCGui2;

import javax.swing.JButton;
import javax.swing.JEditorPane;

import ServerGuiCommunicationInterface.IrcChannel;

public class Channel extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3884219913826764238L;
	private IrcChannel channel;
	private JEditorPane editPane;
	
	public Channel(String s) {
		super(s);
	}
	
	public Channel(IrcChannel channel) {
		super(channel.getName());
		this.channel = channel;
		this.editPane  = new JEditorPane();
		editPane.setEditable(false);
		editPane.setText("You joined the channel " + channel.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Channel) {
			Channel chan = (Channel) o;
			return chan.getText().equals(this.getText());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}

	public IrcChannel getChannel() {
		return channel;
	}

	public JEditorPane getEditPane() {
		return editPane;
	}
	
	public void appendText(String s) {
		editPane.setText(editPane.getText() + "\n" + s);
	}
	
}
