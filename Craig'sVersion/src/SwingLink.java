import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
/************************************************************************
 * Class used to create hyperlinks that can be used to take the user
 * to the webpage that contains the offer we have displayed for them
 ************************************************************************/
public class SwingLink extends JLabel {
    private String text;
    private URI uri;

/******************************************************************
 * Constructor
 ******************************************************************/
    public SwingLink(String text, String uri){
        super();
        URI oURI;
        try {
            oURI = new URI(uri);
            setup(text,oURI);
        } catch (URISyntaxException e) {
             System.out.println("Error Setting Up URI");
        }
        
    }
/******************************************************************
 * Constructor
 ******************************************************************/
      public SwingLink(String text, URI uri){
        super();
        setup(text,uri);
    }
/****************************************************************
 * Setup the hyperlink to take the user to the proper website
 * when clicked on
 *****************************************************************/
    public void setup(String t, URI u){
        text = t;
        uri = u;
        setText(text);
        setToolTipText(uri.toString());
        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    open(uri);
                }
                public void mouseEntered(MouseEvent e) {
                    setText(text,false);
                }
                public void mouseExited(MouseEvent e) {
                    setText(text,true);
                }
        });
    }

    @Override
    public void setText(String text){
        setText(text,true);
    }

    public void setText(String text, boolean ul){
        String link = ul ? "<u>"+text+"</u>" : text;
        super.setText("<html><span style=\"color: #00FFFF;\">"+
                link+"</span></html>");
        this.text = text;
    }

    public String getRawText(){
        return text;
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                        desktop.browse(uri);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to launch the link, " +
                            "your computer is likely misconfigured.",
                            "Cannot Launch Link",JOptionPane.WARNING_MESSAGE);
                }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Java is not able to launch links on your computer.",
                    "Cannot Launch Link",JOptionPane.WARNING_MESSAGE);
        }
    }
}