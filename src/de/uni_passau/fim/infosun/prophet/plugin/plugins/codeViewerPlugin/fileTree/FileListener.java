package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree;

/**
 * Classes implementing this interface can be registered to receive events from a <code>FileTree</code>.
 */
public interface FileListener {

    /**
     * Called when a <code>FileEvent</code> occurs. <code>FileEvent</code>s are produced when the user double clicks
     * a file in the <code>FileTree</code>.
     *
     * @param event
     *         the <code>FileEvent</code> that occurred
     */
    void fileEventOccurred(FileEvent event);
}
