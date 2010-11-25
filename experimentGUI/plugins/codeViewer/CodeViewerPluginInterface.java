package experimentGUI.plugins.codeViewer;

import java.util.List;

import experimentGUI.experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentGUI.plugins.codeViewer.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public interface CodeViewerPluginInterface {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer);
	public void onEditorPanelCreate(QuestionTreeNode selected, EditorPanel editorPanel);
}
