package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentEditor.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.ContentViewerPanel;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.NoteEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.SettingsEditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * The right-side tabbed pane of the <code>ExperimentEditor</code>.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditorTabbedPane extends JTabbedPane {

    private QTreeNode currentNode;
    private ExperimentEditorTab currentTab;

    private static class TreeModelAdapter implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {

        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {

        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {

        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {

        }
    }

    /**
     * Constructs a new <code>ExperimentEditorTabbedPane</code>.
     *
     * @param tree
     *         the <code>QTree</code> used in the <code>ExperimentEditor</code>
     */
    public ExperimentEditorTabbedPane(QTree tree) {

        tree.addTreeSelectionListener(event -> {
            TreePath selectionPath = tree.getSelectionPath();

            if (selectionPath != null) {
                save();
                currentNode = (QTreeNode) selectionPath.getLastPathComponent();
                currentTab.load(currentNode);
            }
        });

        tree.getModel().addTreeModelListener(new TreeModelAdapter() {

            @Override
            public void treeStructureChanged(TreeModelEvent e) {

                if (e.getPath().length > 0) {
                    Object oldRoot = e.getPath()[0];
                    Object currentRoot = tree.getModel().getRoot();

                    if (oldRoot != currentRoot) {

                        for (int i = 0; i < getTabCount(); i++) {
                            ((ExperimentEditorTab) getComponentAt(i)).reset();
                        }
                    }
                }
            }
        });

        addChangeListener(event -> {
            save();
            currentTab = (ExperimentEditorTab) getSelectedComponent();
            currentTab.load(currentNode);
        });

        addTab(getLocalized("MENU_TAB_EDITOR"), new ContentEditorPanel());
        addTab(getLocalized("MENU_TAB_PREVIEW"), new ContentViewerPanel());
        addTab(getLocalized("MENU_TAB_SETTINGS"), new SettingsEditorPanel());
        addTab(getLocalized("MENU_TAB_NOTES"), new NoteEditorPanel());
    }

    /**
     * Saves the state of the currently selected tab.
     */
    public void save() {
        if (currentTab != null) {
            currentTab.save();
        }
    }

    /**
     * Makes the given component and all its descendants transparent (looks better)
     *
     * @param component
     *         the component to be made transparent
     */
    public static void recursiveSetOpaque(JComponent component) {
        component.setOpaque(false);
        for (Component child : component.getComponents()) {
            if (child instanceof JComponent) {
                recursiveSetOpaque((JComponent) child);
            }
        }
    }
}
