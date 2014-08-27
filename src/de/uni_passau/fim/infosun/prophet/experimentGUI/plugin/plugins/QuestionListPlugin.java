package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.awt.BorderLayout;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.questionListPlugin.QuestionList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type;

/**
 * Plugin that modifies the <code>ExperimentViewer</code> by adding a JTree representation of the current experiment.
 */
public class QuestionListPlugin implements Plugin {

    private final static String KEY = "question_list";

    private QuestionList overview;
    private boolean enabled;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != Type.EXPERIMENT) {
            return null;
        }

        Setting setting = new SettingsCheckBox(node.getAttribute(KEY), getClass().getSimpleName());
        setting.setCaption(UIElementNames.getLocalized("QUESTION_LIST_SHOW_LIST"));

        return setting;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        QTreeNode experimentNode = experimentViewer.getTree();
        enabled = Boolean.parseBoolean(experimentNode.getAttribute(KEY).getValue());

        if (enabled) {
            overview = new QuestionList(experimentNode);
            experimentViewer.add(overview, BorderLayout.WEST);
        }
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
        if (enabled) {
            overview.visit(node);
        }
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
