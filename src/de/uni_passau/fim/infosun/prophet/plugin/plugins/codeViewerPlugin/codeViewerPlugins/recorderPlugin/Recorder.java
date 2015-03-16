package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public abstract class Recorder {
    
    protected RecorderPlugin recorder;
    protected CodeViewer viewer;

    public Recorder(RecorderPlugin recorder, CodeViewer viewer) {
        this.recorder = recorder;
        this.viewer = viewer;
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
    public abstract Setting getSetting(Attribute mainAttribute);
    
    /**
     * Called after a new editor panel in the <code>CodeViewer</code> this <code>Recorder</code> belongs to was created.
     *
     * @param editorPanel
     *         the created <code>EditorPanel</code>
     */
    public abstract void onEditorPanelCreate(EditorPanel editorPanel);

    /**
     * Called after an editor panel in the <code>CodeViewer</code> this <code>Recorder</code> belongs to was closed.
     *
     * @param editorPanel
     *         the closed <code>EditorPanel</code>
     */
    public abstract void onEditorPanelClose(EditorPanel editorPanel);
}
