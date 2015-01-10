package de.uni_passau.fim.infosun.prophet.plugin.plugins.phpExportPlugin;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type;

public class PHPExportPlugin implements Plugin {

    @Override
    public Setting getSetting(QTreeNode node) {
        if (node.getType() != Type.EXPERIMENT) {
            return null;
        }

        return new PHPExportComponent(getClass().getSimpleName());
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
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
