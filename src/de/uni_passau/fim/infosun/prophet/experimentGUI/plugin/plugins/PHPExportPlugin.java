package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.phpExportPlugin.PHPExportComponent;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type;

public class PHPExportPlugin implements Plugin {

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != Type.EXPERIMENT) {
            return null;
        }

        return new PHPExportComponent(getClass().getSimpleName());
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        return false;
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
