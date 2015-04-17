package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

public abstract class EditorPanelEntry extends CodeViewerEntry {

    private static IDProvider<EditorPanel> idProvider = new IDProvider<>();

    @XStreamAsAttribute
    private int panelId;

    public EditorPanelEntry(EditorPanel panel) {
        super(panel.getCodeViewer());
        this.panelId = idProvider.idFor(panel);
    }
}
