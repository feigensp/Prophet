package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import java.io.File;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

public class FileEntry extends RecordEntry {
    
    private static final String OPENED = "opened";
    private static final String CLOSED = "closed";
    
    private String action;
    private File concerning;
    
    public FileEntry(File concerning, boolean opened) {
        this.action = (opened) ? OPENED : CLOSED;
        this.concerning = concerning;
    }
}
