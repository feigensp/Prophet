package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     */
    public void setRoot(QTreeNode newRoot) {
        QTreeNode oldRoot = root;

        root = newRoot;
        fireTreeStructureChanged(oldRoot);
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

    private void fireNodeInserted(QTreeNode insertedNode) {
        Object[] path = buildPath(insertedNode);
        Object[] children = {insertedNode};
        int[] childIndices = {insertedNode.getParent().getIndexOfChild(insertedNode)};
        TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);

        for (TreeModelListener tml : listeners) {
            tml.treeNodesInserted(event);
        }
    }

    /**
     * Builds an array of Object identifying the path to the parent of the node <code>from</code>, where the first
     * element of the array is the Object stored at the root node and the last element is the Object stored at the
     * parent node of <code>from</code>.
     *
     * @param from
     *         the node for which the path is to be built
     *
     * @return the path in the specified format
     */
    private Object[] buildPath(QTreeNode from) {
        ArrayList<Object> path = new ArrayList<>();
        QTreeNode node = from.getParent();

        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);

        return path.toArray();
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the whole tree has changed.
     *
     * @param oldRoot
     *         the old root of the tree
     */
    private void fireTreeStructureChanged(QTreeNode oldRoot) {
        TreeModelEvent event = new TreeModelEvent(this, new Object[] {oldRoot});

        for (TreeModelListener tml : listeners) {
            tml.treeStructureChanged(event);
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
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
