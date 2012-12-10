import javax.swing.JButton;

public class VisualAgent extends JButton {

    private int personality;

    public int index;

    public VisualAgent(int personality, int index) {
	this.personality = personality;
	this.index = index;
	//setText("" + (index+1));
	setBorderPainted(false);
	if(personality == 1) {
	    setBackground(Config.EGOIST_COLOR);
	} else {
	    setBackground(Config.ALTRUIST_COLOR);
	}
    }

    public void swapColor() {
	if(personality == 1) { 
	    personality = 2;
	    setBackground(Config.ALTRUIST_COLOR);
	} else {
	    personality = 1;
	    setBackground(Config.EGOIST_COLOR);
	}
    }

    public void updateColor(int newPersonality) {
	if(newPersonality == 1) {
	    setBackground(Config.EGOIST_COLOR);
	} else {
	    setBackground(Config.ALTRUIST_COLOR);
	}
    }

    public void updateColor(char newPersonality) {
	if(newPersonality == 'E') {
	    setBackground(Config.EGOIST_COLOR);
	} else {
	    setBackground(Config.ALTRUIST_COLOR);
	}
    }

} 