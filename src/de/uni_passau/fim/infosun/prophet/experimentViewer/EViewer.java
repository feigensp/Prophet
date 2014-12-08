package de.uni_passau.fim.infosun.prophet.experimentViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_passau.fim.infosun.prophet.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.qTree.handlers.QTreeXMLHandler;

import static de.uni_passau.fim.infosun.prophet.Constants.*;
import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
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

    private boolean visibleStopwatches;
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
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.expTreeRoot = loadExperiment();
        this.visibleStopwatches = Boolean.parseBoolean(expTreeRoot.getAttribute(KEY_TIMING).getValue());

        initLanguage();

        // randomize the children if this is enabled
        applyToTree(expTreeRoot, node -> {
            boolean enabled = Boolean.parseBoolean(node.getAttribute(KEY_RANDOMIZE_CHILDREN).getValue());

            if (enabled) {
                Collections.shuffle(node.getChildren());
            }
        });

        // only retain X children of a node if this is enabled
        applyToTree(expTreeRoot, node -> {
            Attribute attribute = node.getAttribute(KEY_ONLY_SHOW_X_CHILDREN);
            boolean enabled = Boolean.parseBoolean(attribute.getValue());

            if (enabled) {
                int number = Integer.parseInt(attribute.getSubAttribute(KEY_SHOW_NUMBER_OF_CHILDREN).getValue());
                List<QTreeNode> children = node.getChildren();

                if (children.size() > number) {
                    children.removeAll(children.subList(number, children.size()));
                }
            }
        });

        Function<QTreeNode, ViewNode> mapper = node -> new ViewNode(node, listener);
        this.experiment = expTreeRoot.preOrder().stream().map(mapper).collect(Collectors.toList()); // rtt ArrayList
        this.currentIndex = 0;

        ViewNode expNode = experiment.get(currentIndex);
        expNode.setEntered(true);

        StopwatchLabel totalTime = new StopwatchLabel(expTreeRoot, getLocalized("STOPWATCHLABEL_TOTAL_TIME"));
        totalTime.start();
        expNode.setStopwatch(totalTime);

        if (visibleStopwatches) {
            this.timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            this.timePanel.add(totalTime);
            add(timePanel, BorderLayout.SOUTH);
        }

        add(expNode.getViewPane(), BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                String message = getLocalized("EVIEWER_EXPERIMENT_NOT_FINISHED");
                int choice = showConfirmDialog(EViewer.this, message, null, YES_NO_OPTION);

                if (choice == YES_OPTION) {
                    experiment.get(currentIndex).getViewPane().clickSubmit(false);
                    endExperiment();
                    dispose();
                }
            }
        });

        PluginList.experimentViewerRun(this);
        PluginList.enterNode(expNode.getTreeNode());

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Initialises the language bundle used by the <code>EViewer</code>.
     */
    private void initLanguage() {
        String langTag = expTreeRoot.getAttribute(KEY_VIEWER_LANGUAGE).getValue();

        if (langTag.equals(Locale.GERMAN.toLanguageTag()) || langTag.equals(Locale.ENGLISH.toLanguageTag())) {
            UIElementNames.setLocale(Locale.forLanguageTag(langTag));
        }
    }

    /**
     * Applies the given <code>Consumer</code> recursively to every node in the tree with root <code>node</code>.
     *
     * @param node the root of the tree the function is to be applied to
     * @param function the function to be applied
     */
    private static void applyToTree(QTreeNode node, Consumer<QTreeNode> function) {
        function.accept(node);
        node.getChildren().forEach(n -> applyToTree(n, function));
    }

    /**
     * Advances from one node to the next visitable node in the given direction.
     *
     * @param saveAnswers whether to save answers of the current node before advancing
     * @param ignoreDeny whether to ignore plugins denying exiting the current node
     * @param forward true for forward, false for backwards
     */
    private void advance(boolean saveAnswers, boolean ignoreDeny, boolean forward) {
        QuestionViewPane currentViewNode = experiment.get(currentIndex).getViewPane();
        QTreeNode currentNode = experiment.get(currentIndex).getTreeNode();

        if (saveAnswers) {
            currentViewNode.clickSubmit(false);
        }

        String message;
        if (!ignoreDeny && (message = PluginList.denyNextNode(currentNode)) != null) {
            showMessageDialog(this, message, null, INFORMATION_MESSAGE);
            return;
        }

        // make sure that a subject code was entered before leaving the first node forward
        if (forward && currentNode.getType() == QTreeNode.Type.EXPERIMENT && currentNode.equals(expTreeRoot)) {
            String[] answers = currentNode.getAnswers(KEY_SUBJECT_CODE);

            if (answers == null || answers.length < 1) {
                showMessageDialog(this, getLocalized("EVIEWER_NO_SUBJECT_CODE"), null, ERROR_MESSAGE);
                return;
            }
        }

        ViewNode newViewNode;
        boolean doNotShow;
        int newIndex = currentIndex;

        do {
            if (forward) {
                if (++newIndex >= experiment.size()) {
                    endExperiment();
                    return;
                }
            } else {
                if (--newIndex <= 0) {
                    return;
                }
            }

            newViewNode = experiment.get(newIndex);
            doNotShow = Boolean.parseBoolean(newViewNode.getTreeNode().getAttribute(KEY_DONOTSHOWCONTENT).getValue());
        } while (doNotShow || PluginList.denyEnterNode(newViewNode.getTreeNode()));

        switchNode(newIndex);
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
        advance(saveAnswers, ignoreDeny, true);
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
        advance(saveAnswers, ignoreDeny, false);
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

        updateStopwatches(oldNode, newNode);

        newNode.setEntered(true);
        remove(oldNode.getViewPane());
        add(newNode.getViewPane(), BorderLayout.CENTER);

        QTreeNode exitNode = oldNode.getTreeNode();

        if (exitNode.getChildren().isEmpty()) {
            PluginList.exitNode(exitNode);

            while (exitNode.isLastChild()) {
                PluginList.exitNode(exitNode.getParent());
                exitNode = exitNode.getParent();
            }
        }

        PluginList.enterNode(newNode.getTreeNode());
        currentIndex = newIndex;

        revalidate();
        repaint();
        setEnabled(true);
    }

    /**
     * Stops/Starts the stopwatches of <code>oldNode</code> and <code>newNode</code>.
     *
     * @param oldNode
     *         the old selected node
     * @param newNode
     *         the new selected node
     */
    private void updateStopwatches(ViewNode oldNode, ViewNode newNode) {

        // the root node counts the total time
        if (currentIndex != 0) {
            oldNode.getStopwatch().pause();
        }
        newNode.getStopwatch().start();

        if (visibleStopwatches) {
            if (currentIndex != 0) {
                timePanel.remove(oldNode.getStopwatch());
            }
            timePanel.add(newNode.getStopwatch());
        }
    }

    /**
     * Ends the experiment and shows a dialog containing the <code>Plugin</code> messages.
     * The Window will then close.
     */
    private void endExperiment() {
        experiment.get(0).getStopwatch().pause();
        ViewNode currentNode = experiment.get(currentIndex);
        QTreeNode exitNode = currentNode.getTreeNode();

        currentNode.getStopwatch().pause();
        do {
            PluginList.exitNode(exitNode);
            exitNode = exitNode.getParent();
        } while (exitNode != null);

        try {
            QTreeXMLHandler.saveAnswerXML(expTreeRoot, new File(getSaveDir(), FILE_ANSWERS));
        } catch (IOException e) {
            System.err.println("Could not save the answers.xml. " + e);
        }

        String message = "<html><p>" + getLocalized("EVIEWER_EXPERIMENT_FINISHED") + "</p></html>";
        message += PluginList.finishExperiment();

        setEnabled(false);
        showMessageDialog(this, message, null, INFORMATION_MESSAGE);

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
            File workingDir = new File(".");
            JFileChooser fileChooser = new JFileChooser(workingDir);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(getLocalized("EVIEWER_XML_FILES"), "*.xml"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                experimentFile = fileChooser.getSelectedFile();
            } else {
                showMessageDialog(this, getLocalized("EVIEWER_NO_EXPERIMENT_CHOSEN"));
                System.exit(LOAD_FAIL_EXIT_STATUS);
            }

            boolean inWorkingDir = false;
            try {
                inWorkingDir = Files.isSameFile(workingDir.toPath(), experimentFile.getParentFile().toPath());
            } catch (IOException e) {
                System.err.println("Could not check whether the chosen experiment File is in the working directory.");
                System.err.println(e.getMessage());
            }

            if (!inWorkingDir) {
                showMessageDialog(this, getLocalized("EVIEWER_EXPERIMENT_NOT_IN_WORKING_DIR"));
                System.exit(LOAD_FAIL_EXIT_STATUS);
            }
        }

        QTreeNode treeRoot = QTreeXMLHandler.loadExperimentXML(experimentFile);

        if (treeRoot == null) {
            showMessageDialog(this, getLocalized("MESSAGE_NO_VALID_EXPERIMENT_FILE"));
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
            String[] subjectCodeAns = expTreeRoot.getAnswers(KEY_SUBJECT_CODE);
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
