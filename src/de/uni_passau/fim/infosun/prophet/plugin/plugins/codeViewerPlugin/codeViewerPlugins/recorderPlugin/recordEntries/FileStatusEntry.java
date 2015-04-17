package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.io.File;

@XStreamAlias("fileEntry")
public class FileStatusEntry extends EditorPanelEntry {

    private String action;
    private File file;
    
    public FileStatusEntry(EditorPanel panel, boolean opened) {
        super(panel);

        this.action = (opened) ? OPENED : CLOSED;
        this.file = panel.getFile();
    }
}
