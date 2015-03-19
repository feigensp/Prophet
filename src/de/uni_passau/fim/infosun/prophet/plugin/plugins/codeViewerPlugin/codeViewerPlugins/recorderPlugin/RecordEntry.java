package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class RecordEntry implements Comparable<RecordEntry> {

    protected static final String OPENED = "opened";
    protected static final String CLOSED = "closed";
    
    @XStreamAsAttribute
    private long timestamp;

    public RecordEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(RecordEntry other) {
        return Long.compare(timestamp, other.timestamp);
    }
}
