import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.*;

public class AETextField extends JTextField {

    public AETextField(String defaultStr) {
	super();
	setColumns(1);
	setText(defaultStr);
    }
    
    //Construct an empty AETextField
    public AETextField() {
	this("");
    }

     @Override
    protected Document createDefaultModel() {
	return new AEDocument();
    }

    /**
     * Will only accept A's or E's as input
     */
    
    private class AEDocument extends PlainDocument {
	
	//Regular expression - matches "A", "E", "a", or "e"
	private final Pattern AE = Pattern.compile("[AE]|[ae]");
	
	//PlainDocument method - 
	//insert the input string into the textField IF it matches the AE
	//pattern AND the textField is empty
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	    if(str != null && AE.matcher(str).matches() && getLength() < 1) {
		super.insertString(offs, str.toUpperCase(), a);
	    }
	}
    }

}