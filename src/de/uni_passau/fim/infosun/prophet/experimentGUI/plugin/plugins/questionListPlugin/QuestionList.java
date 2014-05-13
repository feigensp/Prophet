package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.questionListPlugin;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * Creates a panel which consits JList's. The Lists represents categories and
 * every item in the list represents a question
 *
 * @author Markus K�ppen, Andreas Hasselberg
 */
public class QuestionList extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private QTreeNode root;
    private JTree tree;

    /**
     * Constructor which creates an Empty Panel
     * @param root
     */
    public QuestionList(QTreeNode root) {
        this.root = root;
        this.tree = new JTree();

        QTreeModel model = new QTreeModel(root);

        tree.setModel(model);
        tree.setEnabled(false);
        tree.setCellRenderer(new SimpleTreeCellRenderer());

        // expand all
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        this.setViewportView(tree);
    }

    public void visit(QTreeNode selectionNode) {
        //TODO why do the search?

//        Enumeration e = root.breadthFirstEnumeration();
//
//        while (e.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
//
//            if (node.equals(selectionNode)) {
//                TreePath path = new TreePath(node.getPath());
//                tree.setSelectionPath(path);
//                break;
//            }
//        }

        Object[] path = ((QTreeModel) tree.getModel()).buildPath(selectionNode, true);
        tree.setSelectionPath(new TreePath(path));
    }
}
