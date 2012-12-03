import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.*;

class FloatTextField extends JTextField {

    //Maximum number of digits 
    //Also used as the number of columns in the text field
    private int maxDigits;

    private boolean autofill = true;

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
    
    @Override
    public Document createDefaultModel() {
	return new DecimalDocument();
    }

    /**
     * Will only accept numbers and 1 decimal point as input
     */
    
    private class DecimalDocument extends PlainDocument {
	
	private final Pattern DIGITS = Pattern.compile("[\\d*.]");
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	    if(autofill) {
		super.insertString(offs, str, a);
	    }
	    else if(str != null && DIGITS.matcher(str).matches() && getLength() + str.length() <= maxDigits + 1) {
		super.insertString(offs, str, a);
	    }
	}
    }

}