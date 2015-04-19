package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

/**
 * <code>Recorder</code> implementations are used by the <code>RecorderPlugin</code> to record some event in a
 * <code>CodeViewer</code> or one of its <code>EditorPanel</code>s. The <code>RecorderPlugin</code> instantiates
 * all <code>Recorder</code>s for every <code>CodeViewer</code> and passes the calls to its
 * {@link Plugin#onEditorPanelCreate(CodeViewer, EditorPanel)},
 * {@link Plugin#onEditorPanelClose(CodeViewer, EditorPanel)}, and {@link Plugin#onClose(CodeViewer)} to them.
 */
public abstract class Recorder {

    protected Record record;
    protected CodeViewer viewer;

    /**
     * Constructs a new <code>Recorder</code> for the given <code>CodeViewer</code>.
     *
     * @param record
     *         the <code>Record</code> that should be used to record <code>RecordEntry</code>s
     * @param viewer
     *         the <code>CodeViewer</code> this <code>Recorder</code> is for
     */
    public Recorder(Record record, CodeViewer viewer) {
        this.record = record;
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
