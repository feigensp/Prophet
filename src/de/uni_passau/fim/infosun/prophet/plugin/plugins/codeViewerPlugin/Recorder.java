package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.RecorderPluginList;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode.LoggingTreeXMLHandler;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class Recorder {

    public final static String KEY = "recorder";
    public final static String KEY_FILENAME = "filename";
    public final static String TYPE_OPENED = "opened";
    public final static String TYPE_CLOSED = "closed";
    public final static String TYPE_VIEWERCLOSED = "viewerclosed";
    public static final String ATTRIBUTE_PATH = "path";

    private LoggingTreeNode rootNode;
    private LoggingTreeNode currentNode;

    private CodeViewer codeViewer;
    private EditorTabbedPane tabbedPane;
    private EditorPanel currentTab;

    private Attribute selected;

    public Recorder(Attribute selected) {
        this.selected = selected;
        this.rootNode = new LoggingTreeNode(LoggingTreeNode.TYPE_LOGFILE);
        this.currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);

        rootNode.add(currentNode);
    }

    public static Setting getSetting(Attribute mainAttribute) {
        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings settings = new PluginSettings(mainAttribute, Recorder.class.getSimpleName(), false);
        settings.setCaption("Recorder");
        settings.addAllSettings(RecorderPluginList.getAllSettings(mainAttribute));

        return settings;
    }

    protected void onFrameCreate(CodeViewer viewer) {
        codeViewer = viewer;
        tabbedPane = codeViewer.getTabbedPane();
        currentTab = null;

        tabbedPane.addChangeListener(arg0 -> {
            if (currentTab != tabbedPane.getSelectedComponent()) {
                //Baum aktualisieren: neuer Zweig
                currentTab = (EditorPanel) tabbedPane.getSelectedComponent();
                if (currentTab == null) {
                    currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
                } else {
                    currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_FILE);
                    currentNode.setAttribute(ATTRIBUTE_PATH, currentTab.getFile().getName());
                }
                rootNode.add(currentNode);
                //Plugins aktualisieren
                for (RecorderPlugin plugin : RecorderPluginList.getPlugins()) {
                    plugin.onNodeChange(currentNode, currentTab);
                }
            }
        });

        for (RecorderPlugin plugin : RecorderPluginList.getPlugins()) {
            plugin.onFrameCreate(selected.getSubAttribute(KEY), codeViewer, currentNode);
        }
    }

    public void onEditorPanelCreate(EditorPanel editorPanel) {
        LoggingTreeNode openedNode = new LoggingTreeNode(TYPE_OPENED);
        openedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFile().getName());
        currentNode.add(openedNode);
    }

    public void onEditorPanelClose(EditorPanel editorPanel) {
        LoggingTreeNode closedNode = new LoggingTreeNode(TYPE_CLOSED);
        closedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFile().getName());
        currentNode.add(closedNode);
    }

    public void addLoggingTreeNode(LoggingTreeNode node) {
        currentNode.add(node);
    }

    public void onClose() {
        LoggingTreeNode node = new LoggingTreeNode(TYPE_VIEWERCLOSED);
        currentNode.add(node);
        LoggingTreeXMLHandler.saveXMLTree(rootNode,
                codeViewer.getSaveDir().getPath() + System.getProperty("file.separator") + "recorder.xml");
    }
}
