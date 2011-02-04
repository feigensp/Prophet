package experimentGUI.plugins;

import java.awt.BorderLayout;

import experimentGUI.PluginInterface;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.plugins.questionListPlugin.QuestionList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;


public class QuestionListPlugin implements PluginInterface {
	private final static String KEY = "question_list";

	private QuestionList overview;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		overview = new QuestionList(experimentViewer.getTree());
		//overview.setPreferredSize(new Dimension(150, 2));
		experimentViewer.add(overview, BorderLayout.WEST);
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}
	
	@Override
	public void enterNode(QuestionTreeNode node) {
		overview.visit(node);
	}	

	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node) {		
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		return null;
	}
}
