package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

/**
 * This class should be extended when recording an event that occurred in a specific <code>EditorPanel</code>.
 * The <code>EditorPanelEntry</code> will add an attribute 'tabId' to the XML which identifies the
 * <code>EditorPanel</code> in which the event occurred.
 */
public abstract class EditorPanelEntry extends CodeViewerEntry {

    private static IDProvider<EditorPanel> idProvider = new IDProvider<>();

    @XStreamAsAttribute
    private int tabId;

    /**
     * Constructs a new <code>EditorPanelEntry</code> storing the ID of the given <code>EditorPanel</code>.
     *
     * @param panel
     *         the <code>EditorPanel</code> in which the event occurred
     */
    public EditorPanelEntry(EditorPanel panel) {
        super(panel.getCodeViewer());
        this.tabId = idProvider.idFor(panel);
    }
}
