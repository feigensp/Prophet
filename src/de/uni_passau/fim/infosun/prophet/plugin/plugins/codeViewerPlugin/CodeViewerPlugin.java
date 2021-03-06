package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin
        .RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.PathChooserSetting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.CATEGORY;

/**
 * A <code>Plugin</code> that displays a separate window in which code samples appropriate for the current experiment
 * node can be viewed by the experimentee. The experiment editor may define a directory containing the code samples
 * that should be available. Functionality such as syntax highlighting can be added as a plugin to the
 * <code>CodeViewer</code>.
 */
public class CodeViewerPlugin implements Plugin {

    public static final String KEY = "codeviewer";

    static RecorderPlugin recorder;
    
    static {
        recorder = new RecorderPlugin();
        CodeViewerPluginList.add(recorder);
    }
    
    private EViewer experimentViewer;
    private int count = 1;

    private Map<QTreeNode, CodeViewer> codeViewers;
    private Point eViewerLocation;

    /**
     * Constructs a new <code>CodeViewerPlugin</code>.
     */
    public CodeViewerPlugin() {
        this.codeViewers = new HashMap<>();
    }

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != CATEGORY) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        SettingsList settingsList = new SettingsList(mainAttribute, getClass().getSimpleName(), true);
        settingsList.setCaption(getLocalized("MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER"));

        Attribute subAttribute = mainAttribute.getSubAttribute(CodeViewer.KEY_PATH);
        Setting subSetting = new PathChooserSetting(subAttribute, null, PathChooserSetting.Type.DIRECTORIES);
        subSetting.setCaption(getLocalized("MENU_TAB_SETTINGS_SOURCE_CODE_PATH") + ":");
        settingsList.addSetting(subSetting);

        settingsList.addAllSettings(CodeViewerPluginList.getAllSettings(mainAttribute));

        return settingsList;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
        this.eViewerLocation = experimentViewer.getLocation();

        this.experimentViewer.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);

                eViewerLocation.setLocation(e.getComponent().getLocation());
            }
        });
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
        boolean enabled = node.containsAttribute(KEY) && Boolean.parseBoolean(node.getAttribute(KEY).getValue());

        if (!enabled) {
            return;
        }

        File eViewerSaveDir = experimentViewer.getSaveDir();
        File saveDir = new File(eViewerSaveDir, String.format("%d_%s_codeviewer", count++, node.getName()));

        CodeViewer cv = new CodeViewer(node.getAttribute(KEY), saveDir);

        Rectangle cvDim = cv.getBounds();
        cv.setLocation(eViewerLocation.x - (cvDim.width + 10), eViewerLocation.y);

        codeViewers.put(node, cv);
        cv.setVisible(true);
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
        CodeViewer cv = codeViewers.get(node);

        if (cv != null) {
            CodeViewerPluginList.onClose(cv);
            cv.dispose();

            codeViewers.remove(node);
        }
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
