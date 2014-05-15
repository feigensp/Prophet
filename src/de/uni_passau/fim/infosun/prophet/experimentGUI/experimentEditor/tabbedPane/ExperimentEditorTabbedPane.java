package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentViewerPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.NoteEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.SettingsEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * The right-side tabbed pane of the Experiment Editor
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
@SuppressWarnings("serial")
public class ExperimentEditorTabbedPane extends JTabbedPane {

    /**
     * the currently selected QuestionTreeNode
     */
    private QTreeNode selected;
    /**
     * the currently open ExperimentEditorTab
     */
    private ExperimentEditorTab currentTab;

    /**
     * Constructor
     */
    public ExperimentEditorTabbedPane() {
        addChangeListener(event -> {
            if (currentTab != null) {
                save();
            }
            activate();
            currentTab = (ExperimentEditorTab) getSelectedComponent();
        });

        addEditorPanel(UIElementNames.MENU_TAB_EDITOR, new ContentEditorPanel());
        addEditorPanel(UIElementNames.MENU_TAB_PREVIEW, new ContentViewerPanel());
        addEditorPanel(UIElementNames.MENU_TAB_SETTINGS, new SettingsEditorPanel());
        addEditorPanel(UIElementNames.MENU_TAB_NOTES, new NoteEditorPanel());
    }

    /**
     * Adds a new ExperimentEditorTab the the TabbedPane
     *
     * @param caption
     *         caption of the new tab
     * @param panel
     *         the tab to be added
     */
    public void addEditorPanel(String caption, ExperimentEditorTab panel) {
        addTab(caption, null, panel, null);
    }

    /**
     * Called upon change in the currently selected QuestionTreeNode. Saves the current tab and then loads the new
     * node.
     *
     * @param selected
     */
    public void setSelected(QTreeNode selected) {
        save();
        this.selected = selected;
        activate();
    }

    /**
     * Informs the currently active editor tab that a new node might have been opened (reload).
     */
    private void activate() {
        ((ExperimentEditorTab) getSelectedComponent()).activate(selected);
    }

    /**
     * saves the current tab
     */
    public void save() {
        currentTab.save();
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
