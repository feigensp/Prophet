package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * Plugins for the <code>CodeViewer</code> implement this interface. The <code>Plugin</code> methods will be called by
 * the <code>CodeViewer</code> as specified in the method documentation.
 */
public interface Plugin {

    /**
     * Returns the <code>Setting</code> of this <code>Plugin</code>. <code>Attribute</code>s to store settings in may
     * be obtained from the given <code>mainAttribute</code>.
     *
     * @param mainAttribute
     *         the <code>Attribute</code> to obtain sub-attributes from
     *
     * @return a <code>Setting</code> object representing the settings of the plugin or <code>null</code> if there are
     *         none
     */
    Setting getSetting(Attribute mainAttribute);

    /**
     * Called after a new <code>CodeViewer</code> was created.
     *
     * @param viewer
     *         the <code>CodeViewer</code> that was created
     */
    void onCreate(CodeViewer viewer);

    /**
     * Called after a new editor panel in a <code>CodeViewer</code> was created.
     *
     * @param codeViewer
     *         the <code>CodeViewer</code> that the <code>editorPanel</code> belongs to
     * @param editorPanel
     *         the created <code>EditorPanel</code>
     */
    void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel);

    /**
     * Called after an editor panel of a <code>CodeViewer</code> was closed.
     *
     * @param codeViewer
     *         the <code>CodeViewer</code> that the <code>editorPanel</code> belongs to
     * @param editorPanel
     *         the closed <code>EditorPanel</code>
     */
    void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel);

    /**
     * Called after a <code>CodeViewer</code> was closed.
     *
     * @param codeViewer
     *         the <code>CodeViewer</code> that was closed
     */
    void onClose(CodeViewer codeViewer);
}
