package experimentEditor.ContentEdit.ToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import experimentEditor.EditArea;

import util.StringTupel;

public class FontStyleBox extends JComboBox implements ActionListener{
	
	private EditArea editArea;
	private ArrayList<StringTupel> fontStyles;
	
	public FontStyleBox(EditArea editArea) {
		super();
		this.editArea = editArea;
		fontStyles = new ArrayList<StringTupel>();
		fontStyles.add(new StringTupel("Fett", "b"));
		fontStyles.add(new StringTupel("Kursiv", "i"));
		fontStyles.add(new StringTupel("Unterstrichen", "u"));
		
		this.addItem("Schrifttyp");
		for(int i=0; i<fontStyles.size(); i++) {
			this.addItem(fontStyles.get(i).getKey());
		}
		this.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (this.getSelectedIndex() != 0) {
			editArea.setTag(fontStyles.get(this.getSelectedIndex()-1).getValue());
			this.setSelectedIndex(0);
		}		
	}

}
