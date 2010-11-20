package plugins;

import java.util.List;

import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentViewer.ExperimentViewer;
import experimentViewer.HTMLFileView;

public interface ExperimentPlugin {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(QuestionTreeNode node);
	public void experimentEditorRun(ExperimentEditor experimentEditor);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView);
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData);
	public String getKey();
}
