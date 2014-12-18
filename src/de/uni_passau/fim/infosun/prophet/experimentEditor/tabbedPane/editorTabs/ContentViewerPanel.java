package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;

import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;

/**
 * Displays a preview of the rendered HTML of a <code>QTreeNode</code> as it would appear in the
 * <code>ExperimentViewer</code>.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ContentViewerPanel extends ExperimentEditorTab {

    private QuestionViewPane previewPane;

    /**
     * Constructs a new empty <code>ContentViewerPanel</code>.
     */
    public ContentViewerPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    public void load(QTreeNode selected) {

        if (previewPane != null) {
            remove(previewPane);
        }

        if (selected != null) {
            previewPane = new QuestionViewPane(selected);
            add(previewPane, BorderLayout.CENTER);
        }

        repaint();
    }

    @Override
    public void save() {
        // nothing to save
    }

    @Override
    public void reset() {
        previewPane = null;
        removeAll();
    }
}
