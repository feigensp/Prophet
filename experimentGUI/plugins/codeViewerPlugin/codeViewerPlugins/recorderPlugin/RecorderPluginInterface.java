package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import java.util.List;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface RecorderPluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer, LoggingTreeNode currentNode);
	public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab);
	public String getKey();
}
