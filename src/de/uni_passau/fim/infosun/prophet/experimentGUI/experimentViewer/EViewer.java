package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
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
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

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

    private StopwatchLabel totalTime;
    private JPanel timePanel;

    private File saveDir;

    private ActionListener listener = event -> {
        switch (event.getActionCommand()) {
            case KEY_FORWARD:
                nextNode(false);
                break;
            case KEY_BACKWARD:
                previousNode(false);
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        Function<QTreeNode, ViewNode> mapper = node -> new ViewNode(node, listener);

        this.expTreeRoot = loadExperiment();
        this.experiment = expTreeRoot.preOrder().stream().map(mapper).collect(Collectors.toList()); // rtt ArrayList
        this.currentIndex = 0;
        this.totalTime = new StopwatchLabel(expTreeRoot, getLocalized("STOPWATCHLABEL_TOTAL_TIME"));
        this.timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        this.totalTime.start();
        this.timePanel.add(totalTime);

        ViewNode expNode = experiment.get(currentIndex);
        expNode.setStopwatch(totalTime);
        expNode.setEntered(true);

        add(expNode.getViewPane(), BorderLayout.CENTER);
        add(timePanel, BorderLayout.SOUTH);

        PluginList.experimentViewerRun(this);

        pack();
        setLocationRelativeTo(null);
    }

    public void nextNode(boolean ignoreDeny) {
        QTreeNode currentNode = experiment.get(currentIndex).getTreeNode();
        String message;

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

        int newIndex = currentIndex;
        boolean doNotShow;
        ViewNode newNode;

        do {
            if (++newIndex == experiment.size()) {
                endExperiment();
                return;
            }

            newNode = experiment.get(newIndex);
            if (newNode.isEntered()) {
                PluginList.exitNode(newNode.getTreeNode());
            }

            doNotShow = Boolean.parseBoolean(newNode.getTreeNode().getAttribute(KEY_DONOTSHOWCONTENT).getValue());
        } while (doNotShow || PluginList.denyEnterNode(newNode.getTreeNode()));

        switchNode(newIndex);
    }

    public void previousNode(boolean ignoreDeny) {

    }

    private void switchNode(int newIndex) {
        setEnabled(false);

        if (currentIndex == newIndex) {
            return;
        }

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

        PluginList.enterNode(newNode.getTreeNode());

        currentIndex = newIndex;
        repaint();
        setEnabled(true);
    }

    private void endExperiment() {
        System.out.println("Its OVER!");
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
