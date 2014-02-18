package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.PluginInterface;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.phpExportPlugin.PHPExportComponent;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public class PHPExportPlugin implements PluginInterface {

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
        if (node.isExperiment()) {
            return new SettingsComponentDescription(PHPExportComponent.class, null, null);
        }
        return null;
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
