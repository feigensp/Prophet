package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.event.ActionListener;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

public class ViewNode {

    private ActionListener listener;

    private QuestionViewPane viewPane;
    private StopwatchLabel stopwatch;

    private boolean entered;
    private QTreeNode treeNode;

    public ViewNode(QTreeNode treeNode, ActionListener listener) {
        this.treeNode = treeNode;
        this.listener = listener;
        this.entered = false;
    }

    public boolean isEntered() {
        return entered;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public QTreeNode getTreeNode() {
        return treeNode;
    }

    public QuestionViewPane getViewPane() {
        if (viewPane == null) {
            viewPane = new QuestionViewPane(treeNode);

            if (listener != null) {
                viewPane.addActionListener(listener);
            }
        }

        return viewPane;
    }

    public void setStopwatch(StopwatchLabel stopwatch) {
        this.stopwatch = stopwatch;
    }

    public StopwatchLabel getStopwatch() {
        if (stopwatch == null) {
            stopwatch = new StopwatchLabel(treeNode, getLocalized("STOPWATCHLABEL_CURRENT_TIME"));
        }

        return stopwatch;
    }
}
