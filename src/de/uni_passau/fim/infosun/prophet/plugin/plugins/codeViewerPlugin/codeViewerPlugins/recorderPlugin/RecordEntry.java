package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class RecordEntry {

    @XStreamAsAttribute
    private long timestamp;

    public RecordEntry() {
        this.timestamp = System.currentTimeMillis();
    }
}
