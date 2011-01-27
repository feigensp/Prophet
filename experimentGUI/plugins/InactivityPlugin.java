package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class InactivityPlugin implements PluginInterface {
	public static final String KEY = "inactive";

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (!node.isExperiment()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY,
					"Diesen und alle Unterknoten deaktivieren");
		} else {
			return null;
		}
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {		
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return Boolean.parseBoolean(node.getAttributeValue(KEY));
	}

	@Override
	public void enterNode(QuestionTreeNode node) {
	}

	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
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
