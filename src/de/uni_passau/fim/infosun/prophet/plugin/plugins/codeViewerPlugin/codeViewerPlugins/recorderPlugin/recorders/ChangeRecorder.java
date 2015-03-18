package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class ChangeRecorder extends Recorder {

    public ChangeRecorder(RecorderPlugin recorder, CodeViewer viewer) {
        super(recorder, viewer);
    }

    /**
     * Returns the <code>Setting</code> of this <code>Recorder</code>. <code>Attribute</code>s to store settings in may
     * be obtained from the given <code>mainAttribute</code>.
     *
     * @param mainAttribute
     *         the <code>Attribute</code> to obtain sub-attributes from
     *
     * @return a <code>Setting</code> object representing the settings of the recorder or <code>null</code> if there are
     *         none
     */
    public static Setting getSetting(Attribute mainAttribute) {
        return null;
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
     
    }

    @Override
    public void onClose() {
        
    }
}
