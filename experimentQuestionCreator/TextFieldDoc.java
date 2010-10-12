package experimentQuestionCreator;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class TextFieldDoc extends DefaultStyledDocument {
	public TextFieldDoc() {
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		if (str.matches("[a-zA-Z0-9]*")) {
			super.insertString(offs, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
}