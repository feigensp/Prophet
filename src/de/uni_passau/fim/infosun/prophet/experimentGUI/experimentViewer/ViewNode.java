package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

public class ViewNode {

    private QTreeNode node;
    private QuestionViewPane viewPane;
    private StopwatchLabel stopwatch;

    public ViewNode(QTreeNode node) {
        this.node = node;
    }

    public QTreeNode getNode() {
        return node;
    }

    public QuestionViewPane getViewPane() {
        if (viewPane == null) {
            viewPane = new QuestionViewPane(node);
        }

        return viewPane;
    }

    public StopwatchLabel getStopwatch() {
        if (stopwatch == null) {
            stopwatch = new StopwatchLabel(node, getLocalized("STOPWATCHLABEL_CURRENT_TIME"));
        }

        return stopwatch;
    }
}
