package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

import java.util.HashMap;
import java.util.Map;

public class EditorPanelEntry extends CodeViewerEntry {

    private static Map<EditorPanel, Integer> cvIDs = new HashMap<>();

    private static Integer idFor(EditorPanel panel) {
        Integer id;

        if (cvIDs.containsKey(panel)) {
            id = cvIDs.get(panel);
        } else {
            id = cvIDs.values().stream().max(Integer::compareTo).orElse(-1) + 1;
            cvIDs.put(panel, id);
        }

        return id;
    }

    @XStreamAsAttribute
    private int panelId;

    public EditorPanelEntry(CodeViewer viewer, EditorPanel panel) {
        super(viewer);
        this.panelId = idFor(panel);
    }
}
