package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

@XStreamAlias("scrollingEntry")
public class ScrollingEntry extends EditorPanelEntry {

    private int startLine;
    private int endLine;

    public ScrollingEntry(CodeViewer viewer, EditorPanel panel, int startLine) {
        super(viewer, panel);
        this.startLine = startLine;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}
