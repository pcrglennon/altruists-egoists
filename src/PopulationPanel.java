import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

public class PopulationPanel extends JPanel implements ActionListener {

    private Circle gameRunner;
    private LinkedList<Agent> community;
    private int curGeneration;

    private JLabel curGenLabel;

    private JPanel buttonPanel;
    private JButton prevGenB;
    private JButton nextGenB;
    private JButton firstGenB;
    private JButton lastGenB;
    private JButton gotoGenB;
    private NumericTextField gotoGenTF;
    private JButton runB;

    //Contains VisualAgents - JButtons that represent an agent
    private ArrayList<VisualAgent> visualAgents;
    //Contains the "boundaries" of each VisualAgent, so that they may be
    //placed in an absolute position
    private ArrayList<Rectangle> popBounds;

    //400 by 400 square
    private int[] dimensions = {0, 0, 400, 400};
    private int circleOffset = 20;
    private int agentOffset = 10;
    //VisualAgent size
    private int agentSize = 20;

    //Circle stuff - radius, and (x,y) of center point
    private int rad = (dimensions[2] - dimensions[0])/2;
    private int centerX = (dimensions[2] - dimensions[0])/2;
    private int centerY = (dimensions[3] - dimensions[1])/2;

    public PopulationPanel(double[] configInfo) {
	super();

	gameRunner = new Circle();
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

    public void runEpoch() {
	gameRunner.runEpoch();
	curGeneration = gameRunner.getCurGeneration() - 1;
	String finalGenPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	System.out.println(finalGenPersonalities);
	updateVisualAgents(finalGenPersonalities);

	MainWindow mainWindow = (MainWindow)getTopLevelAncestor();
	mainWindow.updateGameLogPanel(gameRunner.getFileString());

	updateButtonPanel();
    }

    private void prevGeneration() {
	curGeneration--;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    private void nextGeneration() {
	curGeneration++;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    private void firstGeneration() {
	curGeneration = 0;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    private void lastGeneration() {
	curGeneration = gameRunner.getNumGenerations() - 1;
	String genPersonalities = gameRunner.getGenerationPersonalities(curGeneration);
	updateVisualAgents(genPersonalities);
	updateButtonPanel();
    }

    private void gotoGeneration() {
	try {
	    String genPersonalities = gameRunner.getGenerationPersonalities(Integer.parseInt(gotoGenTF.getText()));
	    curGeneration = gameRunner.getCurGeneration();
	    updateVisualAgents(genPersonalities);
	    updateButtonPanel();
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(this, "Please specify a Generation");
	}
    }

    private void updateButtonPanel() {
	curGenLabel.setText("Cur. Generation > " + curGeneration);
	prevGenB.setEnabled(true);
	nextGenB.setEnabled(true);
	if(curGeneration == 0) {
	    prevGenB.setEnabled(false);
	} else if(curGeneration >= gameRunner.getNumGenerations() - 1) {
	    nextGenB.setEnabled(false);
	}
    }

    private void setupButtonPanel() {
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    
	Rectangle bpBounds = new Rectangle((dimensions[0] + 20), (dimensions[1] + dimensions[3] + 50), dimensions[3], 250);
	buttonPanel.setBounds(bpBounds);

	curGenLabel = new JLabel("Cur. Generation > " + curGeneration);
	curGenLabel.setAlignmentX(CENTER_ALIGNMENT);

	nextGenB = new JButton("Next Gen");
	firstGenB = new JButton("First Gen");
	prevGenB = new JButton("Prev. Gen");
	lastGenB = new JButton("Last Gen");
	gotoGenB = new JButton("Go to Gen #");
	gotoGenTF = new NumericTextField(3);
	runB = new JButton("Run");
	runB.setAlignmentX(CENTER_ALIGNMENT);

	nextGenB.addActionListener(this);
	prevGenB.addActionListener(this);
	firstGenB.addActionListener(this);
	lastGenB.addActionListener(this);
	gotoGenB.addActionListener(this);
	runB.addActionListener(this);

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
	
	buttonPanel.add(Box.createVerticalStrut(15));
	buttonPanel.add(curGenLabel);
	buttonPanel.add(Box.createVerticalStrut(15));
	buttonPanel.add(panelOne);
	buttonPanel.add(panelTwo);
	buttonPanel.add(panelThree);
	buttonPanel.add(runB);

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
     *
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
	for(int i = 0; i < n; i++) {
	    double cos = Math.cos(Math.toRadians(((double)i/n)*360));
	    double sin = Math.sin(Math.toRadians(((double)i/n)*360));
	    popBounds.add(new Rectangle((int)((cos*rad) + centerX)+agentOffset, (int)((sin*rad) + centerY)+agentOffset, agentSize, agentSize));
	}
	int counter = 0;
	for(VisualAgent va: visualAgents) {
	    va.setBounds(popBounds.get(counter));
	    add(va);
	    counter++;
	}
    }

    private void updateVisualAgents() {
	for(VisualAgent va: visualAgents) {
	    va.updateColor(community.get(va.index).getPersonality());
	}
    }

    private void updateVisualAgents(String personalities) {
	for(int i = 0; i < visualAgents.size(); i++) {
	    visualAgents.get(i).updateColor(personalities.charAt(i));
	}
    }

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
	    runEpoch();
	    showNavItems();
	    runB.setEnabled(false);
	}
    }

    //This is a JPanel method, it draws stuff when initialized, resized, or repaint() is called
    @Override
    public void paintComponent(Graphics g) {
	//Draw a circle to fill the square!
	g.drawOval(dimensions[0] + circleOffset, dimensions[1] + circleOffset, dimensions[2], dimensions[3]);
    }
}

