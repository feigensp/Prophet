package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
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

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.xml.QTreeXMLHandler;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

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
    private int currentNode; // index into the 'experiment' List

    private StopwatchLabel totalTime;
    private JPanel timePanel;

    private File saveDir;

    private ActionListener listener = event -> {
        switch (event.getActionCommand()) {
            case Constants.KEY_FORWARD:
                nextNode();
                break;
            case Constants.KEY_BACKWARD:
                previousNode();
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

        this.expTreeRoot = loadExperiment();
        this.experiment = expTreeRoot.preOrder().stream().map(ViewNode::new).collect(Collectors.toList()); // = ArrayList
        this.currentNode = 0;
        this.totalTime = new StopwatchLabel(expTreeRoot, getLocalized("STOPWATCHLABEL_TOTAL_TIME"));
        this.timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        totalTime.start();
        timePanel.add(totalTime);

        ViewNode expNode = experiment.get(currentNode);
        expNode.getViewPane().addActionListener(listener);

        add(expNode.getViewPane(), BorderLayout.CENTER);
        add(timePanel, BorderLayout.SOUTH);

        PluginList.experimentViewerRun(this);

        pack();
        setLocationRelativeTo(null);
    }

    private void nextNode() {
        System.out.println("NextNode!");
    }

    private void previousNode() {
        System.out.println("PreviousNode!");
    }

    /**
     * Locates the experiment XML file and deserializes it. If no experiment XML can be found or there is an error
     * deserializing the XML file this method will terminate the JVM with exit status 1.
     *
     * @return the deserialized <code>QTreeNode</code>
     */
    private QTreeNode loadExperiment() {
        File experimentFile = new File(Constants.DEFAULT_FILE);

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
            String[] subjectCodeAns = expTreeRoot.getAnswers(Constants.KEY_SUBJECT);
            String experimentCode;
            String subjectCode;

            if (subjectCodeAns != null && subjectCodeAns.length == 1) {
                subjectCode = subjectCodeAns[0];
                experimentCode = expTreeRoot.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue();

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
