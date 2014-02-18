package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public interface RecorderPluginInterface {

    public SettingsComponentDescription getSettingsComponentDescription();

    public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer, LoggingTreeNode currentNode);

    public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab);

    public String getKey();
}
