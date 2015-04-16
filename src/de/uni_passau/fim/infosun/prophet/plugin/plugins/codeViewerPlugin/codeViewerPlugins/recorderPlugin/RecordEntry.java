package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * <code>Recorder</code> implementations produce <code>RecordEntry</code>s which are recorded by a <code>Record</code>.
 * Implementations of this class should use XStream annotations to make sure that the XML that is produced when the
 * <code>Record</code> is serialized is readable.
 */
public abstract class RecordEntry implements Comparable<RecordEntry> {

    protected static final String OPENED = "opened";
    protected static final String CLOSED = "closed";

    @XStreamAsAttribute
    private long timestamp;

    /**
     * Constructs a new <code>RecordEntry</code> and records the current time as its timestamp.
     */
    public RecordEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the timestampt of this <code>RecordEntry</code>.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(RecordEntry other) {
        return Long.compare(timestamp, other.timestamp);
    }
}
