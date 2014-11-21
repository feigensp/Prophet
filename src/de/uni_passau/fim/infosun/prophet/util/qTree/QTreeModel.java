package de.uni_passau.fim.infosun.prophet.util.qTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A <code>TreeModel</code> that handles <code>QTreeNode</code> objects.
 */
public class QTreeModel implements TreeModel {

    private List<TreeModelListener> listeners;
    private QTreeNode root;

    /**
     * Constructs a new <code>QTreeModel</code> with the given root for the tree.
     *
     * @param root
     *         the root
     */
    public QTreeModel(QTreeNode root) {
        listeners = new ArrayList<>();
        this.root = root;
    }

    /**
     * Sets the root of the tree.
     *
     * @param newRoot
     *         the new root of the tree
     * @throws NullPointerException if <code>newRoot</code> is <code>null</code>
     */
    public void setRoot(QTreeNode newRoot) {
        Objects.requireNonNull(newRoot, "newRoot must not be null!");

        QTreeNode oldRoot = root;

        root = newRoot;
        fireTreeStructureChanged(oldRoot);
    }

    /**
     * Renames a node to the new name.
     *
     * @param renameNode the node to be renamed
     * @param newName the new name for the node
     */
    public void rename(QTreeNode renameNode, String newName) {
        renameNode.setName(newName);
        fireNodeChanged(renameNode);
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the given node has changed.
     *
     * @param changedNode the node that has changed
     */
    private void fireNodeChanged(QTreeNode changedNode) {
        Object[] path;
        Object[] children = {changedNode};
        int[] childIndices;
        QTreeNode parent = changedNode.getParent();

        if (parent == null) { // construct a NodeChanged event for the root node
            path = new Object[] {changedNode};
            childIndices = null;
        } else {
            path = buildPath(changedNode, false);
            childIndices = new int[] {parent.getIndexOfChild(changedNode)};
        }

        TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

        for (TreeModelListener tml : listeners) {
            tml.treeNodesChanged(event);
        }
    }

    /**
     * Removes the given node from its parent thereby deleting it from the tree.
     *
     * @param removeNode the node to be removed
     */
    public void removeFromParent(QTreeNode removeNode) {
        QTreeNode parent = removeNode.getParent();
        int oldIndex = parent.getIndexOfChild(removeNode);

        parent.removeChild(removeNode);
        fireNodeRemoved(removeNode, oldIndex);
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the given node was removed from its parent.
     *
     * @param removedNode the node that was removed
     * @param oldIndex the index of the node in its parent before it was removed
     */
    private void fireNodeRemoved(QTreeNode removedNode, int oldIndex) {
        Object[] path = buildPath(removedNode, false);
        Object[] children = {removedNode};
        int[] childIndices = {oldIndex};
        TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

        for (TreeModelListener tml : listeners) {
            tml.treeNodesRemoved(event);
        }
    }

    /**
     * Adds a child node to the given parent.
     *
     * @param parent
     *         the parent of the child
     * @param child
     *         the child node to be added
     */
    public void addChild(QTreeNode parent, QTreeNode child) {
        parent.addChild(child);
        fireNodeInserted(child);
    }

    /**
     * Adds a child node to the given parent at the given index.
     *
     * @param parent
     *         the parent of the child
     * @param child
     *         the child node to be added
     * @param index
     *         the index for the child
     */
    public void addChild(QTreeNode parent, QTreeNode child, int index) {
        parent.addChild(child, index);
        fireNodeInserted(child);
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the given node has been inserted.
     *
     * @param insertedNode
     *         the node that has been inserted
     */
    private void fireNodeInserted(QTreeNode insertedNode) {
        Object[] path = buildPath(insertedNode, false);
        Object[] children = {insertedNode};
        int[] childIndices = {insertedNode.getParent().getIndexOfChild(insertedNode)};
        TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

        for (TreeModelListener tml : listeners) {
            tml.treeNodesInserted(event);
        }
    }

    /**
     * Builds an array of <code>QTreeNode</code>s that uniquely identify the path to the given node in the tree.
     * The elements of the array are ordered with the root as the first element of the array.
     *
     * @param from
     *         the node for which the path is to be built
     * @param includeFrom
     *         whether to include the <code>QTreeNode</code> <code>from</code> as the last element of the array
     *
     * @return the path in the specified format
     */
    public static QTreeNode[] buildPath(QTreeNode from, boolean includeFrom) {
        ArrayList<QTreeNode> path = new ArrayList<>();
        QTreeNode node = from.getParent();

        if (includeFrom) {
            path.add(from);
        }

        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);

        return path.toArray(new QTreeNode[path.size()]);
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the whole subtree under <code>root</code> has changed.
     * Passing <code>null</code> as <code>root</code> indicates that the tree has a new root node when there was none
     * before.
     *
     * @param root
     *         the root of the subtree
     */
    private void fireTreeStructureChanged(QTreeNode root) {
        TreeModelEvent event;

        if (root != null) {
            event = new TreeModelEvent(this, buildPath(root, true));
        } else {
            event = new TreeModelEvent(this, new Object[] {this.root});
        }

        for (TreeModelListener tml : listeners) {
            tml.treeStructureChanged(event);
        }
    }

    @Override
    public QTreeNode getRoot() {
        return root;
    }

    @Override
    public QTreeNode getChild(Object parent, int index) {
        return ((QTreeNode) parent).getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((QTreeNode) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((QTreeNode) node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not used by this model
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((QTreeNode) parent).getIndexOfChild((QTreeNode) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }
}
