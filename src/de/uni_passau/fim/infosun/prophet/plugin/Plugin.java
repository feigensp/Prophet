package de.uni_passau.fim.infosun.prophet.plugin;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * Plugins for PROPHET implement this interface. The <code>Plugin</code> methods will be called by the
 * <code>EViewer</code> as specified in the method documentation.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public interface Plugin {

    /**
     * Returns the <code>Setting</code> object of the <code>Plugin</code> for the given <code>Node</code>.
     *
     * @param node
     *         the node to get the <code>Setting</code> for
     *
     * @return a <code>Setting</code> object representing the settings of the plugin or <code>null</code> if there are
     *         none
     */
    public Setting getSetting(QTreeNode node);

    /**
     * Called after the <code>EViewer</code> has been initialized but before the first node is entered. The
     * <code>Plugin</code> may modify the <code>EViewer</code> in this method.
     *
     * @param experimentViewer
     *         the initialized experiment viewer
     */
    public void experimentViewerRun(EViewer experimentViewer);

    /**
     * Called before a new node is entered. The <code>Plugin</code> may request that this node be skipped by returning
     * <code>true</code>.
     *
     * @param node
     *         the node to be entered
     *
     * @return true iff this <code>Plugin</code> denies entry to the given <code>node</code>
     */
    public boolean denyEnterNode(QTreeNode node);

    /**
     * Called when a node is entered.
     *
     * @param node
     *         the entered node
     */
    public void enterNode(QTreeNode node);

    /**
     * Called before a node is exited. The <code>Plugin</code> may indicate that the <code>currentNode</code>
     * should not be exited by returning a non-null <code>String</code> (the reason why). This <code>String</code>
     * will be shown to the user in an alert message box.
     *
     * @param currentNode
     *         the node to be exited
     *
     * @return <code>null</code> if the node may be exited, otherwise a reason for denying the exit
     */
    public String denyNextNode(QTreeNode currentNode);

    /**
     * Called when all children of a node have been exited. This means that the <code>exitNode(node)</code> call for the
     * root of the experiment tree will be made last. Nodes will only be exited if they have been entered previously.
     *
     * @param node
     *         the exited node
     */
    public void exitNode(QTreeNode node);

    /**
     * Called after the experiment is finished. The <code>Plugin</code> may provide a message to be shown to the user.
     *
     * @return a message shown to the user
     */
    public String finishExperiment();
}
