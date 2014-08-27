package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentViewerPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.NoteEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.SettingsEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

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
                currentTab.load(currentNode);
            }
        });

        addChangeListener(event -> {
            save();
            currentTab = (ExperimentEditorTab) getSelectedComponent();
            currentTab.load(currentNode);
        });

        addTab(getLocalized("MENU_TAB_EDITOR"), null, new ContentEditorPanel(), null);
        addTab(getLocalized("MENU_TAB_PREVIEW"), null, new ContentViewerPanel(), null);
        addTab(getLocalized("MENU_TAB_SETTINGS"), null, new SettingsEditorPanel(), null);
        addTab(getLocalized("MENU_TAB_NOTES"), null, new NoteEditorPanel(), null);
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
