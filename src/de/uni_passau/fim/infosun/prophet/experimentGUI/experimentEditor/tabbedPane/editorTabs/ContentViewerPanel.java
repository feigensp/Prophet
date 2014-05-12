package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * Preview of the Content entered in the ContentEditorPanel
 *
 * @author Andreas Hasselberg
 * @author Markus K�ppen
 */
@SuppressWarnings("serial")
public class ContentViewerPanel extends ExperimentEditorTab {

    /**
     * the pane featuring the preview
     */
    private QuestionViewPane viewerPane;

    /**
     * Constructor
     */
    public ContentViewerPanel() {
        setLayout(new BorderLayout());
        this.setOpaque(false);
    }

    /**
     * loads the current content and displays it appropriately, called by EditorTabbedPane
     */
    public void activate(QTreeNode selected) {
        removeAll();
        updateUI();
        if (selected != null) {
            viewerPane = new QuestionViewPane(selected);
            add(viewerPane, BorderLayout.CENTER);
        }
    }

    /**
     * saves any changes to the tree, i.e. does nothing, called by EditorTabbedPane
     */
    @Override
    public void save() {
    }
}
