package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public interface CodeViewerPlugin {

    Setting getSetting(Attribute mainAttribute);

    void init(Attribute selected);

    void onFrameCreate(CodeViewer viewer);

    void onEditorPanelCreate(EditorPanel editorPanel);

    void onEditorPanelClose(EditorPanel editorPanel);

    void onClose();
}
