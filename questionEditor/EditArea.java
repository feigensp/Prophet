package questionEditor;

import javax.swing.JTextPane;

public class EditArea extends JTextPane{
	
	public EditArea() {
		super();
	}
	
	public void setTag(String tag) {
		String text = this.getSelectedText() == null? "" : this.getSelectedText();		
		this.replaceSelection("<" + tag + ">" + text + "</" + tag +">");
	}

}
