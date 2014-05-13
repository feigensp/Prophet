package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public interface RecorderPlugin {

    public Setting getSetting(Attribute node);

    public void onFrameCreate(Attribute selected, CodeViewer viewer, LoggingTreeNode currentNode);

    public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab);

    public String getKey();
}
