package plugins.codeViewer.codeViewerPlugins;

import java.util.List;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;

public interface CodeViewerPlugin {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions();
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer);
	public void onEditorPanelCreate(QuestionTreeNode selected, EditorPanel editorPanel);
}
