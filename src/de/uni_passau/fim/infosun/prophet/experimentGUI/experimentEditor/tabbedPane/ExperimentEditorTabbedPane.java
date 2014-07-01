package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentViewerPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.NoteEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.SettingsEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * The right-side tabbed pane of the Experiment Editor
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditorTabbedPane extends JTabbedPane {

    private QTreeNode currentNode;
    private ExperimentEditorTab currentTab;

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
                currentTab.activate(currentNode);
            }
        });

        addChangeListener(event -> {
            save();
            currentTab = (ExperimentEditorTab) getSelectedComponent();
            currentTab.activate(currentNode);
        });

        addEditorPanel(UIElementNames.MENU_TAB_EDITOR, new ContentEditorPanel());
        addEditorPanel(UIElementNames.MENU_TAB_PREVIEW, new ContentViewerPanel());
        addEditorPanel(UIElementNames.MENU_TAB_SETTINGS, new SettingsEditorPanel());
        addEditorPanel(UIElementNames.MENU_TAB_NOTES, new NoteEditorPanel());
    }

    /**
     * Adds the given <code>ExperimentEditorTab</code> to this <code>TabbedPane</code>.
     *
     * @param caption
     *         the caption for the <code>ExperimentEditorTab</code>
     * @param tab
     *         the <code>ExperimentEditorTab</code> to be added
     */
    private void addEditorPanel(String caption, ExperimentEditorTab tab) {
        addTab(caption, null, tab, null);
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
