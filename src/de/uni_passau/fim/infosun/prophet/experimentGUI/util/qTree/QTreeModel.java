package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.ArrayList;
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
     * @param root the root
     */
    public QTreeModel(QTreeNode root) {
        listeners = new ArrayList<>();
        this.root = root;
    }

    /**
     * Sets the root of the tree.
     *
     * @param newRoot the new root of the tree
     */
    public void setRoot(QTreeNode newRoot) {
        QTreeNode oldRoot = root;

        root = newRoot;
        fireTreeStructureChanged(oldRoot);
    }

    /**
     * Fires a <code>TreeModelEvent</code> indicating that the whole tree has changed.
     *
     * @param oldRoot the old root of the tree
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
        System.out.println("Value for " + path + " changed to " + newValue);
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
