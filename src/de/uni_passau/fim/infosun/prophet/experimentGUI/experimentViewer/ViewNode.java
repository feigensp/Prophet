package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.event.ActionListener;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

/**
 * Wrapper class to hold all state necessary to properly handle displaying a <code>QTreeNode</code> in the
 * <code>EViewer</code>.
 */
public class ViewNode {

    private ActionListener listener;

    private QuestionViewPane viewPane;
    private StopwatchLabel stopwatch;

    private boolean entered;
    private QTreeNode treeNode;

    /**
     * Constructs a new <code>ViewNode</code> for displaying the given <code>treeNode</code>.
     *
     * @param treeNode the <code>QTreeNode</code> whose display state the <code>ViewNode</code> should hold
     * @param listener the <code>ActionListener</code> that will be added to the <code>QuestionViewPane</code> for the
     *                 node
     */
    public ViewNode(QTreeNode treeNode, ActionListener listener) {
        this.treeNode = treeNode;
        this.listener = listener;
        this.entered = false;
    }

    /**
     * Returns whether the <code>ViewNode</code> was entered.
     *
     * @return true iff it was entered
     */
    public boolean isEntered() {
        return entered;
    }

    /**
     * Sets whether the <code>ViewNode</code> was entered.
     *
     * @param entered the new state
     */
    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    /**
     * Returns the <code>QTreeNode</code> this <code>ViewNode</code> holds.
     *
     * @return the <code>QTreeNode</code>
     */
    public QTreeNode getTreeNode() {
        return treeNode;
    }

    /**
     * Returns the <code>QuestionViewPane</code> for this <code>ViewNode</code>. It will be created at the first call
     * to this method.
     *
     * @return the <code>QuestionViewPane</code>
     */
    public QuestionViewPane getViewPane() {
        if (viewPane == null) {
            viewPane = new QuestionViewPane(treeNode);

            if (listener != null) {
                viewPane.addActionListener(listener);
            }
        }

        return viewPane;
    }

    /**
     * Sets the <code>StopwatchLabel</code> for this <code>ViewNode</code>.
     *
     * @param stopwatch the new <code>StopwatchLabel</code>
     */
    public void setStopwatch(StopwatchLabel stopwatch) {
        this.stopwatch = stopwatch;
    }

    /**
     * Returns the <code>StopwatchLabel</code> for this <code>ViewNode</code>. If no <code>StopwatchLabel</code> has
     * been set before and this is the first call to the method a new one will be created.
     *
     * @return the <code>StopwatchLabel</code>
     */
    public StopwatchLabel getStopwatch() {
        if (stopwatch == null) {
            stopwatch = new StopwatchLabel(treeNode, getLocalized("STOPWATCHLABEL_CURRENT_TIME"));
        }

        return stopwatch;
    }
}
