package plugins;

import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentViewer.ExperimentViewer;

public interface ExperimentPlugin {
	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected);
	public void experimentEditorRun(ExperimentEditor experimentEditor);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
}
