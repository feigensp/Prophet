package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class CodeViewerEntry extends RecordEntry {

    protected static class IDProvider<T> {

        private Map<T, Integer> currentIDs = new WeakHashMap<>();
        private Integer nextId = 0;

        public synchronized Integer idFor(T obj) {
            Integer id;

            if (currentIDs.containsKey(obj)) {
                id = currentIDs.get(obj);
            } else {
                id = nextId++;
                currentIDs.put(obj, id);
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
