package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

import java.util.HashMap;
import java.util.Map;

public abstract class CodeViewerEntry extends RecordEntry {

    protected static class IDProvider<T> {

        private Map<T, Integer> cvIDs = new HashMap<>();

        public synchronized Integer idFor(T obj) {
            Integer id;

            if (cvIDs.containsKey(obj)) {
                id = cvIDs.get(obj);
            } else {
                id = cvIDs.values().stream().max(Integer::compareTo).orElse(-1) + 1;
                cvIDs.put(obj, id);
            }

            return id;
        }
    }

    private static IDProvider<CodeViewer> idProvider = new IDProvider<>();

    @XStreamAsAttribute
    private int cvId;

    public CodeViewerEntry(CodeViewer viewer) {
        this.cvId = idProvider.idFor(viewer);
    }
}
