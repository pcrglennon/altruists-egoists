import javax.swing.JButton;

/**
 * A visual representation of an agent in the game
 *
 * Essentially a square button - blue if egoist, red if altruist
 */

public class VisualAgent extends JButton {

    private int personality;

    //An index to the actual agent in the game
    public int index;

    /**
     * Constructor - initial personality and the index of the agent to represent
     */
    public VisualAgent(int personality, int index) {
	this.personality = personality;
	this.index = index;
	setBorderPainted(false);
	if(personality == 1) {
	    setBackground(Config.EGOIST_COLOR);
	} else {
	    setBackground(Config.ALTRUIST_COLOR);
	}
    }
    
    /**
     * Swap the color from red to blue or blue to red
     */
    public void swapColor() {
	if(personality == 1) { 
	    personality = 2;
	    setBackground(Config.ALTRUIST_COLOR);
	} else {
	    personality = 1;
	    setBackground(Config.EGOIST_COLOR);
	}
    }
    
    /**
     * Update the color, called by PopulationPanel after changing between
     * generations
     */
    public void updateColor(char newPersonality) {
	if(newPersonality == 'E') {
	    setBackground(Config.EGOIST_COLOR);
	} else {
	    setBackground(Config.ALTRUIST_COLOR);
	}
    }

} 