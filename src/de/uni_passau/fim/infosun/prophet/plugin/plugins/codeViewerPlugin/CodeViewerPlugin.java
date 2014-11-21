package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public interface CodeViewerPlugin {

    public Setting getSetting(Attribute mainAttribute);

    public void init(Attribute selected);

    public void onFrameCreate(CodeViewer viewer);

    public void onEditorPanelCreate(EditorPanel editorPanel);

    public void onEditorPanelClose(EditorPanel editorPanel);

    public void onClose();
}
