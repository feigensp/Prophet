package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public interface CodeViewerPluginInterface {

    public SettingsComponentDescription getSettingsComponentDescription();

    public void init(QuestionTreeNode selected);

    public void onFrameCreate(CodeViewer viewer);

    public void onEditorPanelCreate(EditorPanel editorPanel);

    public void onEditorPanelClose(EditorPanel editorPanel);

    //public String getKey();
    public void onClose();
}
