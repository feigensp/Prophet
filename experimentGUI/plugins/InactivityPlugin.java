package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.language.UIElementNames;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsCheckBox;

public class InactivityPlugin implements PluginInterface {
	public static final String KEY = "inactive";

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.isCategory()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY,
					UIElementNames.MENU_TAB_SETTINGS_DEACTIVATE_NODES);
		} else if (node.isQuestion()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY,
					UIElementNames.MENU_TAB_SETTINGS_DEACTIVATE_THIS_NODE);
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
	public String finishExperiment() {
		return null;
	}

}
