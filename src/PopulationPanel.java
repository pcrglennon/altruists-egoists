import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.ArrayList;

import javax.swing.*;

public class PopulationPanel extends JPanel {

    //These are all temporary variables - for a population size of 4
    private int popSize = 4;

    //400 by 400 square
    private int[] dimensions = {0, 0, 400, 400};
    private int offset = 10;
    //textfield size
    private int tfSize = 20;

    //Contains the "boundaries" of each AETextField (represents an individual in the
    //population), so that they may be placed in an absolute position
    private ArrayList<Rectangle> popBounds;
    //Contains the population
    private ArrayList<AETextField> population;

    public PopulationPanel() {
	super();
	//This means each component will be added w/ ABSOLUTE position (actual xy coords)
	setLayout(null);
	setPreferredSize(new Dimension(dimensions[2], dimensions[3]));

	popBounds = new ArrayList<Rectangle>(popSize);
	population = new ArrayList<AETextField>(popSize);

	layoutPopulation();
    }

    /**
     * Layout each individual along the circle
     *
     * First implementation - population of 4
     */
    private void layoutPopulation() {
	population.add(new AETextField("A"));
	population.add(new AETextField("E"));
	population.add(new AETextField("A"));
	population.add(new AETextField("E"));
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
	for(AETextField tf: population) {
	    System.out.println("popBounds @ " + counter + " >> " + popBounds.get(counter));
	    tf.setBounds(popBounds.get(counter));
	    add(tf);
	    counter++;
	}
    }

    //This is a JPanel method, it draws stuff when initialized, resized, or repaint() is called
    @Override
    public void paintComponent(Graphics g) {
	//Draw a circle to fill the square!
	g.drawOval(dimensions[0] + offset, dimensions[1] + offset, dimensions[2] - (offset*2), dimensions[3] - (offset*2));
    }

    public int getPopSize() {
	return popSize;
    }

    public void setPopSize(int popSize) {
	this.popSize = popSize;
    }

}