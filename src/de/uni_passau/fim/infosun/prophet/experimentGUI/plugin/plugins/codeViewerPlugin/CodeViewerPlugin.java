package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public interface CodeViewerPlugin {

    public Setting getSetting(Attribute mainAttribute);

    public void init(QuestionTreeNode selected);

    public void onFrameCreate(CodeViewer viewer);

    public void onEditorPanelCreate(EditorPanel editorPanel);

    public void onEditorPanelClose(EditorPanel editorPanel);

    public void onClose();
}
