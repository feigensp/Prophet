package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import java.awt.BorderLayout;

import de.uni_passau.fim.infosun.prophet.experimentGUI.PluginInterface;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.questionListPlugin.QuestionList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsCheckBox;


public class QuestionListPlugin implements PluginInterface {
	private final static String KEY = "question_list";

	private QuestionList overview;
	private boolean enabled;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.isExperiment()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY, UIElementNames.QUESTION_LIST_SHOW_LIST);
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
