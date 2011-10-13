package ICGuiDummy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ServerGuiCommunicationInterface.GuiToConnectionInterface;
import ServerGuiCommunicationInterface.TextSenderInterface;
import ServerGuiCommunicationInterface.TextStyle;

public class IRCGuiDummyMain extends JFrame implements GuiToConnectionInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		

		JTextField 	chatInputBox;
		JTextArea 	chatMessageBox;
		JButton		submit;


		public IRCGuiDummyMain() {
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
			System.out.println("test");
			this.setBounds(10, 10, 420, 500);
			this.setVisible(true);
			System.out.println("test");
		}
		
		protected void initWindow() 
		{
			// Instanzieren:
			chatInputBox = new JTextField();
			chatMessageBox = new JTextArea();

			submit = new JButton("Senden");

			submit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					submitClicked();
				}

			});

			// Positionen festlegen
			chatInputBox.setBounds(5, 425,300, 25);
			chatMessageBox.setBounds(5,10,400,400);
			chatMessageBox.setEditable(false);

			
			submit.setBounds(310,425,100,30);

			// Elemente dem Fenster hinzufügen:
			this.getContentPane().add(chatInputBox);
			this.getContentPane().add(chatMessageBox);
			this.getContentPane().add(submit);


			this.pack();
		}

		public void submitClicked()
		{
			String text = chatInputBox.getText();
			
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
		public void addTextSender(TextSenderInterface sender) {
			// TODO Auto-generated method stub
			
		}

	
	}