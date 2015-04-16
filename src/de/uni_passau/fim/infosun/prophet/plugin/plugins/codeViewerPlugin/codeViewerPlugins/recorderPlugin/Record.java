package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import de.uni_passau.fim.infosun.prophet.util.qTree.handlers.QTreeFormatHandler;

/**
 * A list of <code>RecordEntry</code>s that may be serialized to XML using its {@link Record#save(File)} method.
 */
public class Record {

    private static XStream serialiser = new XStream();

    static {
        serialiser.alias("record", Record.class);
        serialiser.addImplicitCollection(Record.class, "entries");
        serialiser.autodetectAnnotations(true); // we don't plan on deserialising anything
    }

    private List<RecordEntry> entries;

    /**
     * Constructs a new and empty <code>Record</code>.
     */
    public Record() {
        entries = new ArrayList<>();
    }

    /**
     * Saves the <code>Record</code> as an XML document to the given <code>saveFile</code>. The
     * <code>RecordEntry</code>s will be sorted by their timestamp.
     *
     * @param saveFile
     *         the file to save the <code>Record</code> to
     * @throws IOException
     *         if there is an <code>IOException</code> writing to the file
     */
    public void save(File saveFile) throws IOException {

        Collections.sort(entries);
        QTreeFormatHandler.checkParent(saveFile);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), StandardCharsets.UTF_8)) {
            serialiser.toXML(this, writer);
        }
    }

    /**
     * Adds a <code>RecordEntry</code> to this <code>Record</code>.
     *
     * @param entry
     *         the entry to add
     */
    public void add(RecordEntry entry) {

        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     * Adds all <code>RecordEntry</code>s in the given <code>Collection</code> to this <code>Record</code>.
     *
     * @param entries
     *         the entries to add
     */
    public void addAll(Collection<? extends RecordEntry> entries) {
        this.entries.addAll(entries);
    }
}
