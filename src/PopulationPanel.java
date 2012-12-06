import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.*;

public class PopulationPanel extends JPanel implements ActionListener {

    private Circle gameRunner;
    private Agent[] community;

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

    public PopulationPanel() {
	super();

	gameRunner = new Circle();
	//This means each component will be added w/ ABSOLUTE position (actual xy coords)
	setLayout(null);
	setPreferredSize(Config.POP_PANEL_PREF_SIZE);

	popBounds = new ArrayList<Rectangle>(Config.DEF_POP_SIZE);
	visualAgents = new ArrayList<VisualAgent>(Config.DEF_POP_SIZE);

	gameRunner.gridInitialize(Config.DEF_POP_SIZE, Config.DEF_ALT_COST, Config.DEF_ALT_NUM, Config.DEF_AVG_ALT_SIZE, Config.DEF_NUM_GEN, Config.DEF_SEARCH_SIZE);
	community = gameRunner.getCommunity().toArray(new Agent[Config.DEF_POP_SIZE]);
	for(int i = 0; i < community.length; i++) {
	    VisualAgent va = new VisualAgent(community[i].getPersonality(), i);
	    va.addActionListener(this);
	    visualAgents.add(va);
	}
	layoutPopulation();
    }

    public void runEpoch() {
	gameRunner.runEpoch();
	updateVisualAgents();
    }

    public void runOneGeneration() {
	gameRunner.oneGeneration();
	updateVisualAgents();
    }

    public void reset(double[] configInfo) {
	gameRunner.gridInitialize((int)configInfo[0], configInfo[1], (int)configInfo[2], (int)configInfo[3], (int)configInfo[4], (int)configInfo[5]);
	community = gameRunner.getCommunity().toArray(new Agent[Config.DEF_POP_SIZE]);
	//If the population size has not changed
	if(community.length == visualAgents.size()) {
	    for(VisualAgent va: visualAgents) {
		va.updateColor(community[va.index].getPersonality());
	    }
	}
	//Otherwise, delete everything and re-layout
	else {
	    //Delete all VisualAgents
	    removeAll();
	    layoutPopulation();
	}
    }

    /**
     * Layout each individual along the circle
     *
     */
    private void layoutPopulation() {
	popBounds.clear();
	//Absolute positioning coords
	int n = community.length;
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
	    va.updateColor(community[va.index].getPersonality());
	}
    }

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() instanceof VisualAgent) {
	    VisualAgent va = (VisualAgent)e.getSource();
	    va.swapColor();
	    community[va.index].swapPersonality();
	}
    }

    //This is a JPanel method, it draws stuff when initialized, resized, or repaint() is called
    @Override
    public void paintComponent(Graphics g) {
	//Draw a circle to fill the square!
	g.drawOval(dimensions[0] + circleOffset, dimensions[1] + circleOffset, dimensions[2], dimensions[3]);
    }
}

