package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder.RecorderPluginInterface;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder.RecorderPluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeXMLHandler;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;

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

    private QuestionTreeNode selected;

    public static SettingsComponentDescription getSettingsComponentDescription() {
        SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Recorder", false);
        for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
            SettingsComponentDescription desc = plugin.getSettingsComponentDescription();
            if (desc != null) {
                result.addSubComponent(desc);
                while ((desc = desc.getNextComponentDescription()) != null) {
                    result.addSubComponent(desc);
                }
            }
        }
        return result;
    }

    public Recorder(QuestionTreeNode selected) {
        rootNode = new LoggingTreeNode(LoggingTreeNode.TYPE_LOGFILE);
        currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
        rootNode.add(currentNode);
        this.selected = selected;
    }

    protected void onFrameCreate(CodeViewer viewer) {
        codeViewer = viewer;
        tabbedPane = codeViewer.getTabbedPane();
        currentTab = null;
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (currentTab != tabbedPane.getSelectedComponent()) {
                    //Baum aktualisieren: neuer Zweig
                    currentTab = (EditorPanel) tabbedPane.getSelectedComponent();
                    if (currentTab == null) {
                        currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
                    } else {
                        currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_FILE);
                        currentNode.setAttribute(ATTRIBUTE_PATH, currentTab.getFilePath());
                    }
                    rootNode.add(currentNode);
                    //Plugins aktualisieren
                    for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
                        plugin.onNodeChange(currentNode, currentTab);
                    }
                }
            }
        });
        for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
            plugin.onFrameCreate(selected.getAddAttribute(KEY), codeViewer, currentNode);
        }
    }

    public void onEditorPanelCreate(EditorPanel editorPanel) {
        LoggingTreeNode openedNode = new LoggingTreeNode(TYPE_OPENED);
        openedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFilePath());
        currentNode.add(openedNode);
    }

    public void onEditorPanelClose(EditorPanel editorPanel) {
        LoggingTreeNode closedNode = new LoggingTreeNode(TYPE_CLOSED);
        closedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFilePath());
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
