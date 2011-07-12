package experimentGUI.plugins.codeViewerPlugin.recorder;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.recorder.loggingTreeNode.LoggingTreeNode;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;

public interface RecorderPluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer, LoggingTreeNode currentNode);
	public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab);
	public String getKey();
}
