import java.awt.Color;

import javax.swing.JButton;

public class VisualAgent extends JButton {

    private int personality;

    public int index;

    public VisualAgent(int personality, int index) {
	this.personality = personality;
	this.index = index;
	//setText("" + (index+1));
	if(personality == 1) {
	    setBackground(Color.RED);
	} else {
	    setBackground(Color.BLUE);
	}
    }

    public void swapColor() {
	if(personality == 1) { 
	    personality = 2;
	    setBackground(Color.BLUE);
	} else {
	    personality = 1;
	    setBackground(Color.RED);
	}
    }

    public void updateColor(int newPersonality) {
	if(newPersonality == 1) {
	    setBackground(Color.RED);
	} else {
	    setBackground(Color.BLUE);
	}
    }

} 