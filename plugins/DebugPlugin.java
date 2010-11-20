package plugins;

import java.util.List;

import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentViewer.ExperimentViewer;
import experimentViewer.HTMLFileView;

public class DebugPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(
			QuestionTreeNode node) {
		System.out.println("Trying to get component descriptions.");
		return null;
	}

	@Override
	public void experimentEditorRun(ExperimentEditor experimentEditor) {
		System.out.println("Running experiment editor.");		
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		System.out.println("Running experiment viewer.");		
	}

	@Override
	public void enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		System.out.println("--> "+node.getType()+": "+node.getName());
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		System.out.println("<-- "+node.getType()+": "+node.getName());
	}

}
