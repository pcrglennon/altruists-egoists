import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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

    }

    public static void main(String[] args) {
	MainWindow mw = new MainWindow();
    }

}