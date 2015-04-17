package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;

@XStreamAlias("globalSearchEntry")
public class GlobalSearchEntry extends CodeViewerEntry {

    private String action;
    private String query;
    private boolean success;

    public GlobalSearchEntry(CodeViewer viewer, String action, String query, boolean success) {
        super(viewer);

        this.action = action;
        this.query = query;
        this.success = success;
    }
}
