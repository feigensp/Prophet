package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.io.File;

/**
 * A <code>RecordEntry</code> that is produced when a tab (containing an <code>EditorPanel</code>) is selected in the
 * <code>CodeViewer</code>.
 */
@XStreamAlias("tabSelectionEntry")
public class TabSelectionEntry extends EditorPanelEntry {

    private File tabFile;

    /**
     * Constructs a new <code>TabSelectionEntry</code> indicating that the tab containing the given
     * <code>EditorPanel</code> was selected.
     *
     * @param panel
     *         the <code>EditorPanel</code> contained in the selected tab
     */
    public TabSelectionEntry(EditorPanel panel) {
        super(panel);
        this.tabFile = panel.getFile();
    }
}
