package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsCheckBox;

public class InactivityPlugin implements Plugin {

    public static final String KEY = "inactive";

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
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
