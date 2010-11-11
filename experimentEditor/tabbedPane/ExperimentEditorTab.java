package experimentEditor.tabbedPane;

import javax.swing.JPanel;

import util.QuestionTreeNode;

@SuppressWarnings("serial")
public abstract class ExperimentEditorTab extends JPanel {
	public abstract void setSelected(QuestionTreeNode sel);
	public abstract void activate();
}
