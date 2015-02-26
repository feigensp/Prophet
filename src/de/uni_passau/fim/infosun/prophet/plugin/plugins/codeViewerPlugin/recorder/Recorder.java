package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeXMLHandler;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;

public class Recorder {

    public static final String KEY = "recorder";
    public static final String KEY_FILENAME = "filename";
    public static final String TYPE_OPENED = "opened";
    public static final String TYPE_CLOSED = "closed";
    public static final String TYPE_VIEWERCLOSED = "viewerclosed";
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

        rootNode.addChild(currentNode);
    }

    public static Setting getSetting(Attribute mainAttribute) {
        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        SettingsList settings = new SettingsList(mainAttribute, Recorder.class.getSimpleName(), false);
        settings.setCaption("Recorder");
        settings.addAllSettings(RecorderPluginList.getAllSettings(mainAttribute));

        return settings;
    }

    public void onFrameCreate(CodeViewer viewer) {
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
                rootNode.addChild(currentNode);

				RecorderPluginList.onNodeChange(currentNode, currentTab);
            }
        });

		RecorderPluginList.onFrameCreate(selected.getSubAttribute(KEY), codeViewer, currentNode);
    }

    public void onEditorPanelCreate(EditorPanel editorPanel) {
        LoggingTreeNode openedNode = new LoggingTreeNode(TYPE_OPENED);
        openedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFile().getName());
        currentNode.addChild(openedNode);
    }

    public void onEditorPanelClose(EditorPanel editorPanel) {
        LoggingTreeNode closedNode = new LoggingTreeNode(TYPE_CLOSED);
        closedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFile().getName());
        currentNode.addChild(closedNode);
    }

    public void addLoggingTreeNode(LoggingTreeNode node) {
        currentNode.addChild(node);
    }

    public void onClose() {
        LoggingTreeNode node = new LoggingTreeNode(TYPE_VIEWERCLOSED);
        currentNode.addChild(node);
        LoggingTreeXMLHandler.saveXMLTree(rootNode,
                codeViewer.getSaveDir().getPath() + System.getProperty("file.separator") + "recorder.xml");
    }
}
