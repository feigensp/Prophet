package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.*;

public class InactivityPlugin implements Plugin {

    public static final String KEY = "inactive";

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() == EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        Setting setting = new SettingsCheckBox(mainAttribute, getClass().getSimpleName());

        if (node.getType() == CATEGORY) {
            setting.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_DEACTIVATE_NODES"));
        } else if (node.getType() == QUESTION) {
            setting.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_DEACTIVATE_THIS_NODE"));
        }

        return setting;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return Boolean.parseBoolean(node.getAttribute(KEY).getValue());
    }

    @Override
    public void enterNode(QTreeNode node) {
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
