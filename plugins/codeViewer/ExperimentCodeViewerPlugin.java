package plugins.codeViewer;

import java.util.List;
import java.util.Vector;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentViewer.ExperimentViewer;

public class ExperimentCodeViewerPlugin implements ExperimentPlugin {

	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected) {
		Vector<SettingsComponentFactory> result = new Vector<SettingsComponentFactory>();
		if (selected.isCategory()) {
			result.add(new SettingsComponentFactory(selected, new CodeViewerSettingsComponent(),"codeviewer","Codeviewer aktivieren"));
		}
		return result;
	}

	@Override
	public void experimentEditorRun(ExperimentEditor experimentEditor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		// TODO Auto-generated method stub
		
	}

}
