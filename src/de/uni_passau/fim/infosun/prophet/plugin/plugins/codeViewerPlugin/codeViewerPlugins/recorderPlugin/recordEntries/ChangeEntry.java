package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;

/**
 * A <code>RecordEntry</code> that is produced when the contents of a document displayed in an <code>EditorPanel</code>
 * are changed. For insertions the inserted content is saved.
 */
@XStreamAlias("changeEntry")
public class ChangeEntry extends EditorPanelEntry {

    @XStreamAlias("type")
    public enum Type {
        INSERT, REMOVE
    }

    private Type type;

    private int offset;
    private int length;
    private String content;

    /**
     * Constructs a new <code>ChangeEntry</code> recording a change to the document displayed in the given
     * <code>EditorPanel</code>. The data to be saved must be set using the provided setter methods.
     *
     * @param panel
     *         the <code>EditorPanel</code> whose document was changed
     * @param type
     *         whether an insertion or removal of content occured
     */
    public ChangeEntry(EditorPanel panel, Type type) {
        super(panel);
        this.type = type;
    }

    /**
     * Returns the type of the change.
     *
     * @return whether this <code>ChangeEntry</code> records insertion or removal of content
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the offset into the document at which the insertion or removal started.
     *
     * @return the offset into the document
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the offset into the document to the given value.
     *
     * @param offset
     *         the new offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Returns the length of the inserted or removed content.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the changed content to the given value.
     *
     * @param length
     *         the new length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the changed content. Returns <code>null</code> for <code>REMOVE ChangeEntry</code>s and the inserted
     * content for <code>INSERT ChangeEntry</code>s.
     *
     * @return the changed content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the changed content to the given value.
     *
     * @param content
     *         the new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Appends the given <code>additionalContent</code> to the current changed content and updates the length
     * accordingly.
     *
     * @param additionalContent
     *         the additional content to append
     */
    public void appendContent(String additionalContent) {

        if (type == Type.REMOVE) {
            String m = "Ignoring a call to appendContent(String) because the ChangeEntry records a removal of content.";
            System.err.println(m);
            return;
        }

        this.length += additionalContent.length();
        this.content += additionalContent;
    }
}
