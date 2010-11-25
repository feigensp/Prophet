package experimentGUI.experimentEditor.tabbedPane;

import javax.swing.JPanel;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public abstract class ExperimentEditorTab extends JPanel {
	public abstract void activate(QuestionTreeNode sel);
}
