import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.ArrayList;

import javax.swing.*;

public class PopulationPanel extends JPanel {

    private int popSize = 4;

    private int[] dimensions = {0, 0, 400, 400};
    private int offset = 10;
    private int tfSize = 20;

    private ArrayList<Rectangle> popBounds;
    private ArrayList<AETextField> population;

    public PopulationPanel() {
	super();
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
	popBounds.add(new Rectangle((dimensions[2] - dimensions[0]) / 2, dimensions[1] + offset, tfSize, tfSize));
	popBounds.add(new Rectangle(dimensions[2] - (offset + tfSize), (dimensions[3] - dimensions[1]) / 2, tfSize, tfSize));
	popBounds.add(new Rectangle((dimensions[2] - dimensions[0]) / 2, dimensions[3] - (offset + tfSize), tfSize, tfSize));
	popBounds.add(new Rectangle(dimensions[0] + offset, (dimensions[3] - dimensions[1]) / 2, tfSize, tfSize));
	int counter = 0;
	for(AETextField tf: population) {
	    System.out.println("popBounds @ " + counter + " >> " + popBounds.get(counter));
	    tf.setBounds(popBounds.get(counter));
	    add(tf);
	    counter++;
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	g.drawOval(dimensions[0] + offset, dimensions[1] + offset, dimensions[2] - (offset*2), dimensions[3] - (offset*2));
    }

    public int getPopSize() {
	return popSize;
    }

    public void setPopSize(int popSize) {
	this.popSize = popSize;
    }

}