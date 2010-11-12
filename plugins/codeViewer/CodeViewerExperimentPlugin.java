package plugins.codeViewer;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentViewer.ExperimentViewer;

public class CodeViewerExperimentPlugin implements ExperimentPlugin {

	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected) {
		if (selected.isCategory()) {
			return new SettingsComponentFactory(selected, new CodeViewerSettingsComponent(),"codeviewer","Codeviewer",selected.getAttribute("codeviewer"));
		}
		return null;
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
