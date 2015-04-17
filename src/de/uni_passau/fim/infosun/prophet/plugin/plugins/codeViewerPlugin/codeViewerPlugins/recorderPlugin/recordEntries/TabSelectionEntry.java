package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.io.File;

@XStreamAlias("tabSelectionEntry")
public class TabSelectionEntry extends EditorPanelEntry {

    private File tabFile;

    public TabSelectionEntry(EditorPanel panel) {
        super(panel);
        this.tabFile = panel.getFile();
    }
}
