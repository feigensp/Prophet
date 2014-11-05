package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree;

import java.awt.AWTEvent;

/**
 * <code>FileEvent</code>s are produced by the <code>FileTree</code> when the user double clicks a file.
 */
public class FileEvent extends AWTEvent {

    public static final int FILE_OPENED = RESERVED_ID_MAX + 1;
    public static final int FILE_CLOSED = FILE_OPENED + 1;

    private String filePath;

    /**
     * Constructs a new <code>FileEvent</code> containing the given information.
     *
     * @param source
     *         the <code>FileTree</code> that produced the event
     * @param type
     *         the type of the event, should be one of {@link #FILE_OPENED} or {@link #FILE_CLOSED}
     * @param filePath
     *         the path to the file that was opened/closed
     */
    public FileEvent(FileTree source, int type, String filePath) {
        super(source, type);

        this.filePath = filePath;
    }

    /**
     * Returns the path to the file that was opened/closed.
     *
     * @return the path
     */
    public String getFilePath() {
        return filePath;
    }
}
