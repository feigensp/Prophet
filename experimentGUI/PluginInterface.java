package experimentGUI;

import java.util.List;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface PluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView);
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData);
	public String getKey();
}
