import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Some Swing Basics
 *
 * SO - BASICALLY everything is a subclass of the Component class
 *    And everything with the J prefix is a JComponent - specific to Swing
 * Components can be added to Containers - the most common Container is a JPanel
 * That's the easy part - the tough part is laying out each component in its JPanel
 * This is done with Layout Managers - BoxLayout, BorderLayout, GridLayout... etc.
 * BorderLayout is the default manager.  Descriptions of the others are online.
 * When you call JPanel.add(JComponent), you add it to the JPanel using that JPanel's
 * Layout Manager.
 * You can add JPanels TO other JPanels, using different layout managers, to accomplish
 * tricky layouts.
 * 
 * ActionListener Interface -
 * Listens for button clicks, and the method actionPerformed() will handle what happens
 * when a certain button is clicked (check the method for a syntax example).
 * A JButton must have an ActionListener in order to register a click.
 * i.e. JButton button = new JButton("Button");
 *      button.addActionListener(this);
 */

//JFrame is the "Window" class in Swing, so we extend it to use its functionality
public class MainWindow extends JFrame {

    //The main content panel
    Container panel;

    PopulationPanel populationPanel;
    PopConfigPanel popConfigPanel;
    
    //The panel containing the buttons
    JPanel buttonPanel;
    JButton quitB;

    GameLogPanel gameLogPanel;

    public MainWindow() {
	super();
	
	//Run the initialization stuff in a new thread
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI();
		}
	    });
    }

    private void createAndShowGUI() {
	//Set the frame size and title
	setSize(Config.MAIN_WINDOW_SIZE);
	setTitle("Altruists and Egoists");
	//Closing the Window will terminate the program
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel = getContentPane();

	popConfigPanel = new PopConfigPanel();
	add(popConfigPanel, BorderLayout.LINE_START);
	
	//add populationPanel to the center of the window
	populationPanel = new PopulationPanel(popConfigPanel.getConfigInfo());
	add(populationPanel, BorderLayout.CENTER);

	setupButtonPanel();
	//Add buttonPanel to the left side of the window
	panel.add(buttonPanel, BorderLayout.PAGE_END);

	gameLogPanel = new GameLogPanel();
	gameLogPanel.setPreferredSize(Config.SIDE_PANEL_PREF_SIZE);
	//Add gameLogPanel to the right side of the window
	panel.add(gameLogPanel, BorderLayout.LINE_END);

	//Try to allot each component its "preferred size"
	pack();
	//Actually display the window
	setVisible(true);
    }

    public void resetPopulationPanel() {
	String errors = popConfigPanel.checkInputs();
	if(errors.equals("")) {
	    panel.remove(populationPanel);
	    populationPanel = new PopulationPanel(popConfigPanel.getConfigInfo());
	    panel.add(populationPanel, BorderLayout.CENTER);
	    panel.revalidate();
	    panel.repaint();
	} else {
	    JOptionPane.showMessageDialog(this, errors);
	}
    }
    
    private void setupButtonPanel() {
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

	quitB = new JButton("Quit");

	quitB.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });
			      
	buttonPanel.add(quitB);
    }

    public static void main(String[] args) {
	MainWindow mw = new MainWindow();
    }

}