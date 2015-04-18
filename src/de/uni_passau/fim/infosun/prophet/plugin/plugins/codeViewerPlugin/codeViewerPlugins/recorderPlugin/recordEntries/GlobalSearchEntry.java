package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;

/**
 * A <code>RecordEntry</code> that is produced when the search functionality provided by
 * <code>GlobalSearchBar</code> is used.
 */
@XStreamAlias("globalSearchEntry")
public class GlobalSearchEntry extends CodeViewerEntry {

    private String action;
    private String query;
    private boolean success;

    /**
     * Construcs a new <code>GlobalSearchEntry</code> recording a search event in the given <code>CodeViewer</code>.
     *
     * @param viewer
     *         the <code>CodeViewer</code> whose displayed files where searched through
     * @param action
     *         the search action command
     * @param query
     *         the search query
     * @param success
     *         whether the search was successful
     */
    public GlobalSearchEntry(CodeViewer viewer, String action, String query, boolean success) {
        super(viewer);

        this.action = action;
        this.query = query;
        this.success = success;
    }
}
