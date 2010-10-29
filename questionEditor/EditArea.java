package questionEditor;

/**
 * a JTextPane which could be extended which a few methods
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import javax.swing.JTextPane;

public class EditArea extends JTextPane {

	/**
	 * normal JTextPane initialisation
	 */
	public EditArea() {
		super();
	}

	/**
	 * Sorrounds the Selected Text with a Tag, or if nothing is selected write
	 * the Tag to the caret position
	 * 
	 * @param tag
	 */
	public void setTag(String tag) {
		String text = this.getSelectedText() == null ? "" : this
				.getSelectedText();
		this.replaceSelection("<" + tag + ">" + text + "</" + tag + ">");
	}

}
