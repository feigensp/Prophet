package plugins.QuestionSwitching;

import java.util.List;
import java.util.Vector;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentViewer.ExperimentViewer;

public class ExperimentQuestionSwitchingPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(String type) {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		if (type.equals(QuestionTreeNode.TYPE_CATEGORY)) {
			result.add(new SettingsComponentDescription(SettingsCheckBox.class,"questionswitching", "Vor- und Zurückblättern erlauben"));
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
