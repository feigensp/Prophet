package plugins;

import java.util.List;

import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentViewer.ExperimentViewer;

public interface ExperimentPlugin {
	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected);
	public void experimentEditorRun(ExperimentEditor experimentEditor);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
}
