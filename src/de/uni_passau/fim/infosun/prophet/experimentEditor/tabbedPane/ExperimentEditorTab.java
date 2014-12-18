package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane;

import javax.swing.JPanel;

import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;

/**
 * A <code>JPanel</code> used as a tab in the <code>ExperimentEditorTabbedPane</code>.
 * Adds methods to load/save data from/to a <code>QTreeNode</code>.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public abstract class ExperimentEditorTab extends JPanel {

    /**
     * Loads any data the tab is interested in from the given <code>QTreeNode</code>.
     *
     * @param selected
     *         the <code>QTreeNode</code> to load from
     */
    public abstract void load(QTreeNode selected);

    /**
     * Saves the data in the tab to the last <code>QTreeNode</code> {@link ExperimentEditorTab#load(QTreeNode)}
     * was called with.
     */
    public abstract void save();

    /**
     * Resets the tab by clearing all caches and displaying an empty panel.
     */
    public abstract void reset();
}
