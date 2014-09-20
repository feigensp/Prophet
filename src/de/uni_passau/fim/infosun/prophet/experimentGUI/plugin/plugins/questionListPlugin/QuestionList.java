package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.questionListPlugin;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * A <code>JTree</code> that shows a fully expanded view of an experiment tree.
 */
public class QuestionList extends JTree {

    /**
     * Constructs a new <code>QuestionList</code> displaying the experiment tree with the given <code>root</code>.
     *
     * @param root
     *         the root of the tree to display
     */
    public QuestionList(QTreeNode root) {
        super(new QTreeModel(root));

        setEnabled(false);
        setCellRenderer(new SimpleTreeCellRenderer());

        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    /**
     * Selects the node <code>toSelect</code>.
     *
     * @param toSelect
     *         the <code>QTreeNode</code> that is to be selected.
     */
    public void setSelectionPath(QTreeNode toSelect) {
        super.setSelectionPath(new TreePath(QTreeModel.buildPath(toSelect, true)));
    }
}
