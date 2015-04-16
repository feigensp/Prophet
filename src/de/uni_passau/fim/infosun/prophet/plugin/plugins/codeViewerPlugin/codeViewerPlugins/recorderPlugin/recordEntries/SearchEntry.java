package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("searchEntry")
public class SearchEntry extends RecordEntry {

    private String action;
    private String query;
    private boolean success;

    public SearchEntry(String action, String query, boolean success) {
        this.action = action;
        this.query = query;
        this.success = success;
    }
}
