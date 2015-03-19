package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import java.io.File;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("tabSelectionEntry")
public class TabSelectionEntry extends RecordEntry {

    private File tabFile;

    public TabSelectionEntry(File tabFile) {
        this.tabFile = tabFile;
    }
}
