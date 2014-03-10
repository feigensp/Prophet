package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class QTree extends JTree {

    // TODO store reference to the bundle in a central location
    private static final String baseName = "de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.LabelsBundle";
    private static final ResourceBundle resourceBundle;

    static {
        resourceBundle = ResourceBundle.getBundle(baseName);
    }

    /**
     * Popup menus for the different types of nodes.
     */
    private JPopupMenu experimentPopup;
    private JPopupMenu categoryPopup;
    private JPopupMenu questionPopup;

    /**
     * ActionCommand String constants for the popup menus.
     */
    private final static String ACTION_NEW_CATEGORY = "newcategory";
    private final static String ACTION_NEW_QUESTION = "newquestion";
    private final static String ACTION_RENAME = "rename";
    private final static String ACTION_REMOVE = "remove";
    private final static String ACTION_COPY = "copy";
    private final static String ACTION_PASTE = "paste";

    private QTreeModel model;

    public QTree(QTreeNode root) {
        super(new QTreeModel(root));

        model = (QTreeModel) getModel();
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        buildPopupMenus();
    }

    private void buildPopupMenus() {
        JMenuItem menuItem;
        ActionListener popupListener;

        experimentPopup = new JPopupMenu();
        categoryPopup = new JPopupMenu();
        questionPopup = new JPopupMenu();

        // action listener for handling all actions coming from the popup menus
        popupListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                QTreeNode selNode = (QTreeNode) getSelectionPath().getLastPathComponent();

                switch (e.getActionCommand()) {
                    case ACTION_NEW_CATEGORY: {
                        newCategory(selNode);
                    }
                    break;
                    case ACTION_NEW_QUESTION: {
                        newQuestion(selNode);
                    }
                    break;
                    case ACTION_RENAME: {
                        rename(selNode);
                    }
                    break;
                }
            }

            private void rename(QTreeNode selNode) {
                String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"));

                if (name == null) {
                    return;
                }

                selNode.setName(name);
            }

            private void newQuestion(QTreeNode selNode) {
                String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"));

                if (name == null) {
                    return;
                }

                model.addChild(selNode, new QTreeNode(selNode, QTreeNode.Type.QUESTION, name));
                expandPath(getSelectionPath());
            }

            private void newCategory(QTreeNode selNode) {
                String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"));

                if (name == null) {
                    return;
                }

                model.addChild(selNode, new QTreeNode(selNode, QTreeNode.Type.CATEGORY, name));
                expandPath(getSelectionPath());
            }
        };

        // setup for the experiment popup menu
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.NEW_CATEGORY"));
        menuItem.setActionCommand(ACTION_NEW_CATEGORY);
        menuItem.addActionListener(popupListener);
        experimentPopup.add(menuItem);
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        experimentPopup.add(menuItem);

        // setup for the category popup menu
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.NEW_QUESTION"));
        menuItem.setActionCommand(ACTION_NEW_QUESTION);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);

        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        questionPopup.add(menuItem);

        // mouse listener for showing the popup menus when a node of the tree is right clicked
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (!SwingUtilities.isRightMouseButton(e)) {
                    return;
                }

                int selX = e.getX();
                int selY = e.getY();
                TreePath path = getPathForLocation(selX, selY);

                if (path == null) {
                    return;
                }

                setSelectionPath(path);

                QTreeNode selNode = (QTreeNode) path.getLastPathComponent();

                switch (selNode.getType()) {

                    case EXPERIMENT:
                        experimentPopup.show(QTree.this, selX, selY);
                        break;
                    case CATEGORY:
                        categoryPopup.show(QTree.this, selX, selY);
                        break;
                    case QUESTION:
                        questionPopup.show(QTree.this, selX, selY);
                        break;
                }
            }
        });
    }
}
