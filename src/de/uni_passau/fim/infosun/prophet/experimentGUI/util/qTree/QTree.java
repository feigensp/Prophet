package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.*;

/**
 * A <code>JTree</code> subclass displaying a tree of <code>QTreeNode</code> objects.
 */
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
    private JPopupMenu treePopup;

    /**
     * Menu items and the reference to the clipboard reference to support copy and paste.
     */
    private JMenuItem experimentPasteItem;
    private JMenuItem categoryPasteItem;
    private QTreeNode clipboard;

    /**
     * ActionCommand String constants for the popup menus.
     */
    private final static String ACTION_NEW_CATEGORY = "newcategory";
    private final static String ACTION_NEW_QUESTION = "newquestion";
    private final static String ACTION_NEW_EXPERIMENT = "newexperiment";
    private final static String ACTION_RENAME = "rename";
    private final static String ACTION_REMOVE = "remove";
    private final static String ACTION_COPY = "copy";
    private final static String ACTION_PASTE = "paste";

    private QTreeModel model;

    /**
     * Constructs a new <code>QTree</code>.
     */
    public QTree() {
        super(new QTreeModel(null));

        model = (QTreeModel) getModel();
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        buildPopupMenus();

        try {
            setupDragAndDrop();
        } catch (TooManyListenersException ignored) {
        }

        setupShortcuts();
    }

    private void setupShortcuts() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (!e.isControlDown()) {
                    return;
                }

                TreePath selPath = getSelectionPath();

                if (selPath == null) {
                    return;
                }

                QTreeNode selNode = (QTreeNode) selPath.getLastPathComponent();

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_N:
                        if (selNode.getType() == EXPERIMENT) {
                            newCategory(selNode);
                        } else if (selNode.getType() == CATEGORY) {
                            newQuestion(selNode);
                        }
                        break;
                    case KeyEvent.VK_R:
                        rename(selNode);
                        break;
                    case KeyEvent.VK_Q:
                        remove(selNode);
                        break;
                    case KeyEvent.VK_C:
                        copy(selNode);
                        break;
                    case KeyEvent.VK_V:
                        paste(selNode);
                        break;
                }
            }
        });
    }

    /**
     * Sets up the drag and drop actions for the tree.
     *
     * @throws TooManyListenersException
     *         if a <code>DropTargetListener</code> is already added to the <code>QTree</code>
     */
    private void setupDragAndDrop() throws TooManyListenersException {

        setDragEnabled(true);
        setDropTarget(new DropTarget());

        getDropTarget().addDropTargetListener(new DropTargetAdapter() {

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                QTreeNode dragComponent = (QTreeNode) getSelectionPath().getLastPathComponent();

                // you can't drag the experiment node
                if (dragComponent.getType() == EXPERIMENT) {
                    dtde.rejectDrag();
                    return;
                }

                Point mouseLocation = dtde.getLocation();
                TreePath selPath = getPathForLocation(mouseLocation.x, mouseLocation.y);

                // you can only drag onto a tree component
                if (selPath == null) {
                    dtde.rejectDrag();
                    return;
                }

                QTreeNode target = (QTreeNode) selPath.getLastPathComponent();

                // you can't drag onto the experiment node
                if (target.getType() == EXPERIMENT) {
                    dtde.rejectDrag();

                    // you can't drag a category onto a question
                } else if (dragComponent.getType() == CATEGORY && target.getType() != CATEGORY) {
                    dtde.rejectDrag();
                } else {
                    dtde.acceptDrag(dtde.getDropAction());
                }
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                Point dropLocation = dtde.getLocation();
                TreePath dropPath = getPathForLocation(dropLocation.x, dropLocation.y);
                QTreeNode dragComponent = (QTreeNode) getSelectionPath().getLastPathComponent();

                if (dropPath == null) {
                    dtde.rejectDrop();
                    return;
                }

                QTreeNode target = (QTreeNode) dropPath.getLastPathComponent();

                switch (dragComponent.getType()) {

                    case CATEGORY: {
                        moveCategory(dragComponent, target);
                    }
                    break;
                    case QUESTION: {
                        moveQuestion(dragComponent, target);
                    }
                    break;
                }

                dtde.dropComplete(true);
            }
        });
    }

    /**
     * Builds the popup menus and sets up the actions available through them.
     */
    private void buildPopupMenus() {
        JMenuItem menuItem;
        ActionListener popupListener;

        experimentPopup = new JPopupMenu();
        categoryPopup = new JPopupMenu();
        questionPopup = new JPopupMenu();
        treePopup = new JPopupMenu();

        // action listener for handling all actions coming from the popup menus
        popupListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                QTreeNode selNode = null;
                TreePath selPath = getSelectionPath();

                if (selPath != null) {
                    selNode = (QTreeNode) selPath.getLastPathComponent();
                }

                switch (e.getActionCommand()) {
                    case ACTION_NEW_CATEGORY: {
                        newCategory(selNode);
                    }
                    break;
                    case ACTION_NEW_QUESTION: {
                        newQuestion(selNode);
                    }
                    break;
                    case ACTION_NEW_EXPERIMENT: {
                        newExperiment();
                    }
                    break;
                    case ACTION_RENAME: {
                        rename(selNode);
                    }
                    break;
                    case ACTION_REMOVE: {
                        remove(selNode);
                    }
                    break;
                    case ACTION_COPY: {
                        copy(selNode);
                    }
                    break;
                    case ACTION_PASTE: {
                        paste(selNode);
                    }
                    break;
                }
            }
        };

        // setup for the experiment popup menu
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.NEW_CATEGORY"));
        menuItem.setActionCommand(ACTION_NEW_CATEGORY);
        menuItem.addActionListener(popupListener);
        experimentPopup.add(menuItem);
        experimentPopup.add(new JPopupMenu.Separator());
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        experimentPopup.add(menuItem);
        experimentPopup.add(new JPopupMenu.Separator());
        experimentPasteItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.PASTE"));
        experimentPasteItem.setActionCommand(ACTION_PASTE);
        experimentPasteItem.addActionListener(popupListener);
        experimentPasteItem.setEnabled(false);
        experimentPopup.add(experimentPasteItem);

        // setup for the category popup menu
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.NEW_QUESTION"));
        menuItem.setActionCommand(ACTION_NEW_QUESTION);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);
        categoryPopup.add(new JPopupMenu.Separator());
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.REMOVE"));
        menuItem.setActionCommand(ACTION_REMOVE);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);
        categoryPopup.add(new JPopupMenu.Separator());
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.COPY"));
        menuItem.setActionCommand(ACTION_COPY);
        menuItem.addActionListener(popupListener);
        categoryPopup.add(menuItem);
        categoryPasteItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.PASTE"));
        categoryPasteItem.setActionCommand(ACTION_PASTE);
        categoryPasteItem.addActionListener(popupListener);
        categoryPasteItem.setEnabled(false);
        categoryPopup.add(categoryPasteItem);

        // setup for the question popup menu
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.RENAME"));
        menuItem.setActionCommand(ACTION_RENAME);
        menuItem.addActionListener(popupListener);
        questionPopup.add(menuItem);
        questionPopup.add(new JPopupMenu.Separator());
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.REMOVE"));
        menuItem.setActionCommand(ACTION_REMOVE);
        menuItem.addActionListener(popupListener);
        questionPopup.add(menuItem);
        questionPopup.add(new JPopupMenu.Separator());
        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.COPY"));
        menuItem.setActionCommand(ACTION_COPY);
        menuItem.addActionListener(popupListener);
        questionPopup.add(menuItem);

        menuItem = new JMenuItem(resourceBundle.getString("TREE.POPUP.NEW_EXPERIMENT"));
        menuItem.setActionCommand(ACTION_NEW_EXPERIMENT);
        menuItem.addActionListener(popupListener);
        treePopup.add(menuItem);

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
                    treePopup.show(QTree.this, selX, selY);
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

    /**
     * Moves a category. Categories can only be dragged onto other categories and will be placed after the
     * node <code>target</code> in the targets parent.
     *
     * @param dragComponent
     *         the component that is being dragged
     * @param target
     *         the component <code>dragComponent</code> is being dropped on
     */
    private void moveCategory(QTreeNode dragComponent, QTreeNode target) {
        model.removeFromParent(dragComponent);

        int index = target.getParent().getIndexOfChild(target) + 1;

        dragComponent.setParent(target.getParent());
        model.addChild(target.getParent(), dragComponent, index);
    }

    /**
     * Moves a question. Questions can be dragged onto categories or other questions. If the are dragged onto
     * a category they will be added as the last child of the category, if the are dragged onto a question
     * they will be added after the node <code>target</code> in the targets parent.
     *
     * @param dragComponent
     *         the component that is being dragged
     * @param target
     *         the component <code>dragComponent</code> is being dropped on
     */
    private void moveQuestion(QTreeNode dragComponent, QTreeNode target) {

        switch (target.getType()) {

            case CATEGORY: {
                model.removeFromParent(dragComponent);

                int index = target.getChildCount();

                dragComponent.setParent(target);
                model.addChild(target, dragComponent, index);
            }
            break;
            case QUESTION: {
                model.removeFromParent(dragComponent);

                int index = target.getParent().getIndexOfChild(target) + 1;

                dragComponent.setParent(target.getParent());
                model.addChild(target.getParent(), dragComponent, index);
            }
            break;
        }
    }

    /**
     * Copies the given node.
     *
     * @param selNode
     *         the node to be copied
     */
    private void copy(QTreeNode selNode) {
        clipboard = selNode;

        experimentPasteItem.setEnabled(true);
        categoryPasteItem.setEnabled(true);
    }

    /**
     * Pasts the node currently in <code>clipboard</code> into the given node.
     *
     * @param selNode
     *         the node into which to paste
     */
    private void paste(QTreeNode selNode) {
        boolean categoryToExperiment = selNode.getType() == EXPERIMENT && clipboard.getType() == CATEGORY;
        boolean questionToCategory = selNode.getType() == CATEGORY && clipboard.getType() == QUESTION;

        if (categoryToExperiment || questionToCategory) {
            QTreeNode copy;

            try {
                copy = (QTreeNode) clipboard.clone();
            } catch (CloneNotSupportedException e) {
                JOptionPane.showMessageDialog(QTree.this, resourceBundle.getString("TREE.POPUP.COPY_FAILED"),
                        resourceBundle.getString("TREE.POPUP.ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            copy.setParent(selNode);
            model.addChild(selNode, copy);
        } else {
            JOptionPane.showMessageDialog(QTree.this, resourceBundle.getString("TREE.POPUP.COPY_IMPOSSIBLE"),
                    resourceBundle.getString("TREE.POPUP.ERROR"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Replaces the current experiment with a new one.
     */
    private void newExperiment() {

        if (model.getRoot() != null) {
            int choice = JOptionPane
                    .showOptionDialog(QTree.this, resourceBundle.getString("TREE.POPUP.CONFIRM.NEW_EXPERIMENT"),
                            resourceBundle.getString("TREE.POPUP.NEW_EXPERIMENT"), JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, null, null);

            if (choice != JOptionPane.OK_OPTION) {
                return;
            }
        }

        String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"), resourceBundle.getString("TREE.POPUP.NEW_EXPERIMENT"), JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }

        model.setRoot(new QTreeNode(null, EXPERIMENT, name));
    }

    /**
     * Removes the given node from the tree.
     *
     * @param selNode
     *         the node to be removed
     */
    private void remove(QTreeNode selNode) {
        model.removeFromParent(selNode);
    }

    /**
     * Renames the given node.
     *
     * @param selNode
     *         the node to be renamed
     */
    private void rename(QTreeNode selNode) {
        String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"), resourceBundle.getString("TREE.POPUP.RENAME"), JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }

        model.rename(selNode, name);
    }

    /**
     * Adds a new question to the given node.
     *
     * @param selNode
     *         the node to which a new question is to be added
     */
    private void newQuestion(QTreeNode selNode) {
        String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"),
                resourceBundle.getString("TREE.POPUP.NEW_QUESTION"), JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }

        model.addChild(selNode, new QTreeNode(selNode, QUESTION, name));
        expandPath(getSelectionPath());
    }

    /**
     * Adds a new category to the given node.
     *
     * @param selNode
     *         the node to which a new category is to be added
     */
    private void newCategory(QTreeNode selNode) {
        String name = JOptionPane.showInputDialog(QTree.this, resourceBundle.getString("TREE.POPUP.NAME"),
                resourceBundle.getString("TREE.POPUP.NEW_CATEGORY"), JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }

        model.addChild(selNode, new QTreeNode(selNode, CATEGORY, name));
        expandPath(getSelectionPath());
    }
}
