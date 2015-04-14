package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import javax.swing.event.ChangeListener;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin
        .RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin
        .recordEntries.TabSelectionEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class TabSwitchRecorder extends Recorder {

    private EditorTabbedPane tabbedPane;
    private ChangeListener listener;
    private EditorPanel selected;
    
    public TabSwitchRecorder(RecorderPlugin recorder, CodeViewer viewer) {
        super(recorder, viewer);

        this.tabbedPane = viewer.getTabbedPane();
        this.listener = c -> {
            EditorPanel newSelection = (EditorPanel) tabbedPane.getSelectedComponent();
            
            if (newSelection != null && newSelection != selected) {
                recorder.record(viewer, new TabSelectionEntry(newSelection.getFile()));
                selected = newSelection;
            }
        };
        
        this.tabbedPane.addChangeListener(listener);
    }

    /**
     * Returns the <code>Setting</code> of this <code>Recorder</code>. <code>Attribute</code>s to store settings in may
     * be obtained from the given <code>mainAttribute</code>. <code>mainAttribute</code> will be an
     * <code>Attribute</code> returned by the {@link RecorderPlugin#getAttribute()} method. <code>Recorder</code>
     * instances can retrieve the values for their settings from the <code>RecorderPlugin</code> instance given
     * in the constructor.
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
        tabbedPane.removeChangeListener(listener);
    }
}
