package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Record;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.FileStatusEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileRecorder extends Recorder {

    private Set<File> opened;
    
    public FileRecorder(Record record, CodeViewer viewer) {
        super(record, viewer);
        
        this.opened = new HashSet<>();
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
        File file = editorPanel.getFile();
        
        if (!opened.contains(file)) {
            opened.add(file);
            record.add(new FileStatusEntry(editorPanel, true));
        }
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        File file = editorPanel.getFile();

        if (opened.contains(file)) {
            opened.remove(file);
            record.add(new FileStatusEntry(editorPanel, false));
        }
    }

    @Override
    public void onClose() {
        opened.clear();
    }
}
