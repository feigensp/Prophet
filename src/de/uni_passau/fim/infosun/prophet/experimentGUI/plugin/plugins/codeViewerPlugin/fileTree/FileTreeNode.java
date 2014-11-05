package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A <code>DefaultMutableTreeNode</code> to be used in a <code>JTree</code> that displays a file system directory
 * structure.
 */
public class FileTreeNode {

    private FileTreeNode parent;

    private File file;
    private List<FileTreeNode> children;

    /**
     * Constructs a new <code>FileTreeNode</code> representing the root node of a directory structure. Calling this
     * constructor will add <code>FileTreeNode</code> instances for all children of <code>file</code> to this
     * <code>FileTreeNode</code>. Equivalent to calling {@link #FileTreeNode(java.io.File, FileTreeNode)} with
     * <code>parent</code> <code>null</code>.
     *
     * @param file
     *         the <code>File</code> to create a <code>FileTreeNode</code> structure for
     *
     * @throws IllegalArgumentException
     *         if <code>file</code> is <code>null</code> or does not exist according to
     *         {@link java.io.File#exists()}
     */
    public FileTreeNode(File file) {
        this(file, null);
    }

    /**
     * Constructs a new <code>FileTreeNode</code>. Calling this constructor will add <code>FileTreeNode</code>
     * instances for all (filesystem) children of <code>file</code> to this <code>FileTreeNode</code>.
     *
     * @param file
     *         the <code>File</code> to create a <code>FileTreeNode</code> structure for
     *
     * @param parent
     *         the parent <code>FileTreeNode</code> for this node, <code>null</code> for the root node of a tree
     *
     * @throws IllegalArgumentException
     *         if <code>file</code> is <code>null</code> or does not exist according to
     *         {@link java.io.File#exists()}
     */
    public FileTreeNode(File file, FileTreeNode parent) {
        List<File> files;
        List<File> directories;
        File[] children;

        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Can not construct a FileTreeNode from null or a non-existent File.");
        }

        this.parent = parent;
        this.file = file;
        this.children = new ArrayList<>();

        if (!file.isFile()) {

            if ((children = file.listFiles()) != null) {
                files = new ArrayList<>(children.length);
                directories = new ArrayList<>(children.length);

                Arrays.sort(children); // sort by the natural ordering of File instances (this is filesystem dependant)

                for (File child : children) {
                    if (child.isFile()) {
                        files.add(child);
                    } else {
                        directories.add(child);
                    }
                }

                directories.stream().map(f -> new FileTreeNode(f, this)).forEach(this.children::add);
                files.stream().map(f -> new FileTreeNode(f, this)).forEach(this.children::add);
            }
        }
    }

    /**
     * Equivalent to .getFile().getPath().
     *
     * @return the path of the file this <code>FileTreeNode</code> represents
     */
    public String getFilePath() { // TODO replace this methods usages by .getFile().getPath()/.getName()
        return file.getPath();
    }

    /**
     * Returns whether this <code>FileTreeNode</code> represents a files (as opposed to a directory).
     *
     * @return true iff this <code>FileTreeNode</code> represents a file
     */
    public boolean isFile() {
        return file.isFile();
    }

    /**
     * Returns the index of the given <code>child</code> in this <code>FileTreeNode</code>s children list.
     *
     * @param child the child the get the index for
     * @return the index, possibly -1 if <code>child</code> is not a child of this node
     */
    public int getIndexOfChild(FileTreeNode child) {
        return children.indexOf(child);
    }

    /**
     * Returns the number of children this <code>FileTreeNode</code> has.
     *
     * @return the number of children
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns the child at the given index or <code>null</code> if the index is invalid.
     *
     * @param index the index of the child
     * @return the child <code>FileTreeNode</code> or <code>null</code>
     */
    public FileTreeNode getChild(int index) {
        if (!(index < 0 || index >= getChildCount())) {
            return children.get(index);
        } else {
            return null;
        }
    }

    /**
     * Removes all children from this <code>FileTreeNode</code> and sets their parents to <code>null</code>.
     */
    public void removeAllChildren() {
        children.forEach(child -> child.parent = null);
        children.clear();
    }

    /**
     * Returns the parent <code>FileTreeNode</code> of this node. The root node of a tree has parent <code>null</code>.
     *
     * @return the parent of this node
     */
    public FileTreeNode getParent() {
        return parent;
    }

    /**
     * Removes this node from its parent and sets its parent to <code>null</code>. Does nothing for the root node of a
     * tree.
     */
    public void removeFromParent() {
        if (parent != null) {
            parent.children.remove(this);
            parent = null;
        }
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
