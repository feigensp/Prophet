package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.CVStatusEntry.Action;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.io.File;

/**
 * A <code>RecordEntry</code> that is produced when a tab for a file is opened or closed in the <code>CodeViewer</code>.
 */
@XStreamAlias("fileEntry")
public class FileStatusEntry extends EditorPanelEntry {

    private Action action;
    private File file;

    /**
     * Constructs a new <code>FileStatusEntry</code> recording an open/close event for the file of the given
     * <code>EditorPanel</code>.
     *
     * @param panel
     *         the <code>EditorPanel</code> whose file was opened/closed
     * @param action
     *         whether the file of the <code>panel</code> was opened or closed
     */
    public FileStatusEntry(EditorPanel panel, Action action) {
        super(panel);
        this.action = action;
        this.file = panel.getFile();
    }
}
