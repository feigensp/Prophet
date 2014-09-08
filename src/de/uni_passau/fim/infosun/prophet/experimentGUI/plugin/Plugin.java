package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * Interface for all plugins to be used immediately within the product. It represents methods called on special
 * occasions.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public interface Plugin {

    /**
     * Returns the <code>Setting</code> object of this <code>Plugin</code> for the given <code>Node</code>
     *
     * @param node
     *         the currently active node within the experiment viewer
     *
     * @return a <code>Setting</code> object representing the settings of the plugin or null if there are
     * none
     */
    public Setting getSetting(QTreeNode node);

    /**
     * Called once when the experiment viewer is initialized. May be used to manipulate the viewer.
     *
     * @param experimentViewer
     *         The initialized experiment viewer
     */
    public void experimentViewerRun(EViewer experimentViewer);

    /**
     * Called right before a new node is entered. If any plugin denies the currentNode to be entered (e.g. because of a
     * timeout), it will just be skipped.
     *
     * @param node
     *         The node to be entered
     *
     * @return true if the node shall not be visited<br>
     * false if it might be visited
     */
    public boolean denyEnterNode(QTreeNode node);

    /**
     * Called when a node is entered, at the beginning of categories or questions. Is not called if a plugin denied the
     * entrance of that node.
     *
     * @param node
     *         The node entered
     */
    public void enterNode(QTreeNode node);

    /**
     * Called right before a new node is opened in the ExperimentViewer. If any plugin denies the currentNode to be
     * finished (for example because needed answers are missing) the next Node won't be opened.
     *
     * @param currentNode
     *         The node to be finished
     *
     * @return A message shown to the subject to indicate what needs to be done to accept finishing this node (e.g.
     * enter a needed answer)
     */
    public String denyNextNode(QTreeNode currentNode);

    /**
     * Called when a node is exited, i.e. after a question, after all active questions of a category are exited, when
     * the Experiment is finished (but before finishExperiment()). Is not called if node was not entered previously.
     *
     * @param node
     *         The node to be exited
     */
    public void exitNode(QTreeNode node);

    /**
     * Called after all nodes have been visited. Allows the plugin to do last steps upon finishing the experiment
     *
     * @return A message shown to the subject at experiment's end
     */
    public String finishExperiment();
}
