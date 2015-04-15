package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("scrollingEntry")
public class ScrollingEntry extends RecordEntry {

    private int startLine;
    private int endLine;

    public ScrollingEntry(int startLine) {
        this.startLine = startLine;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}
