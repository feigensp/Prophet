package experimentEditor;

/**
 * a JTextPane which could be extended which a few methods
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class EditArea extends JTextPane {

	/**
	 * normal JTextPane initialisation
	 */
	public EditArea() {
		super();
	}

	/**
	 * Surrounds the selected text with a tag, or if nothing is selected write
	 * the tag to the caret position
	 * 
	 * @param tag
	 */
	public void setTag(String tag) {
		String text = this.getSelectedText() == null ? "" : this
				.getSelectedText();
		this.replaceSelection("<" + tag + ">" + text + "</" + tag + ">");
	}
	
	/**
	 * Surrounds the selected text with the given strings.
	 * If nothing is selected it print the strings at the caret prosition.
	 * @param openTag string which is inserted before
	 * @param closeTag string which is inserted after
	 */
	public void setEmbedding(String openTag, String closeTag) {
		String text = this.getSelectedText() == null ? "" : this
				.getSelectedText();
		this.replaceSelection(openTag + text + closeTag);		
	}

}
