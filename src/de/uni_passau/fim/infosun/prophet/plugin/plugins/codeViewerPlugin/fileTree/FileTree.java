package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * A <code>JTree</code> displaying the filesystem directory structure under a given <code>File</code>.
 * Fires <code>FileEvent</code>s when files are double clicked.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Koeppen
 */
public class FileTree extends JTree {

    private List<FileListener> fileListeners;

    private FileTreeModel model;

    /**
     * Constructs a new <code>FileTree</code> displaying the directory structure under <code>rootDir</code>.
     *
     * @param rootDir
     *         the root directory for the <code>FileTree</code>
     */
    public FileTree(File rootDir) {
        super(new FileTreeModel((rootDir != null && rootDir.exists()) ? new FileTreeNode(rootDir) : null));

        this.fileListeners = new LinkedList<>();
        this.model = (FileTreeModel) getModel();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                TreePath selPath = getPathForLocation(e.getX(), e.getY());

                if ((e.getClickCount() == 2) && (selPath != null)) {
                    FileTreeNode lastPathComponent = (FileTreeNode) selPath.getLastPathComponent();

                    if (lastPathComponent.isFile()) {
                        fireFileOpenedEvent(lastPathComponent.getFile());
                    }
                }
            }
        });

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Adds the given <code>FileListener</code> to this <code>FileTree</code>s listeners.
     *
     * @param listener
     *         the <code>FileListener</code> to add
     */
    public void addFileListener(FileListener listener) {
        fileListeners.add(listener);
    }

    /**
     * Removes the given <code>FileListener</code> from this <code>FileTree</code>s listeners.
     *
     * @param listener
     *         the <code>FileListener</code> to remove
     */
    public void removeFileListener(FileListener listener) {
        fileListeners.remove(listener);
    }

    /**
     * Fires a <code>FileEvent</code> indicating that the given <code>File</code> was opened.
     *
     * @param file
     *         the <code>File</code> that was opened
     */
    private void fireFileOpenedEvent(File file) {
        FileEvent event = new FileEvent(this, FileEvent.FILE_OPENED, file);

        fileListeners.forEach(listener -> listener.fileEventOccurred(event));
    }

    /**
     * Selects the tree node representing the given <code>File</code> if it is in the directory structure that is
     * currently being displayed.
     *
     * @param file
     *         the file to be selected
     */
    public void selectFile(File file) {
        FileTreeNode[] path = model.buildPath(file);
        TreePath treePath;

        if (path != null) {
            treePath = new TreePath(path);

            setSelectionPath(treePath);
            makeVisible(treePath);
        }
    }

    /**
     * Sets the <code>TreeModel</code> that will provide the data. <code>FileTree</code> only accepts non-null models
     * of type <code>FileTreeModel</code>.
     * <p>
     * This is a bound property.
     *
     * @param newModel
     *         the <code>TreeModel</code> that is to provide the data
     * @throws IllegalArgumentException
     *         if <code>newModel</code> is not of type <code>FileTreeModel</code>
     */
    @Override
    public void setModel(TreeModel newModel) {

        if (newModel instanceof FileTreeModel) {
            model = (FileTreeModel) newModel;
            super.setModel(newModel);
        } else {
            throw new IllegalArgumentException("newModel must be of type " + FileTreeModel.class.getSimpleName());
        }
    }
}
