package FileTransfer; 

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import ServerGuiCommunicationInterface.IrcServerInterface;



/**
 * @author Holger Rocks
 */
class ContextFileAction extends AbstractAction{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JList comp; 
    IrcServerInterface conn;
    public ContextFileAction(JList comp, IrcServerInterface con){ 
        super("Start File Transfer"); 
        this.comp = comp; 
        this.conn = con;
    } 
 
    public void actionPerformed(ActionEvent e){ 
    	
    	int port = 7000;
    	String user = comp.getSelectedValue().toString();
    	conn.openFileConnection
    	(user, port);
    } 
 
    public boolean isEnabled()
    { 
        return comp.isEnabled();
    } 
} 
 


