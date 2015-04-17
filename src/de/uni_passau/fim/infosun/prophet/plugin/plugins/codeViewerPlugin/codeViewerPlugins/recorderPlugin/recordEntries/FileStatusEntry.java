package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.CVStatusEntry.Action;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.io.File;

@XStreamAlias("fileEntry")
public class FileStatusEntry extends EditorPanelEntry {

    private Action action;
    private File file;
    
    public FileStatusEntry(EditorPanel panel, Action action) {
        super(panel);
        this.action = action;
        this.file = panel.getFile();
    }
}
