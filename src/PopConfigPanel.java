import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PopConfigPanel extends JPanel {

    private NumericTextField popSizeTF, altNumTF, avgAltSizeTF, numGenTF, searchSizeTF;
    private FloatTextField altCostTF;

    private JButton resetB;

    public PopConfigPanel() {
	super();
	setPreferredSize(Config.POP_CONFIG_PANEL_PREF_SIZE);
	popSizeTF = new NumericTextField(2, "" + Config.DEF_POP_SIZE);
	altCostTF = new FloatTextField(3, "" + Config.DEF_ALT_COST);
	altNumTF = new NumericTextField(3, "" + Config.DEF_ALT_NUM);
	avgAltSizeTF = new NumericTextField(3, "" + Config.DEF_AVG_ALT_SIZE);
	numGenTF = new NumericTextField(3, "" + Config.DEF_NUM_GEN);
	searchSizeTF = new NumericTextField(2, "" + Config.DEF_SEARCH_SIZE);
	setupResetB();
	addComponentsToPanel();
    }

    public void setupResetB() {
	resetB = new JButton("Reset");
	resetB.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    MainWindow mainWindow = (MainWindow)getTopLevelAncestor();
		    mainWindow.resetPopulationPanel();
		}
	    });
    }

    public double[] getConfigInfo() {
	double[] configInfo = {Double.parseDouble(popSizeTF.getText()), Double.parseDouble(altCostTF.getText()), Double.parseDouble(altNumTF.getText()), Double.parseDouble(avgAltSizeTF.getText()), Double.parseDouble(numGenTF.getText()), Double.parseDouble(searchSizeTF.getText())};
	return configInfo;
    }

    //Returns "" if no error
    public String checkInputs() {
	StringBuilder errors = new StringBuilder();
	if(popSizeTF.getText().equals("")) {
	    errors.append("Population Size: Text Field is Empty!\n");
	} else if (Integer.parseInt(popSizeTF.getText()) < 3){
	    errors.append("Population Size: Must be 3 or larger!\n");
	}
	if(altNumTF.getText().equals("")) {
	    errors.append("Number of Altruists: Text Field is Empty!\n");
	}	
	if(avgAltSizeTF.getText().equals("")) {
	    errors.append("Average Altruist Community Size: Text Field is Empty!\n");
	}
	if(numGenTF.getText().equals("")) {
	    errors.append("Number of Generations: Text Field is Empty!\n");
	}	
	errors.append(altCostTF.validateInput("Altruism Cost"));
	if(searchSizeTF.getText().equals("")) {
	    errors.append("Search Size: Text Field is Empty!\n");
	}
	return errors.toString();
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
	add(new JLabel("# Generations: "), c);
	
	c.gridx = 1;
	c.gridy = 1;
	add(numGenTF, c);

	c.gridx = 0;
	c.gridy = 2;
	add(new JLabel("# Altruists: "), c);

	c.gridx = 1;
	c.gridy = 2;
	add(altNumTF, c);
	
	c.gridx = 0;
	c.gridy = 3;
	add(new JLabel("A.A.C.S.: "), c);

	c.gridx = 1;
	c.gridy = 3;
	add(avgAltSizeTF, c);

	c.gridx = 0;
	c.gridy = 5;
	add(new JLabel("Altruism Cost: "), c);

	c.gridx = 1;
	c.gridy = 5;
	add(altCostTF, c);

	c.gridx = 0;
	c.gridy = 6;
	add(new JLabel("Search Size: "), c);
	
	c.gridx = 1;
	c.gridy = 6;
	add(searchSizeTF, c);

	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 7;
	add(resetB, c);
    }

}