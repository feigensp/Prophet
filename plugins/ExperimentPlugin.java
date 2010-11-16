package plugins;

import java.util.List;

import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentViewer.ExperimentViewer;

public interface ExperimentPlugin {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(String type);
	public void experimentEditorRun(ExperimentEditor experimentEditor);
	public void experimentViewerRun(ExperimentViewer experimentViewer);
}
