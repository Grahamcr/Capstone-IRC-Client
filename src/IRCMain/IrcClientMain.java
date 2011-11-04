package IRCMain;

import ICGuiDummy.IRCGuiMain;
import ServerGuiCommunicationInterface.IrcGuiInterface;


public class IrcClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		// This is the way, how to implement the Gui and the IRCConnection!!!
		
		
		
		// Create the IRCConnection and start operation
		//IrcServerInterface ircConnection = new IRCConnectionMain();
		//Thread t = new Thread(ircConnection);
		//t.start();
		
		
		
		
		// Create the GUI and start operation
		IrcGuiInterface gui = new IRCGuiMain();
		// Add the Connection to the GUI. You can send Text with this Interface
		//gui.addIrcServer(ircConnection);
		// Start working
		Thread t2 = new Thread(gui);
		t2.start();
		
		
		// Give the GUI to the IRCConnection.
		//ircConnection.setTextReceiver(gui);
		
		
		
		while(t2.isAlive())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}

}
