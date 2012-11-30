import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Static class which contains a number of constants (such as window sizes
 * and positions, colors, fonts, etc.) used by our program.
 *
 * They are placed here so they can be edited from this one file.
 */

public class Config {

    //Temporary
    public final static int POP_SIZE = 4;
    
    public final static Dimension MAIN_WINDOW_SIZE = new Dimension(750, 750);

    public final static Dimension SIDE_PANEL_PREF_SIZE = new Dimension(MAIN_WINDOW_SIZE.width /3, MAIN_WINDOW_SIZE.height);

    public final static Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 17);

}