package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.xml.QTreeXMLHandler;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.Constants.*;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;
import static javax.swing.JOptionPane.*;

/**
 * A viewer for the experiments created with the <code>ExperimentEditor</code>.
 *
 * @author Georg Seibt
 */
public class EViewer extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int LOAD_FAIL_EXIT_STATUS = 1;
    private static final String DEFAULT_EXP_CODE = "default";

    private QTreeNode expTreeRoot;
    private List<ViewNode> experiment; // the experiment tree in pre-order
    private int currentIndex; // index into the 'experiment' List

    private JPanel timePanel;

    private File saveDir;

    /**
     * An <code>ActionListener</code> that is added to all <code>QuestionViewPane</code> instances created by the
     * <code>EViewer</code>. Calls the {@link #nextNode(boolean, boolean)} or {@link #previousNode(boolean, boolean)}
     * methods after there was an appropriate event.
     */
    private ActionListener listener = event -> {
        switch (event.getActionCommand()) {
            case KEY_FORWARD:
                nextNode(false, false);
                break;
            case KEY_BACKWARD:
                previousNode(false, false);
                break;
            default:
                System.err.println("Unrecognized action command from " + QuestionViewPane.class.getSimpleName() +
                        ". Action command: " + event.getActionCommand());
        }
    };

    /**
     * Constructs a new <code>EViewer</code> and starts the experiment.
     */
    public EViewer() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        Function<QTreeNode, ViewNode> mapper = node -> new ViewNode(node, listener);

        this.expTreeRoot = loadExperiment();
        this.experiment = expTreeRoot.preOrder().stream().map(mapper).collect(Collectors.toList()); // rtt ArrayList
        this.currentIndex = 0;
        this.timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        StopwatchLabel totalTime = new StopwatchLabel(expTreeRoot, getLocalized("STOPWATCHLABEL_TOTAL_TIME"));
        totalTime.start();
        this.timePanel.add(totalTime);

        ViewNode expNode = experiment.get(currentIndex);
        expNode.setStopwatch(totalTime);
        expNode.setEntered(true);

        add(expNode.getViewPane(), BorderLayout.CENTER);
        add(timePanel, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                String message = getLocalized("EVIEWER_EXPERIMENT_NOT_FINISHED");
                int choice = JOptionPane.showConfirmDialog(EViewer.this, message, null, JOptionPane.YES_NO_OPTION);

                if (choice == YES_OPTION) {
                    experiment.get(currentIndex).getViewPane().saveCurrentAnswersToNode();
                }
            }
        });

        PluginList.experimentViewerRun(this);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Tries to advance to the next node of the experiment. If this leads to exiting the last visitable node the
     * experiment will end. Since plugins may disallow exiting the current node or entering other nodes this method
     * may not advance at all or skip nodes.
     *
     * @param saveAnswers whether to save answers of the current node before advancing
     * @param ignoreDeny whether to ignore plugins denying exiting the current node
     */
    public void nextNode(boolean saveAnswers, boolean ignoreDeny) {
        ViewNode currentViewNode = experiment.get(currentIndex);
        QTreeNode currentNode = currentViewNode.getTreeNode();
        ViewNode newViewNode;
        String message;
        int newIndex = currentIndex;
        boolean doNotShow;

        if (saveAnswers) {
            currentViewNode.getViewPane().saveCurrentAnswersToNode();
        }

        if (!ignoreDeny && (message = PluginList.denyNextNode(currentNode)) != null) {
            JOptionPane.showMessageDialog(this, message, null, INFORMATION_MESSAGE);
            return;
        }

        // make sure that a subject code was entered before leaving the first node
        if (currentNode.getType() == QTreeNode.Type.EXPERIMENT && currentNode.equals(expTreeRoot)) {
            String[] answers = currentNode.getAnswers(KEY_SUBJECT);

            if (answers == null || answers.length < 1) {
                JOptionPane.showMessageDialog(this, getLocalized("EVIEWER_NO_SUBJECT_CODE"), null, ERROR_MESSAGE);
                return;
            }
        }

        do {
            if (++newIndex >= experiment.size()) {
                endExperiment();
                return;
            }

            newViewNode = experiment.get(newIndex);
            if (newViewNode.isEntered()) {
                PluginList.exitNode(newViewNode.getTreeNode());
            }

            doNotShow = Boolean.parseBoolean(newViewNode.getTreeNode().getAttribute(KEY_DONOTSHOWCONTENT).getValue());
        } while (doNotShow || PluginList.denyEnterNode(newViewNode.getTreeNode()));

        switchNode(newIndex);
    }

    /**
     * Tries to regress to the previous node of the experiment. Since plugins may disallow exiting the current node or
     * entering other nodes this method may not regress at all or skip nodes. Will not regress past the first
     * visitable node after the experiment root.
     *
     * @param saveAnswers whether to save answers of the current node before advancing
     * @param ignoreDeny whether to ignore plugins denying exiting the current node
     */
    public void previousNode(boolean saveAnswers, boolean ignoreDeny) {
        ViewNode currentViewNode = experiment.get(currentIndex);
        QTreeNode currentNode = currentViewNode.getTreeNode();
        ViewNode newViewNode;
        String message;
        int newIndex = currentIndex;
        boolean doNotShow;

        if (saveAnswers) {
            currentViewNode.getViewPane().saveCurrentAnswersToNode();
        }

        if (!ignoreDeny && (message = PluginList.denyNextNode(currentNode)) != null) {
            JOptionPane.showMessageDialog(this, message, null, INFORMATION_MESSAGE);
            return;
        }

        do {
            if (--newIndex <= 0) {
                return;
            }

            newViewNode = experiment.get(newIndex);
            if (newViewNode.isEntered()) {
                PluginList.exitNode(newViewNode.getTreeNode());
            }

            doNotShow = Boolean.parseBoolean(newViewNode.getTreeNode().getAttribute(KEY_DONOTSHOWCONTENT).getValue());
        } while (doNotShow || PluginList.denyEnterNode(newViewNode.getTreeNode()));

        switchNode(newIndex);
    }

    /**
     * Switches from displaying the node at <code>currentIndex</code> to <code>newIndex</code>.
     * This method maintains the <code>StopwatchLabel</code> states and notifies the <code>Plugin</code> instances of
     * exit from <code>currentIndex</code> and entry into <code>newIndex</code>.
     *
     * @param newIndex the index of the new node to display
     */
    private void switchNode(int newIndex) {

        if (currentIndex == newIndex) {
            return;
        }

        setEnabled(false);

        ViewNode oldNode = experiment.get(currentIndex);
        ViewNode newNode = experiment.get(newIndex);

        // the root node counts the total time
        if (currentIndex != 0) {
            oldNode.getStopwatch().pause();
            timePanel.remove(oldNode.getStopwatch());
        }
        newNode.getStopwatch().start();
        timePanel.add(newNode.getStopwatch());

        newNode.setEntered(true);
        remove(oldNode.getViewPane());
        add(newNode.getViewPane(), BorderLayout.CENTER);

        PluginList.exitNode(oldNode.getTreeNode());
        PluginList.enterNode(newNode.getTreeNode());
        currentIndex = newIndex;

        repaint();
        setEnabled(true);
    }

    /**
     * Ends the experiment and shows a dialog containing the <code>Plugin</code> messages.
     * The Window will then close.
     */
    private void endExperiment() {
        experiment.get(0).getStopwatch().pause();
        experiment.get(currentIndex).getStopwatch().pause();

        String message = "<html><p>" + getLocalized("EVIEWER_EXPERIMENT_FINISHED") + "</p></html>";

        try {
            QTreeXMLHandler.saveAnswerXML(expTreeRoot, new File(getSaveDir(), FILE_ANSWERS));
        } catch (IOException e) {
            System.err.println("Could not save the answers.xml. " + e);
        }

        message += PluginList.finishExperiment();

        setEnabled(false);
        JOptionPane.showMessageDialog(this, message, null, INFORMATION_MESSAGE);

        dispose();
    }

    /**
     * Locates the experiment XML file and deserializes it. If no experiment XML can be found or there is an error
     * deserializing the XML file this method will terminate the JVM with exit status 1.
     *
     * @return the deserialized <code>QTreeNode</code>
     */
    private QTreeNode loadExperiment() {
        File experimentFile = new File(DEFAULT_FILE);

        if (!experimentFile.exists()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(getLocalized("EVIEWER_XML_FILES"), "*.xml"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                experimentFile = fileChooser.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(this, getLocalized("EVIEWER_NO_EXPERIMENT_CHOSEN"));
                System.exit(LOAD_FAIL_EXIT_STATUS);
            }

            if (!experimentFile.getParentFile().equals(new File("."))) {
                JOptionPane.showMessageDialog(this, getLocalized("EVIEWER_EXPERIMENT_NOT_IN_WORKING_DIR"));
                System.exit(LOAD_FAIL_EXIT_STATUS);
            }
        }

        QTreeNode treeRoot = QTreeXMLHandler.loadExperimentXML(experimentFile);

        if (treeRoot == null) {
            JOptionPane.showMessageDialog(this, getLocalized("MESSAGE_NO_VALID_EXPERIMENT_FILE"));
            System.exit(LOAD_FAIL_EXIT_STATUS);
        }

        return treeRoot;
    }

    /**
     * Returns the root <code>QTreeNode</code> of the experiment tree.
     *
     * @return the root node
     */
    public QTreeNode getExperimentTree() {
        return expTreeRoot;
    }

    /**
     * Returns the directory in which the <code>EViewer</code> stores the date resulting from the current experiment
     * run. This method will return <code>null</code> if the experimentee has not yet entered a subject code
     * and progressed to the next page of the experiment or if the directory could not be created.
     *
     * @return the save directory or <code>null</code>
     */
    public File getSaveDir() {
        if (saveDir == null) {
            final String dirName;
            String[] subjectCodeAns = expTreeRoot.getAnswers(KEY_SUBJECT);
            String experimentCode;
            String subjectCode;

            if (subjectCodeAns != null && subjectCodeAns.length == 1) {
                subjectCode = subjectCodeAns[0];
                experimentCode = expTreeRoot.getAttribute(KEY_EXPERIMENT_CODE).getValue();

                if (experimentCode == null) {
                    experimentCode = DEFAULT_EXP_CODE;
                }

                dirName = experimentCode + "_" + subjectCode;
                saveDir = new File(dirName);

                if (saveDir.exists()) {
                    IntFunction<File> mapper = value -> new File(dirName + "_" + value);
                    Stream<File> dirs = IntStream.rangeClosed(1, Integer.MAX_VALUE).mapToObj(mapper);

                    saveDir = dirs.filter(f -> !f.exists()).findFirst().get();
                }

                if (!saveDir.mkdirs()) {
                    System.err.println("Could not create the save directory for the ExperimentViewer.");
                    saveDir = null;
                }
            }
        }

        return saveDir;
    }

    /**
     * Shows the GUI of the <code>ExperimentEditor</code>.
     *
     * @param args
     *         command line arguments, ignored by this application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Could not set the look and feel to the system look and feel.\n" + e.getMessage());
            }

            new EViewer().setVisible(true);
        });
    }
}
