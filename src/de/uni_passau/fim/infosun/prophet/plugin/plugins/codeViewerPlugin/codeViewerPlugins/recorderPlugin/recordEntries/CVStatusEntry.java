package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;

@XStreamAlias("codeViewerEntry")
public class CVStatusEntry extends CodeViewerEntry {

    public enum Action {
        OPENED, CLOSED
    }

    private Action action;

    public CVStatusEntry(CodeViewer viewer, Action action) {
        super(viewer);
        this.action = action;
    }
}
