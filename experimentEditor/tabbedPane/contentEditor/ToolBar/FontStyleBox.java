package experimentEditor.tabbedPane.contentEditor.ToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import util.StringTuple;

@SuppressWarnings("serial")
public class FontStyleBox extends JComboBox implements ActionListener{
	
	private RSyntaxTextArea editArea;
	private ArrayList<StringTuple> fontStyles;
	
	public FontStyleBox(RSyntaxTextArea editArea) {
		super();
		this.editArea = editArea;
		fontStyles = new ArrayList<StringTuple>();
		fontStyles.add(new StringTuple("Fett", "b"));
		fontStyles.add(new StringTuple("Kursiv", "i"));
		fontStyles.add(new StringTuple("Unterstrichen", "u"));
		
		this.addItem("Schrifttyp");
		for(int i=0; i<fontStyles.size(); i++) {
			this.addItem(fontStyles.get(i).getKey());
		}
		this.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (this.getSelectedIndex() != 0) {
			String tag = fontStyles.get(this.getSelectedIndex()-1).getValue();
			String text = editArea.getSelectedText();
			text = text==null ? "" : text;
			editArea.replaceSelection("<"+tag+">"+text+"</"+tag+">");
			this.setSelectedIndex(0);
		}		
	}

}
