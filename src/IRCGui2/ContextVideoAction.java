package IRCGui2;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import ServerGuiCommunicationInterface.IrcServerInterface;

/**
 * @author Holger Rocks
 */
class ContextVideoAction extends AbstractAction{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JList comp; 
    IrcServerInterface conn;
    public ContextVideoAction(JList comp, IrcServerInterface con){ 
        super("Start Video"); 
        this.comp = comp; 
        this.conn = con;
    } 
 
    public void actionPerformed(ActionEvent e){ 
    	
    	int port = 6668;
    	String user = comp.getSelectedValue().toString();
    	
        conn.openVideoConnection(user, port, true);
    } 
 
    public boolean isEnabled()
    { 
        return comp.isEnabled();
    } 
} 
 


