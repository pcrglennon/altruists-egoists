import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;

import javax.swing.*;

public class MainWindow extends JFrame {

    //The main content panel
    private Container panel;

    private PopulationPanel populationPanel;

    private JPanel gridPanel;
    private JPanel imgPanel;
    private PopConfigPanel popConfigPanel;
    
    private JPanel buttonPanel;
    private JButton quitB;
    private JButton helpB;

    private GameLogPanel gameLogPanel;
    private int curIndex;

    public MainWindow() {
	super();
	
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI();
		}
	    });
    }

    private void createAndShowGUI() {
	try {
	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (Exception e) {
	
	}
	setSize(Config.MAIN_WINDOW_SIZE);
	setTitle("Altruists and Egoists");
	//Closing the Window will terminate the program
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel = getContentPane();

	gridPanel = new JPanel();
	gridPanel.setLayout(new GridLayout(2, 0));
	
	setupImgPanel();
	
	gridPanel.add(imgPanel);

	popConfigPanel = new PopConfigPanel();
	gridPanel.add(popConfigPanel);

	add(gridPanel, BorderLayout.LINE_START);
	
	populationPanel = new PopulationPanel(popConfigPanel.getConfigInfo());
	add(populationPanel, BorderLayout.CENTER);

	setupButtonPanel();
	panel.add(buttonPanel, BorderLayout.PAGE_END);

	gameLogPanel = new GameLogPanel();
	gameLogPanel.setPreferredSize(Config.SIDE_PANEL_PREF_SIZE);
	panel.add(gameLogPanel, BorderLayout.LINE_END);

	pack();

	setVisible(true);
    }

    public void resetPopulationPanel() {
	String errors = popConfigPanel.checkInputs();
	if(errors.equals("")) {
	    panel.remove(populationPanel);
	    populationPanel = new PopulationPanel(popConfigPanel.getConfigInfo());
	    panel.add(populationPanel, BorderLayout.CENTER);
	    panel.revalidate();
	    panel.repaint();
	} else {
	    JOptionPane.showMessageDialog(this, errors);
	}
    }

    public void updateGameLogPanel(String gameLogText) {
	gameLogPanel.updateGameLog(gameLogText);
    }
    
    private void setupImgPanel() {
	imgPanel = new JPanel();
	imgPanel.setLayout(new BoxLayout(imgPanel, BoxLayout.Y_AXIS));
	try {
	    imgPanel.add(Box.createVerticalStrut(25));
	    BufferedImage img = ImageIO.read(new File("media/BearBones2.png"));
	    JLabel bearImage = new JLabel(new ImageIcon(img));
	    //This aligns the image to the left for some reason I don't understand
	    bearImage.setAlignmentX(CENTER_ALIGNMENT);
	    imgPanel.add(bearImage);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	imgPanel.add(Box.createVerticalStrut(25));
	setupHelpB();
	JPanel helpBPanel = new JPanel();
	helpBPanel.setLayout(new FlowLayout());
	helpBPanel.add(helpB);
	imgPanel.add(helpBPanel);
    }
    
    private void setupHelpB() {
	helpB = new JButton("Help");

	helpB.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
		    HelpWindow hw = new HelpWindow();
		    hw.setVisible(true);
		}
	    });
	
    }

    private void setupButtonPanel() {
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

	quitB = new JButton("Quit");

	quitB.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });

	buttonPanel.add(quitB);
    }

    public static void main(String[] args) {
	MainWindow mw = new MainWindow();
    }

}