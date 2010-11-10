package questionEditor.QuestEdit;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import questionEditor.EditArea;

@SuppressWarnings("serial")
public class QuestionEditorToolBar extends JToolBar {
	EditArea editArea;
	
	public QuestionEditorToolBar(EditArea ea) {
		editArea = ea;
		setFloatable(false);

		add(new FontStyleBox(editArea));
		add(new FontSizeBox(editArea));
		add(new FormularBox(editArea));
		add(new MacroBox(editArea));
	}
	
	public void setEnabled(boolean enabled) {
		Component[] components = this.getComponents();
		for(int i=0; i<components.length; i++) {
			components[i].setEnabled(enabled);
		}
	}
}
