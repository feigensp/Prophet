package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

/**
 * A <code>RecordEntry</code> that is produced when the search functionality provided by
 * <code>SearchBar</code> is used.
 */
@XStreamAlias("searchEntry")
public class SearchEntry extends EditorPanelEntry {

    private String action;
    private String query;
    private boolean success;

    /**
     * Constructs a new <code>SearchEntry</code> recording a search event in the given <code>EditorPanel</code>.
     *
     * @param panel
     *         the <code>EditorPanel</code> whose contents where searched through
     * @param action
     *         the search action command
     * @param query
     *         the search query
     * @param success
     *         whether the search was successful
     */
    public SearchEntry(EditorPanel panel, String action, String query, boolean success) {
        super(panel);

        this.action = action;
        this.query = query;
        this.success = success;
    }
}
