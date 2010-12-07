package experimentGUI.experimentEditor.tabbedPane;

import java.awt.BorderLayout;

import experimentGUI.experimentViewer.QuestionViewPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class ContentViewerPanel extends ExperimentEditorTab {
	public static final String HTMLSTART = "<html><body>";
	public static final String HTMLEND = "</body></html>";
	
	private QuestionTreeNode selected;
	private QuestionViewPane viewerPane;
	
	public ContentViewerPanel() {
		setLayout(new BorderLayout());
	}
	
	public void activate(QuestionTreeNode selected) {
		removeAll();
		updateUI();
		this.selected=selected;
		if (selected!=null) {
			viewerPane = new QuestionViewPane(selected);
			add(viewerPane,BorderLayout.CENTER);
		}
	}
}
