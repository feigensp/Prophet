package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree;

import java.awt.AWTEvent;
import java.io.File;

/**
 * <code>FileEvent</code>s are produced by the <code>FileTree</code> when the user double clicks a file.
 */
public class FileEvent extends AWTEvent {

    public static final int FILE_OPENED = RESERVED_ID_MAX + 1;
    public static final int FILE_CLOSED = FILE_OPENED + 1;

    private File file;

    /**
     * Constructs a new <code>FileEvent</code> containing the given information.
     *
     * @param source
     *         the <code>FileTree</code> that produced the event
     * @param type
     *         the type of the event, should be one of {@link #FILE_OPENED} or {@link #FILE_CLOSED}
     * @param file
     *         the file that was opened/closed
     */
    public FileEvent(FileTree source, int type, File file) {
        super(source, type);

        this.file = file;
    }

    /**
     * Returns the <code>File</code> that was opened/closed.
     *
     * @return the <code>File</code>
     */
    public File getFile() {
        return file;
    }
}
