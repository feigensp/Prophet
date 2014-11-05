package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTree;
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
public class FileTree extends JTree { //TODO remove all the String paths, use canonical Files everywhere

    private List<FileListener> fileListeners;

    private FileTreeModel model;
    private FileTreeNode root;

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
        this.root = model.getRoot();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                TreePath selPath = getPathForLocation(e.getX(), e.getY());

                if ((e.getClickCount() == 2) && (selPath != null)) {
                    FileTreeNode lastPathComponent = (FileTreeNode) selPath.getLastPathComponent();

                    if (lastPathComponent.isFile()) {
                        fireFileOpenedEvent(lastPathComponent.getFilePath());
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
     * Fires a <code>FileEvent</code> indicating the the <code>File</code> with the given <code>path</code> was opened.
     *
     * @param filePath
     *         the path of the opened file
     */
    private void fireFileOpenedEvent(String filePath) {
        FileEvent event = new FileEvent(this, FileEvent.FILE_OPENED, filePath);

        fileListeners.forEach(listener -> listener.fileEventOccured(event));
    }

    /**
     * Selects the tree node representing the file with the given <code>path</code> if it is in the directory structure
     * that is currently being displayed.
     *
     * @param path
     *         the path to the file to be selected
     */
    public void selectFile(String path) {
        if (path.startsWith(System.getProperty("file.separator"))) {
            path = path.substring(System.getProperty("file.separator").length());
        }
        ArrayList<String> pathElements = new ArrayList<>();
        if (root != null) {
            pathElements.add(root.toString());
            int pos = path.indexOf(System.getProperty("file.separator"));
            while (pos != -1) {
                pathElements.add(path.substring(0, pos));
                path = path.substring(pos + 1);
                pos = path.indexOf(System.getProperty("file.separator"));
            }
            pathElements.add(path);
            ArrayList<Object> treePathList = new ArrayList<>();
            treePathList.add(root);
            FileTreeNode currentNode = root;
            for (String pathElement : pathElements) {
                for (int j = 0; j < currentNode.getChildCount(); j++) {
                    if (currentNode.getChild(j).toString().equals(pathElement)) {
                        currentNode = currentNode.getChild(j);
                        treePathList.add(currentNode);
                        break;
                    }
                }
            }
            TreePath selection = new TreePath(treePathList.toArray());

            expandPath(selection);
            setSelectionPath(selection);
        }
    }
}
