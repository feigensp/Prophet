package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

/**
 * A <code>RecordEntry</code> that is produced when the user scrolls the document displayed in an
 * <code>EditorPanel</code>. The lines stored in the <code>ScrollingEntry</code> are the first line of the document
 * displayed in the view at the start and end of the scrolling event.
 */
@XStreamAlias("scrollingEntry")
public class ScrollingEntry extends EditorPanelEntry {

    private int startLine;
    private int endLine;

    /**
     * Constructs a new <code>ScrollingEntry</code> recording a scroll event in the given <code>EditorPanel</code>.
     *
     * @param panel
     *         the <code>EditorPanel</code> whose document was scrolled
     * @param startLine
     *         the first line of the document displayed in the view at the start of the scrolling event
     */
    public ScrollingEntry(EditorPanel panel, int startLine) {
        super(panel);
        this.startLine = startLine;
    }

    /**
     * Returns the starting line of the scrolling event.
     *
     * @return the starting line
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * Sets the end line of the scrolling event to the given value.
     *
     * @param endLine
     *         the new end line
     */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}
