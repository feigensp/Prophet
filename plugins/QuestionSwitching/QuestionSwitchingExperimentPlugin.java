package plugins.QuestionSwitching;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentViewer.ExperimentViewer;

public class QuestionSwitchingExperimentPlugin implements ExperimentPlugin {

	@Override
	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected) {
		if(selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"questionswitching", "Vor- und Zurückblättern erlauben",selected.getAttribute("questionswitching"));
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
