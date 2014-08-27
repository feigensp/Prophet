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
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsDirectoryPathChooser;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.CATEGORY;

public class CodeViewerPlugin implements Plugin {

    public final static String KEY = "codeviewer";

    private ExperimentViewer experimentViewer;
    private int count = 1;

    private HashMap<QTreeNode, CodeViewer> codeViewers;

    private Rectangle bounds;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != CATEGORY) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(getLocalized("MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER"));

        Attribute subAttribute = mainAttribute.getSubAttribute(CodeViewer.KEY_PATH);
        Setting subSetting = new SettingsDirectoryPathChooser(subAttribute, null);
        subSetting.setCaption(getLocalized("MENU_TAB_SETTINGS_SOURCE_CODE_PATH") + ":");
        pluginSettings.addSetting(subSetting);

        pluginSettings.addSetting(Recorder.getSetting(mainAttribute));
        pluginSettings.addAllSettings(CodeViewerPluginList.getAllSettings(mainAttribute));

        return pluginSettings;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
        codeViewers = new HashMap<>();
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
        boolean enabled = Boolean.parseBoolean(node.getAttribute(KEY).getValue());

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
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
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
