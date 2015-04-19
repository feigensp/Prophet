package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;

import java.io.File;

/**
 * A <code>RecordEntry</code> that is produced when a <code>CodeViewer</code> is opened or closed.
 */
@XStreamAlias("codeViewerEntry")
public class CVStatusEntry extends CodeViewerEntry {

    public enum Action {
        OPENED, CLOSED
    }

    private Action action;
    private File shownDir;

    /**
     * Constructs a new <code>CVStatusEntry</code> for the given <code>CodeViewer</code>.
     *
     * @param viewer the <code>CodeViewer</code> that was opened / closed
     * @param action whether the <code>CodeViewer</code> was opened or closed
     */
    public CVStatusEntry(CodeViewer viewer, Action action) {
        super(viewer);
        this.action = action;
        this.shownDir = viewer.getShowDir();
    }
}
