package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.awt.BorderLayout;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.questionListPlugin.QuestionList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type;

public class QuestionListPlugin implements Plugin {

    private final static String KEY = "question_list";

    private QuestionList overview;
    private boolean enabled;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != Type.EXPERIMENT) {
            return null;
        }

        return new SettingsCheckBox(node.getAttribute(KEY), getClass().getSimpleName());
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
