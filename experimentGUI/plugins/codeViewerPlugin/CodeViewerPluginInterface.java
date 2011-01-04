package experimentGUI.plugins.codeViewerPlugin;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface CodeViewerPluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer);
	public void onEditorPanelCreate(EditorPanel editorPanel);
	public void onEditorPanelClose(EditorPanel editorPanel);
	public String getKey();
	public void onClose();
}
