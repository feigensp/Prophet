package experimentGUI.plugins.codeViewerPlugin;

import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;

public interface CodeViewerPluginInterface {
	public SettingsComponentDescription getSettingsComponentDescription();
	public void init(QuestionTreeNode selected);
	public void onFrameCreate(CodeViewer viewer);
	public void onEditorPanelCreate(EditorPanel editorPanel);
	public void onEditorPanelClose(EditorPanel editorPanel);
	//public String getKey();
	public void onClose();
}
