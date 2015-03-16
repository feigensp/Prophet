package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class TabSwitchRecorder extends Recorder {

    public TabSwitchRecorder(RecorderPlugin recorder, CodeViewer viewer) {
        super(recorder, viewer);
    }

    @Override
    public Setting getSetting(Attribute mainAttribute) {
        return null;
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {

    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {

    }
}
