import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * New Window which contains a image on how to use the simulation
 */

public class HelpWindow extends JFrame {

    Container panel;
    
    public HelpWindow() {
	super();

	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createFrame();
		}
	    });
    }

    private void createFrame() {
	setSize(Config.HELP_WINDOW_SIZE);
	setTitle("Help");
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	panel = getContentPane();

	try {
	    BufferedImage helpImage;
	    //panel.add(new JScrollPane(new JLabel(new ImageIcon(helpImage)), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

}