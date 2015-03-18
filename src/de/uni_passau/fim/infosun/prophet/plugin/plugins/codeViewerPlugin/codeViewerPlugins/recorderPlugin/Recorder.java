package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

public abstract class Recorder {
    
    protected RecorderPlugin recorder;
    protected CodeViewer viewer;

    public Recorder(RecorderPlugin recorder, CodeViewer viewer) {
        this.recorder = recorder;
        this.viewer = viewer;
    }
 
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

    /**
     * Called after the <code>CodeViewer</code> this <code>Recorder</code> belongs to was closed.
     */
    public abstract void onClose();
}
