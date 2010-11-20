package plugins.QuestionSwitching;

import java.util.List;
import java.util.Vector;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentViewer.ExperimentViewer;
import experimentViewer.HTMLFileView;

public class ExperimentQuestionSwitchingPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(QuestionTreeNode node) {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
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

	@Override
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKey() {
		return "questionswitching";
	}

}
