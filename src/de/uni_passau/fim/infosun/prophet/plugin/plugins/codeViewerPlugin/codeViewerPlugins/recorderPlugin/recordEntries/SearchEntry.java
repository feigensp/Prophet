package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

@XStreamAlias("searchEntry")
public class SearchEntry extends EditorPanelEntry {

    private String action;
    private String query;
    private boolean success;

    public SearchEntry(EditorPanel panel, String action, String query, boolean success) {
        super(panel);

        this.action = action;
        this.query = query;
        this.success = success;
    }
}
