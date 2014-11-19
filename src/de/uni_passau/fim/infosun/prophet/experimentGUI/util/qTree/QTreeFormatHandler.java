package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Contains methods used by both <code>QTreeXMLHandler</code> and <code>QTreeHTMLHandler</code>.
 */
public class QTreeFormatHandler {

    /**
     * Utility class.
     */
    protected QTreeFormatHandler() {}

    /**
     * Ensures that the given <code>saveFile</code> has an existing parent directory. <code>FileWriter</code>s will
     * only create the <code>File</code> they are trying to write to, not the directory structure above it.
     *
     * @param saveFile
     *         the file to be checked
     *
     * @throws java.io.FileNotFoundException
     *         if the directory structure can not be created
     */
    protected static void checkParent(File saveFile) throws FileNotFoundException {
        File parent = saveFile.getParentFile();

        if (parent == null || parent.exists()) {
            return;
        }

        if (!parent.mkdirs()) {
            throw new FileNotFoundException("Can not create the directory structure for " + saveFile.getAbsolutePath());
        }
    }
}
