package experimentGUI.plugins.codeViewerPlugin;

import java.util.List;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface CodeViewerPluginInterface {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer);
	public void onEditorPanelCreate(QuestionTreeNode selected, EditorPanel editorPanel);
}
