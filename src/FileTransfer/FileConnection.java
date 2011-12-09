package FileTransfer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FileConnection{

    Boolean connectionOpened = false;
    private DCC d;
    private DCCServer ds;

    public Boolean getConnectionOpened() {
        return connectionOpened;
    }

    public void setConnectionOpened(Boolean connectionOpened) {
        this.connectionOpened = connectionOpened;
    }

    public void startFileConnection(String ip, int port) {
        String displayText = "Would You Like To Send A File To The Selected User " +
            "Or Recieve A File From Them?";

        //create pop-up and get user's response
        JOptionPane pane = new JOptionPane(displayText);
        Object[] options = new String[] { "Send", "Receive" };
        pane.setOptions(options);
        JDialog dialog = pane.createDialog(new JFrame(), "Dilaog");
        dialog.setVisible(true);
        Object obj = pane.getValue(); 
        int result = -1;
        for (int k = 0; k < options.length; k++)
            if (options[k].equals(obj))
                result = k;

        //User Wishes To Send A File
        if(result == 0) {
            System.out.println("You Choose To Send A File");
            d = new DCC(ip, port);
        }
        //User Chooses To Receive A File
        else {
            System.out.println("You have choosen to receive a file");
            ds = new DCCServer(ip, port);
        }
    }

    public void startFileConnection(String ip, int port, String name) {

        String displayText = "Would You Like To Send A File To The Selected User " +
            "Or Recieve A File From Them?";

        //create pop-up and get user's response
        JOptionPane pane = new JOptionPane(displayText);
        Object[] options = new String[] { "Send", "Receive" };
        pane.setOptions(options);
        JDialog dialog = pane.createDialog(new JFrame(), "Dilaog");
        dialog.setVisible(true);
        Object obj = pane.getValue(); 
        int result = -1;
        for (int k = 0; k < options.length; k++)
            if (options[k].equals(obj))
                result = k;

        //User Wishes To Send A File
        if(result == 0) {
            System.out.println("You Choose To Send A File");
            DCC d = new DCC(ip, port);
        }
        //User Chooses To Receive A File
        else {
            System.out.println("You have choosen to receive a file");
            DCCServer ds = new DCCServer(ip, port);
        }
        /**
        try {
        Socket socket = new Socket(InetAddress.getByName(ip), port);

        receiver = new ClientReceiver(socket);
        // starts decoder and player
        receiver.start();	

        sender = new ClientSender(socket);
        sender.start();
        } catch (UnknownHostException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

         ***/
    }

    public void waitForAudioConnection(int port) {
    }

}

