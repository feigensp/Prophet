package experimentGUI;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface PluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
	public Object enterNode(QuestionTreeNode node);
	public void exitNode(QuestionTreeNode node, Object pluginData);
	public String getKey();
	public String finishExperiment();
}
