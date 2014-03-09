package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class QTree extends JTree {

    QTreeModel model;

    public QTree(QTreeNode root) {
        super(new QTreeModel(root));

        model = (QTreeModel) getModel();
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selectedPath = e.getPath();
                QTreeNode selectedNode = (QTreeNode) selectedPath.getLastPathComponent();

                selectedNode.addChild(new QTreeNode(selectedNode, QTreeNode.Type.CATEGORY, "Name"));
            }
        });
    }
}
