package experimentGUI.plugins;

import java.awt.BorderLayout;

import experimentGUI.PluginInterface;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.plugins.questionListPlugin.QuestionList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsCheckBox;


public class QuestionListPlugin implements PluginInterface {
	private final static String KEY = "question_list";

	private QuestionList overview;
	private boolean enabled;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.isExperiment()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Fragenkatalog anzeigen");
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		QuestionTreeNode experimentNode = experimentViewer.getTree();
		enabled = Boolean.parseBoolean(experimentNode.getAttributeValue(KEY));
		if (enabled) {
			overview = new QuestionList(experimentNode);
			//overview.setPreferredSize(new Dimension(150, 2));
			experimentViewer.add(overview, BorderLayout.WEST);
		}
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}
	
	@Override
	public void enterNode(QuestionTreeNode node) {
		if (enabled) {
			overview.visit(node);
		}
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
	public String finishExperiment() {
		return null;
	}
}
