package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public interface Plugin {

    Setting getSetting(Attribute node);

    void onFrameCreate(Attribute selected, CodeViewer viewer, LoggingTreeNode currentNode);

    void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab);
}
