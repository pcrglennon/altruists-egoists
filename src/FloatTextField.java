import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.*;

/**
 * Similar to NumericTexField, but accepts decimal numbers from 0.00 to 9.99
 */

class FloatTextField extends JTextField {

    //Maximum number of digits 
    //Also used as the number of columns in the text field
    private int maxDigits;

    private boolean autofill = true;
    
    //Regular Expression denoting three forms of decimal numbers:
    // 1) 1 digit followed by a period, followed by 1-2 digits
    // 2) A period, followed by 1-3 digits
    // 3) 1 digit (single digit int)
    private final Pattern FULL_FLOAT = Pattern.compile("^\\d\\.\\d+$|^\\.\\d+$|^\\d$");

    public FloatTextField(int maxDigits, String input) {
	super();
	this.maxDigits = maxDigits;
	setColumns(maxDigits + 1);
	setText(input);
	autofill = false;
    }

    public FloatTextField(int maxDigits) {
	this(maxDigits, "");
    }
    
    /**
     * Checks first to see if the field is empty, then checks the contents of the
     * field against the FULL_FLOAT regular expression
     */
    public String validateInput(String tfName) {
	if(getText().equals("")) {
	    return tfName + ": Text Field is Empty!\n";
	}
	else if(FULL_FLOAT.matcher(getText()).matches()) {
	    return "";
	} else {
	    return tfName + ": Invalid Format (max 9.99)\n";
	}
    }
    
    @Override
    public Document createDefaultModel() {
	return new DecimalDocument();
    }

    /**
     * Will only accept numbers and periods as input
     */
    
    private class DecimalDocument extends PlainDocument {
	
	private final Pattern DIGITS_POINTS = Pattern.compile("[\\d*.]");
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	    if(autofill) {
		super.insertString(offs, str, a);
	    }
	    else if(str != null && DIGITS_POINTS.matcher(str).matches() && getLength() + str.length() <= maxDigits + 1) {
		super.insertString(offs, str, a);
	    }
	}
    }

}