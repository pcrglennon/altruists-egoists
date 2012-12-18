import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

/**
 * This is the key GUI component: contains the visual representation of the community
 * and the navigation buttons to switch between generations
 */

public class PopulationPanel extends JPanel implements ActionListener {

    //Variables for the actual game, running in the background
    private Circle gameRunner;
    private LinkedList<Agent> community;
    private int curGeneration;
    //Necessary for animation - 
    private boolean hasRun;
    
    private JLabel curGenLabel;

    //Buttons and textfields to control moving between generations
    private JPanel buttonPanel;
    private JButton prevGenB;
    private JButton nextGenB;
    private JButton firstGenB;
    private JButton lastGenB;
    private JButton gotoGenB;
    private NumericTextField gotoGenTF;
    private JButton runB;
    //Animation button and textfield to control the animation speed
    private JButton animateB;
    private NumericTextField animateSpeedTF;

    //Contains VisualAgents - JButtons that represent an agent
    private ArrayList<VisualAgent> visualAgents;
    //Contains the "boundaries" of each VisualAgent, so that they may be
    //placed in an absolute position
    private ArrayList<Rectangle> popBounds;

    //400 by 400 square
    private int[] dimensions = Config.POP_CIRCLE_DIMENSIONS;
    private int circleOffset = 20;
    private int agentOffset = 10;
    //VisualAgent size
    private int agentSize = 20;

    //Circle stuff - radius, and (x,y) of center point
    private int rad = (dimensions[2] - dimensions[0])/2;
    private int centerX = (dimensions[2] - dimensions[0])/2;
    private int centerY = (dimensions[3] - dimensions[1])/2;

    /*
     * Constructor - initializes the button panel, the game itself, and calls the
     * layoutPopulation() method
     */
    public PopulationPanel(double[] configInfo) {
	super();

	gameRunner = new Circle();
	hasRun = false;
	//This means each component will be added w/ ABSOLUTE position (actual xy coords)
	setLayout(null);
	setPreferredSize(Config.POP_PANEL_PREF_SIZE);

	setupButtonPanel();

	popBounds = new ArrayList<Rectangle>(Config.DEF_POP_SIZE);
	visualAgents = new ArrayList<VisualAgent>(Config.DEF_POP_SIZE);

	gameRunner.gridInitialize((int)configInfo[0], configInfo[1], (int)configInfo[2], (int)configInfo[3], (int)configInfo[4], (int)configInfo[5]);
	community = gameRunner.getCommunity();
	
	layoutPopulation();
    }

    /**
     * Runs all generations, and updates the visual agents
     */
    public void runEpoch(boolean updateGameLog) {
	gameRunner.runEpoch();
	hasRun = true;
	curGeneration = gameRunner.getCurGeneration() - 1;
	String finalGenPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(finalGenPersonalities);

	if(updateGameLog) {
	    MainWindow mainWindow = (MainWindow)getTopLevelAncestor();
	    mainWindow.updateGameLogPanel(gameRunner.getFileString());
	}

	updateButtonPanel();
    }

    /**
     * Animate to the end of the epoch, starting from the current generation
     *
     * If the epoch has not been run yet, it runs it first
     */

