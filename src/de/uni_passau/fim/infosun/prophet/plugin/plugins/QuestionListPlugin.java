package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.questionListPlugin.QuestionList;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsCheckBox;

import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type;

/**
 * Plugin that modifies the <code>ExperimentViewer</code> by adding a JTree representation of the current experiment.
 */
public class QuestionListPlugin implements Plugin {

    private final static String KEY = "question_list";

    private QuestionList questionList;

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
    public void experimentViewerRun(EViewer experimentViewer) {
        QTreeNode experimentNode = experimentViewer.getExperimentTree();
        boolean enabled = Boolean.parseBoolean(experimentNode.getAttribute(KEY).getValue());

        if (enabled) {
            questionList = new QuestionList(experimentNode);
            experimentViewer.add(new JScrollPane(questionList), BorderLayout.WEST);
        }
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
        if (questionList != null) {
            questionList.setSelectionPath(node);
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
