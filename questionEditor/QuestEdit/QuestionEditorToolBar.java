package questionEditor.QuestEdit;

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
}
