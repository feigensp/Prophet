package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

public abstract class RecordEntry {

    private long timestamp;

    public RecordEntry() {
        this.timestamp = System.currentTimeMillis();
    }
}
