package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A <code>TreeModel</code> that only allows <code>FileTreeNode</code> instances to be used. This is the
 * <code>TreeModel</code> used by <code>FileTree</code>.
 */
public class FileTreeModel implements TreeModel {

    private FileTreeNode root;

    /**
     * Constructs a new <code>FileTreeModel</code> with the given root node.
     *
     * @param root
     *         the root node for the tree
     */
    public FileTreeModel(FileTreeNode root) {
        this.root = root;
    }

    @Override
    public FileTreeNode getRoot() {
        return root;
    }

    @Override
    public FileTreeNode getChild(Object parent, int index) {
        if (parent instanceof FileTreeNode) {
            return ((FileTreeNode) parent).getChild(index);
        } else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof FileTreeNode) {
            return ((FileTreeNode) parent).getChildCount();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof FileTreeNode && ((FileTreeNode) node).isFile();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not used by this model
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (!(parent instanceof FileTreeNode && child instanceof FileTreeNode)) {
            return -1;
        } else {
            return ((FileTreeNode) parent).getIndexOfChild((FileTreeNode) child);
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // no events fired by this model
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        // no events fired by this model
    }

    /**
     * Builds a <code>FileTreeNode[]</code> representing the path to take through the tree to arrive at a
     * <code>FileTreeNode</code> representing the given <code>File</code>. Returns <code>null</code> if there is no such
     * node in the tree.
     *
     * @param file the file to build the path to
     * @return the path
     */
    public FileTreeNode[] buildPath(File file) {

        if (root == null || !isChild(root.getFile(), file)) {
            return null;
        }

        List<FileTreeNode> path = new ArrayList<>();
        FileTreeNode searchNode = root;

        do {
            path.add(searchNode);
            searchNode = searchNode.getChildren().stream().filter(node -> isChild(node.getFile(), file)).findFirst().orElse(null);
        } while (searchNode != null);

        if (!path.isEmpty() && isSameFile(path.get(path.size() - 1).getFile(), file)) {
            return path.toArray(new FileTreeNode[path.size()]);
        } else {
            return null;
        }
    }

    /**
     * Checks whether two <code>File</code> instances represent the same file. Will return <code>false</code> if
     * there is an <code>IOException</code> trying to determine this.
     *
     * @param first the first <code>File</code>
     * @param second the second <code>File</code>
     * @return true iff the two <code>File</code> represent the same file
     */
    private boolean isSameFile(File first, File second) {
        try {
            return Files.isSameFile(first.toPath(), second.toPath());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks whether the given <code>file</code> is a child (in the file system directory structure) of the
     * <code>File</code> <code>parent</code>.
     *
     * @param parent the parent <code>File</code> to check against
     * @param file the file to check
     * @return true iff <code>file</code> is a child of <code>parent</code>
     */
    private boolean isChild(File parent, File file) {
        return file.toPath().toAbsolutePath().startsWith(parent.toPath().toAbsolutePath());
    }
}
