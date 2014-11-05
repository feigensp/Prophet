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

    public FileTreeNode[] buildPath(File file) {

        if (root == null) {
            return null;
        }

        List<FileTreeNode> path = new ArrayList<>();
        FileTreeNode searchNode = root;

        //TODO implement building the path

        return path.isEmpty() ? null : path.toArray(new FileTreeNode[path.size()]);
    }

    private boolean isSameFile(File first, File second) {
        try {
            return Files.isSameFile(first.toPath(), second.toPath());
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isChild(File child, File parent) {
        return child.toPath().startsWith(parent.toPath().toAbsolutePath());
    }
}
