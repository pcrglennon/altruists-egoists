import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

public class PopConfigPanel extends JPanel {

    private NumericTextField popSizeTF, altCostTF, numGenTF, altNumTF, avgAltSizeTF, searchSizeTF;

    public PopConfigPanel() {
	super();
	setPreferredSize(Config.POP_CONFIG_PANEL_PREF_SIZE);
	popSizeTF = new NumericTextField(2);
	altCostTF = new NumericTextField(2);
	numGenTF = new NumericTextField(3);
	altNumTF = new NumericTextField(2);
	avgAltSizeTF = new NumericTextField(2);
	searchSizeTF = new NumericTextField(1);
	addComponentsToPanel();
    }

    private void addComponentsToPanel() {
	setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();

	Insets padding = new Insets(15, 0, 0, 0);

	c.insets = padding;

	c.gridx = 0;
	c.gridy = 0;
	add(new JLabel("Pop. Size: "), c);

	c.gridx = 1;
	c.gridy = 0;
	add(popSizeTF, c);

	c.gridx = 0;
	c.gridy = 1;
	add(new JLabel("Altruism Cost: "), c);

	c.gridx = 1;
	c.gridy = 1;
	add(altCostTF, c);
	
	c.gridx = 0;
	c.gridy = 2;
	add(new JLabel("# Generations: "), c);
	
	c.gridx = 1;
	c.gridy = 2;
	add(numGenTF, c);

	c.gridx = 0;
	c.gridy = 3;
	add(new JLabel("# Altruists: "), c);

	c.gridx = 1;
	c.gridy = 3;
	add(altNumTF, c);
	
	c.gridx = 0;
	c.gridy = 4;
	add(new JLabel("A.A.C.S.: "), c);

	c.gridx = 1;
	c.gridy = 4;
	add(avgAltSizeTF, c);
	
	c.gridx = 0;
	c.gridy = 5;
	add(new JLabel("Search Size: "), c);
	
	c.gridx = 1;
	c.gridy = 5;
	add(searchSizeTF, c);
    }

}