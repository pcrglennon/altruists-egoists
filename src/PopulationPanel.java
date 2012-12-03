import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;

import java.util.ArrayList;

import javax.swing.*;

public class PopulationPanel extends JPanel {

    private Circle gameRunner;
    private Agent[] community;

    //Contains the "boundaries" of each AETextField (represents an individual in the
    //population), so that they may be placed in an absolute position
    private ArrayList<Rectangle> popBounds;
    //Contains the population
    private ArrayList<AETextField> visualPopulation;

    //400 by 400 square
    private int[] dimensions = {0, 0, 400, 400};
    private int offset = 10;
    //textfield size
    private int tfSize = 20;

    public PopulationPanel() {
	super();

	gameRunner = new Circle();
	//This means each component will be added w/ ABSOLUTE position (actual xy coords)
	setLayout(null);
	setPreferredSize(Config.POP_PANEL_PREF_SIZE);

	popBounds = new ArrayList<Rectangle>(Config.DEF_POP_SIZE);
	visualPopulation = new ArrayList<AETextField>(Config.DEF_POP_SIZE);

	gameRunner.gridInitialize(Config.DEF_POP_SIZE, Config.DEF_ALT_COST, Config.DEF_ALT_NUM, Config.DEF_AVG_ALT_SIZE, Config.DEF_NUM_GEN, Config.DEF_SEARCH_SIZE);
	community = gameRunner.getCommunity().toArray(new Agent[Config.DEF_POP_SIZE]);
	for(Agent a: community) {
	    AETextField tf = new AETextField((a.getPersonality() == 1) ? "E" : "A");
	    visualPopulation.add(tf);
	}
	layoutPopulation();
    }

    public void runEpoch() {
	for(int i = 0; i < community.length; i++) {
	    community[i].setPersonality((visualPopulation.get(i).getText().equals("E")) ? 1 : 2);
	}
	gameRunner.runEpoch();
	updateTextFields();
    }

    //New pop size?
    public void reset(double[] configInfo) {
	gameRunner.gridInitialize((int)configInfo[0], configInfo[1], (int)configInfo[2], (int)configInfo[3], (int)configInfo[4], (int)configInfo[5]);
	community = gameRunner.getCommunity().toArray(new Agent[Config.DEF_POP_SIZE]);
	for(int i = 0; i < community.length; i++) {
	    Agent a = community[i];
	    visualPopulation.get(i).setText((a.getPersonality() == 1) ? "E" : "A");
	}
	//layoutPopulation();
    }

    /**
     * Layout each individual along the circle
     *
     * First implementation - population of 4
     */
    private void layoutPopulation() {
	popBounds.clear();
	//visualPopulation.add(new AETextField("A"));
	//visualPopulation.add(new AETextField("E"));
	//visualPopulation.add(new AETextField("A"));
	//visualPopulation.add(new AETextField("E"));
	//Absolute positioning coords
	//12 o'clock on the circle
	popBounds.add(new Rectangle((dimensions[2] - dimensions[0]) / 2, dimensions[1] + offset, tfSize, tfSize));
	//3 o'clock
	popBounds.add(new Rectangle(dimensions[2] - (offset + tfSize), (dimensions[3] - dimensions[1]) / 2, tfSize, tfSize));
	//6 o'clock
	popBounds.add(new Rectangle((dimensions[2] - dimensions[0]) / 2, dimensions[3] - (offset + tfSize), tfSize, tfSize));
	//9 o'clock
	popBounds.add(new Rectangle(dimensions[0] + offset, (dimensions[3] - dimensions[1]) / 2, tfSize, tfSize));
	int counter = 0;
	//Add each AETextField at their defined position around the circle
	for(AETextField tf: visualPopulation) {
	    //System.out.println("popBounds @ " + counter + " >> " + popBounds.get(counter));
	    tf.setBounds(popBounds.get(counter));
	    add(tf);
	    counter++;
	}
    }

    private void updateTextFields() {
	for(int i = 0; i < community.length; i++) {
	    visualPopulation.get(i).setText((community[i].getPersonality() == 1) ? "E" : "A");
	}
    }

    //This is a JPanel method, it draws stuff when initialized, resized, or repaint() is called
    @Override
    public void paintComponent(Graphics g) {
	//Draw a circle to fill the square!
	g.drawOval(dimensions[0] + offset, dimensions[1] + offset, dimensions[2] - (offset*2), dimensions[3] - (offset*2));
    }
}

