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
public class MainWindow extends JFrame implements ActionListener {

    //The main content panel
    Container panel;

    PopulationPanel populationPanel;
    
    //The panel containing the buttons
    JPanel buttonPanel;
    JButton startB;

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
	
	setupButtonPanel();
	//Add buttonPanel to the left side of the window
	panel.add(buttonPanel, BorderLayout.LINE_START);

	populationPanel = new PopulationPanel();
	//add populationPanel to the center of the window
	panel.add(populationPanel, BorderLayout.CENTER);

	gameLogPanel = new GameLogPanel();
	gameLogPanel.setPreferredSize(Config.SIDE_PANEL_PREF_SIZE);
	//Add gameLogPanel to the right side of the window
	panel.add(gameLogPanel, BorderLayout.LINE_END);

	//Try to allot each component its "preferred size"
	pack();
	//Actually display the window
	setVisible(true);
    }

    private void setupButtonPanel() {
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	buttonPanel.setPreferredSize(Config.SIDE_PANEL_PREF_SIZE);
	
	startB = new JButton("Start");
	startB.addActionListener(this);
	buttonPanel.add(startB);
    }
    
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == startB) {
	    //"Start Game" code goes here
	    System.out.println("Start Clicked");
	}
    }

    public static void main(String[] args) {
	MainWindow mw = new MainWindow();
    }

}