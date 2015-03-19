package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("codeViewerEntry")
public class CVEntry extends RecordEntry {
    
    private String action;

    public CVEntry(boolean opened) {
        this.action = opened ? OPENED : CLOSED;
    }
}
