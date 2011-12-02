package IRCGui2;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import ServerGuiCommunicationInterface.IrcServerInterface;

/**
 * @author Holger Rocks
 */
class ContextAudioAction extends AbstractAction{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JList comp; 
    IrcServerInterface conn;
    public ContextAudioAction(JList comp, IrcServerInterface con){ 
        super("Start Audio"); 
        this.comp = comp; 
        this.conn = con;
    } 
 
    public void actionPerformed(ActionEvent e){ 
    	
    	int port = 6669;
    	String user = comp.getSelectedValue().toString();
    	conn.openAudioConnection(user, port);
    } 
 
    public boolean isEnabled()
    { 
        return comp.isEnabled();
    } 
} 
 


