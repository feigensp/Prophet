package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.io.File;
import java.io.IOException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.handlers.QTreeXMLHandler;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

/**
 * A <code>Plugin</code> that, when activated, exports the experiment that was shown to the user in the
 * <code>EViewer</code> to the folder that contains the answers.xml file.
 */
public class ExperimentExportPlugin implements Plugin {

    private static final String KEY = "experiment_export";
    private static final String FILENAME = "experiment.xml";

    private EViewer viewer;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != QTreeNode.Type.EXPERIMENT) {
            return null;
        }

        Setting setting = new SettingsCheckBox(node.getAttribute(KEY), getClass().getSimpleName());
        setting.setCaption(UIElementNames.getLocalized("EXPERIMENT_EXPORT"));

        return setting;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
        viewer = experimentViewer;
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
        QTreeNode root = viewer.getExperimentTree();
        File saveDir = viewer.getSaveDir();

        boolean enabled = root.containsAttribute(KEY) && Boolean.parseBoolean(root.getAttribute(KEY).getValue());

        if (!enabled) {
            return null;
        }

        try {
            QTreeXMLHandler.saveExperimentXML(root, new File(saveDir, FILENAME));
        } catch (IOException e) {
            System.err.println("Could not export the experiment.xml.");
            System.err.println(e.getMessage());
        }

        return null;
    }
}
