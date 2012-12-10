import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainWindow extends JFrame {

    //The main content panel
    private Container panel;

    private PopulationPanel populationPanel;

    private PopConfigPanel popConfigPanel;
    
    private JPanel buttonPanel;
    private JButton quitB;

    private GameLogPanel gameLogPanel;
    private int curIndex;

    public MainWindow() {
	super();
	
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI();
		}
	    });
    }

    private void createAndShowGUI() {
	setSize(Config.MAIN_WINDOW_SIZE);
	setTitle("Altruists and Egoists");
	//Closing the Window will terminate the program
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel = getContentPane();

	popConfigPanel = new PopConfigPanel();
	add(popConfigPanel, BorderLayout.LINE_START);
	
	populationPanel = new PopulationPanel(popConfigPanel.getConfigInfo());
	add(populationPanel, BorderLayout.CENTER);

	setupButtonPanel();
	panel.add(buttonPanel, BorderLayout.PAGE_END);

	gameLogPanel = new GameLogPanel();
	gameLogPanel.setPreferredSize(Config.SIDE_PANEL_PREF_SIZE);
	panel.add(gameLogPanel, BorderLayout.LINE_END);

	pack();

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

    public void updateGameLogPanel(String gameLogText) {
	gameLogPanel.updateGameLog(gameLogText);
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