package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class should be extended when recording an event that concerns a <code>CodeViewer</code> but no specific
 * <code>EditorPanel</code> in it. The <code>CodeViewerEntry</code> will add an attribute 'cvId' to the XML which
 * identifies the <code>CodeViewer</code> in which the event occurred.
 */
public abstract class CodeViewerEntry extends RecordEntry {

    /**
     * Provides <code>Integer</code> IDs for objects of type <code>T</code>. The references to the objects are weak
     * and will not prevent garbage collection.
     *
     * @param <T>
     *         the type of the objects for which IDs are to be provided
     */
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

    /**
     * Constructs a new <code>CodeViewerEntry</code> storing the ID of the given <code>CodeViewer</code>.
     *
     * @param viewer
     *         the <code>CodeViewer</code> in which the event occurred
     */
    public CodeViewerEntry(CodeViewer viewer) {
        this.cvId = idProvider.idFor(viewer);
    }
}
