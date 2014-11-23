package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsCheckBox;

import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.*;

/**
 * This <code>Plugin</code> enables the <code>ExperimentEditor</code> to mark nodes as 'inactive'. The
 * <code>Plugin</code>'s {@link #denyEnterNode(QTreeNode)} will return <code>true</code> for any node marked 'inactive'
 * and all its children.
 */
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
    public void experimentViewerRun(EViewer experimentViewer) {

    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        QTreeNode currentNode = node;

        do {
            if (currentNode.containsAttribute(KEY) && Boolean.parseBoolean(currentNode.getAttribute(KEY).getValue())) {
                return true;
            }

            currentNode = currentNode.getParent();
        } while (currentNode != null);

        return false;
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
