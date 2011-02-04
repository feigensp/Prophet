package experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;

import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentGUI.util.QuestionViewPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class ContentViewerPanel extends ExperimentEditorTab {

	private QuestionViewPane viewerPane;
	
	public ContentViewerPanel() {
		setLayout(new BorderLayout());
		this.setOpaque(false);
	}
	
	public void activate(QuestionTreeNode selected) {
		removeAll();
		updateUI();
		if (selected!=null) {
			viewerPane = new QuestionViewPane(selected);
			add(viewerPane,BorderLayout.CENTER);
		}
	}
}
