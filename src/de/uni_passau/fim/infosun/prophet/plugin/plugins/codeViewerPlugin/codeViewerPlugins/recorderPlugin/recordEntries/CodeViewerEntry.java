package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

import java.util.*;

public abstract class CodeViewerEntry extends RecordEntry {

    private static Map<CodeViewer, Integer> cvIDs = new HashMap<>();

    private static Integer idFor(CodeViewer viewer) {
        Integer id;

        if (cvIDs.containsKey(viewer)) {
            id = cvIDs.get(viewer);
        } else {
            id = cvIDs.values().stream().max(Integer::compareTo).orElse(-1) + 1;
            cvIDs.put(viewer, id);
        }

        return id;
    }

    @XStreamAsAttribute
    private int cvId;

    public CodeViewerEntry(CodeViewer viewer) {
        this.cvId = idFor(viewer);
    }
}
