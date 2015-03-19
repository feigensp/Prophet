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

public class Record {

    private static XStream serialiser = new XStream();
    
    static {
        serialiser.alias("record", Record.class);
        serialiser.addImplicitCollection(Record.class, "entries");
        serialiser.autodetectAnnotations(true); // we don't plan on deserialising anything
    }
    
    private List<RecordEntry> entries;

    public Record() {
        entries = new ArrayList<>();
    }

    public void save(File saveFile) throws IOException {

        Collections.sort(entries);
        QTreeFormatHandler.checkParent(saveFile);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), StandardCharsets.UTF_8)) {
            serialiser.toXML(this, writer);
        }
    }

    public void add(RecordEntry entry) {
        
        if (entry != null) {
            entries.add(entry);
        }
    }
    
    public void addAll(Collection<? extends RecordEntry> entries) {
        this.entries.addAll(entries);
    }
}
