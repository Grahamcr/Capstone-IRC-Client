package IRCGui;

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
        conn.sendText("DVC " + comp.getSelectedValue());
    } 
 
    public boolean isEnabled()
    { 
        return comp.isEnabled();
    } 
} 
 


