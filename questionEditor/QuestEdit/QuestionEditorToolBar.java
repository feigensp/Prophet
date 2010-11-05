package questionEditor.QuestEdit;

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
		JButton boldButton = new JButton("<b>");
		// toolbar buttons
		boldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editArea.setTag("b");
			}
		});
		add(boldButton);
		JButton tableButton = new JButton("<table>");
		add(tableButton);
		MacroBox macroBox = new MacroBox(editArea);
		add(macroBox);
	}
}
