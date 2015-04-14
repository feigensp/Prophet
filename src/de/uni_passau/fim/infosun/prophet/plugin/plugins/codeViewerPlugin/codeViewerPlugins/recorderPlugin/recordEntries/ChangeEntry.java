package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecordEntry;

@XStreamAlias("changeEntry")
public class ChangeEntry extends RecordEntry {

    @XStreamAlias("type")
    public static enum Type {
        INSERT, REMOVE
    }

    private Type type;

    private int offset;
    private String content;

    public ChangeEntry(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return content.length();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void appendContent(String additionalContent) {
        this.content += additionalContent;
    }
}
