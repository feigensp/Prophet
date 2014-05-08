package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import javax.swing.JFrame;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsDirectoryPathChooser;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;

public class CodeViewerPlugin implements Plugin {

    public final static String KEY = "codeviewer";

    private ExperimentViewer experimentViewer;
    private int count = 1;

    private HashMap<QuestionTreeNode, CodeViewer> codeViewers;

    private Rectangle bounds;

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
        if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
            SettingsPluginComponentDescription result =
                    new SettingsPluginComponentDescription(KEY, UIElementNames.MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER,
                            true);
            result.addSubComponent(
                    new SettingsComponentDescription(SettingsDirectoryPathChooser.class, CodeViewer.KEY_PATH,
                            UIElementNames.MENU_TAB_SETTINGS_SOURCE_CODE_PATH + ":"));
            result.addSubComponent(Recorder.getSettingsComponentDescription());
            SettingsComponentDescription desc = CodeViewerPluginList.getSettingsComponentDescription();
            if (desc != null) {
                result.addSubComponent(desc);
                while ((desc = desc.getNextComponentDescription()) != null) {
                    result.addSubComponent(desc);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
        codeViewers = new HashMap<>();
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QuestionTreeNode node) {
        boolean enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
        if (enabled) {
            String savePath =
                    experimentViewer.getSaveDir().getPath() + System.getProperty("file.separator") + (count++) + "_"
                            + node.getName() + "_codeviewer";
            CodeViewer cv = new CodeViewer(node.getAttribute(KEY), new File(savePath));
            if (bounds == null) {
                Point location = experimentViewer.getLocation();
                cv.setLocation(new Point(location.x + 20, location.y + 20));
            } else {
                cv.setBounds(bounds);
            }
            cv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            cv.setVisible(true);
            codeViewers.put(node, cv);
        }
    }

    @Override
    public String denyNextNode(QuestionTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QuestionTreeNode node) {
        CodeViewer cv = codeViewers.get(node);
        if (cv != null) {
            CodeViewerPluginList.onClose();
            cv.getRecorder().onClose();
            bounds = cv.getBounds();
            cv.dispose();
            codeViewers.remove(node);
        }
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