    private void animate() {
	if(!hasRun) {
	    runEpoch(false);
	    firstGeneration();
	}
	int animSpeed = 1000;
	//Get the new animation speed from the textbox
	try {
	    animSpeed = Integer.parseInt(animateSpeedTF.getText());
	} catch(Exception e) {
	    
	}
	//Timer that controls the animation
	Timer t = new Timer(animSpeed, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(curGeneration < gameRunner.getNumGenerations() - 1) {
			nextGeneration();
		    } else { 
			//Animation is done - Update the game Log and Nav Buttons
			MainWindow mainWindow = (MainWindow)getTopLevelAncestor();
			mainWindow.updateGameLogPanel(gameRunner.getFileString());
			showNavItems();
			runB.setEnabled(false);
			((Timer)e.getSource()).stop();
		    }
		}
	    });
	t.start();
    }

    /**
     * Move to previous generation, and update visualagents and button panel
     */
    private void prevGeneration() {
	curGeneration--;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    /**
     * Move to next generation and update visualagents and button panel
     */
    private void nextGeneration() {
	curGeneration++;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    /**
     * Move to the first generation, and update visualagents and button panel
     */
    private void firstGeneration() {
	curGeneration = 0;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    /**
     * Move to the last generation, and update visualagents and button panel
     */
    private void lastGeneration() {
	curGeneration = gameRunner.getNumGenerations() - 1;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }


    /**
     * Move to a specific generation, and update visualagents and button panel
     */
    private void gotoGeneration() {
	try {
	    String genPersonalities = gameRunner.getGenerationPersonalities(Integer.parseInt(gotoGenTF.getText()));
	    curGeneration = gameRunner.getCurGeneration();
	    updateVisualAgents(genPersonalities);
	    updateButtonPanel();
	} catch (Exception e) { //If the textfield is empty, show a pop-up indicating this
	    JOptionPane.showMessageDialog(this, "Please specify a Generation");
	}
    }

    /**
     * Enable and disable the next/prev buttons based on the current generation
     *
     * I.e. if curGeneration is the last generation, disable the Next Gen button
     */
    private void updateButtonPanel() {
	prevGenB.setEnabled(true);
	nextGenB.setEnabled(true);
	if(curGeneration == 0) {
	    prevGenB.setEnabled(false);
	    curGenLabel.setText("Cur. Generation = " + curGeneration + " (First)");
	} else if(curGeneration >= gameRunner.getNumGenerations() - 1) {
	    nextGenB.setEnabled(false);
	    curGenLabel.setText("Cur. Generation = " + curGeneration + " (Last)");
	} else {
	    curGenLabel.setText("Cur. Generation = " + curGeneration);	    
	}
    }

    /**
     * Initialize the button panel
     */
    private void setupButtonPanel() {
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	Rectangle bpBounds = new Rectangle((dimensions[0] + 20), (dimensions[1] + dimensions[3] + 50), dimensions[3], 250);
	buttonPanel.setBounds(bpBounds);

	curGenLabel = new JLabel("Cur. Generation = " + curGeneration + " (First)");
	curGenLabel.setAlignmentX(CENTER_ALIGNMENT);

	nextGenB = new JButton("Next Gen");
	firstGenB = new JButton("First Gen");
	prevGenB = new JButton("Prev. Gen");
	lastGenB = new JButton("Last Gen");
	gotoGenB = new JButton("Go to Gen #");
	gotoGenTF = new NumericTextField(3);
	runB = new JButton("Run");
	runB.setAlignmentX(CENTER_ALIGNMENT);
	animateB = new JButton("Animate");
	JLabel animateSpeedLabel = new JLabel("Speed:");
	animateSpeedTF = new NumericTextField(4, "1000");
	JLabel millisLabel = new JLabel("(millis)");

	nextGenB.addActionListener(this);
	prevGenB.addActionListener(this);
	firstGenB.addActionListener(this);
	lastGenB.addActionListener(this);
	gotoGenB.addActionListener(this);
	runB.addActionListener(this);
	animateB.addActionListener(this);

	JPanel panelOne = new JPanel();
	panelOne.setLayout(new FlowLayout());
	panelOne.add(prevGenB);
	panelOne.add(nextGenB);

	JPanel panelTwo = new JPanel();
	panelTwo.setLayout(new FlowLayout());
	panelTwo.add(firstGenB);
	panelTwo.add(lastGenB);

	JPanel panelThree = new JPanel();
	panelThree.setLayout(new FlowLayout());
	panelThree.add(gotoGenB);
	panelThree.add(gotoGenTF);

	JPanel panelFour = new JPanel();
	panelFour.setLayout(new FlowLayout());
	panelFour.add(animateB);
	panelFour.add(animateSpeedLabel);
	panelFour.add(animateSpeedTF);
	panelFour.add(millisLabel);
	
	buttonPanel.add(Box.createVerticalStrut(15));
	buttonPanel.add(curGenLabel);
	buttonPanel.add(Box.createVerticalStrut(15));
	buttonPanel.add(panelOne);
	buttonPanel.add(panelTwo);
	buttonPanel.add(panelThree);
	buttonPanel.add(runB);
	buttonPanel.add(Box.createVerticalStrut(10));
	buttonPanel.add(panelFour);

	hideNavItems();
	
	add(buttonPanel);
    }

    private void showNavItems() {
	nextGenB.setVisible(true);
	prevGenB.setVisible(true);
	firstGenB.setVisible(true);
	lastGenB.setVisible(true);
	gotoGenB.setVisible(true);
	gotoGenTF.setVisible(true);
    }

    private void hideNavItems() {
	nextGenB.setVisible(false);
	prevGenB.setVisible(false);
	firstGenB.setVisible(false);
	lastGenB.setVisible(false);
	gotoGenB.setVisible(false);
	gotoGenTF.setVisible(false);
    }

    /**
     * Layout each individual along the circle
     */
    private void layoutPopulation() {
	popBounds.clear();
	visualAgents.clear();
	for(int i = 0; i < community.size(); i++) {
	    VisualAgent va = new VisualAgent(community.get(i).getPersonality(), i);
	    va.addActionListener(this);
	    visualAgents.add(va);
	}
	//Absolute positioning coords
	int n = community.size();
	/**
	 * Layout coords depend on the size of the community, as each agent must
	 * be evenly spaced
	 *
	 * I got the equation from this Yahoo answers page: 
	 * http://answers.yahoo.com/question/index?qid=20090905193658AAcXIQw
	 * (I know, not a credible source, but it does work!)
	 */
	for(int i = 0; i < n; i++) {
	    double cos = Math.cos(Math.toRadians(((double)i/n)*360));
	    double sin = Math.sin(Math.toRadians(((double)i/n)*360));
	    popBounds.add(new Rectangle((int)((cos*rad) + centerX)+agentOffset, (int)((sin*rad) + centerY)+agentOffset, agentSize, agentSize));
	}
	//Set each visual agent's coords to the coords set up in the previous loop
	int counter = 0;
	for(VisualAgent va: visualAgents) {
	    va.setBounds(popBounds.get(counter));
	    add(va);
	    counter++;
	}
    }

    /**
     * Update the color of each visual agent based on the given personality string
     */
    private void updateVisualAgents(String personalities) {
	for(int i = 0; i < visualAgents.size(); i++) {
	    visualAgents.get(i).updateColor(personalities.charAt(i));
	}
    }
    
    /**
     * Handle button clicks
     */
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() instanceof VisualAgent) {
	    VisualAgent va = (VisualAgent)e.getSource();
	    va.swapColor();
	    community.get(va.index).swapPersonality();
	} else if(e.getSource().equals(nextGenB)) {
	    nextGeneration();
	} else if(e.getSource().equals(prevGenB)) {
	    prevGeneration();
	} else if(e.getSource().equals(firstGenB)) {
	    firstGeneration();
	} else if(e.getSource().equals(lastGenB)) {
	    lastGeneration();
	} else if(e.getSource().equals(gotoGenB)) {
	    gotoGeneration();
	} else if(e.getSource().equals(runB)) {
	    runEpoch(true);
	    showNavItems();
	    runB.setEnabled(false);
	} else if(e.getSource().equals(animateB)) {
	    animate();
	}
    }

    /**
     * Draw the circle representing the links between the agents
     */
    @Override
	public void paintComponent(Graphics g) {
	//Draw a circle to fill the square!
	g.drawOval(dimensions[0] + circleOffset, dimensions[1] + circleOffset, dimensions[2], dimensions[3]);
    }
}

