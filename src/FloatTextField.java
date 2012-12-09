import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.*;

class FloatTextField extends JTextField {

    //Maximum number of digits 
    //Also used as the number of columns in the text field
    private int maxDigits;

    private boolean autofill = true;

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
     * Will only accept numbers and 1 decimal point as input
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