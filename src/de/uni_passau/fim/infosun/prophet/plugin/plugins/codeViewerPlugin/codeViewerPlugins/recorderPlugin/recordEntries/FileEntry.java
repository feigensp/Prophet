package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import java.io.File;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("fileEntry")
public class FileEntry extends RecordEntry {
    
    private static final String OPENED = "opened";
    private static final String CLOSED = "closed";

    private String action;
    private File file;
    
    public FileEntry(File file, boolean opened) {
        this.action = (opened) ? OPENED : CLOSED;
        this.file = file;
    }
}
