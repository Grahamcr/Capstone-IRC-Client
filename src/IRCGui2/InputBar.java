package IRCGui2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputBar extends JPanel {
	
	private JTextField inputField;
	private JButton sendButton;
	
	public InputBar() {
		setLayout(new BorderLayout());
		
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
		
		
		sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sendText();
			}
		});
		
		add(inputField, BorderLayout.CENTER);
		add(sendButton, BorderLayout.EAST);
		
	}
	
	private void sendText() {
		String text = inputField.getText();
		if (text.length() > 0) {
			System.out.println(text);
			inputField.setText("");
		}
	}
}
