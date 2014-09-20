package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.questionListPlugin;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * A <code>JTree</code> that shows a fully expanded view of an experiment tree.
 */
public class QuestionList extends JTree {

    /**
     * A <code>TreeCellRenderer</code> that uses a circle as an icon for all cells and enables them regardless of
     * whether the tree is enabled.
     */
    TreeCellRenderer renderer = new DefaultTreeCellRenderer() {

        Icon icon = new ListIcon(ListIcon.CIRCLE);

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            setIcon(icon);
            setEnabled(true);

            return this;
        }
    };

    /**
     * Constructs a new <code>QuestionList</code> displaying the experiment tree with the given <code>root</code>.
     *
     * @param root
     *         the root of the tree to display
     */
    public QuestionList(QTreeNode root) {
        super(new QTreeModel(root));

        setEnabled(false);
        setCellRenderer(renderer);

        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }

        setSelectionPath(root);
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
