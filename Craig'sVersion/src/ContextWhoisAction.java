 

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;



/**
 * @author Holger Rocks
 */
class ContextWhoisAction extends AbstractAction{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JList comp; 
    IrcServerInterface conn;
    public ContextWhoisAction(JList comp, IrcServerInterface con){ 
        super("send WHOIS"); 
        this.comp = comp; 
        this.conn = con;
    } 
 
    public void actionPerformed(ActionEvent e){ 
        conn.sendText("whois " + comp.getSelectedValue());
    } 
 
    public boolean isEnabled()
    { 
        return comp.isEnabled();
    } 
} 
 


