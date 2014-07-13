package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.xml.QTreeXMLHandler;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.QUESTION;

/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 *
 * @author Markus Köppen, Andreas Hasselberg
 */
public class ExperimentViewer extends JFrame {

    private JPanel contentPane;

    // the textpanes (one for each question)
    private QuestionViewPane currentViewPane;
    private Map<QTreeNode, QuestionViewPane> textPanes;

    // time objects
    private JPanel timePanel;
    private Map<QTreeNode, StopwatchLabel> times;
    private StopwatchLabel totalTime;

    // nodes of the question tree
    private QTreeNode tree;
    private QTreeNode currentNode;

    private File saveDir;

    private Set<QTreeNode> enteredNodes;

    private boolean ignoreDenyNextNode = false;
    private boolean exitExperiment = false;
    private boolean experimentNotRunning = true;

    ActionListener myActionListener = arg0 -> {
        String command = arg0.getActionCommand();
        if (command.equals(Constants.KEY_BACKWARD)) {
            previousNode();
        } else if (command.equals(Constants.KEY_FORWARD)) {
            nextNode();
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                String laf = UIManager.getSystemLookAndFeelClassName();
                UIManager.setLookAndFeel(laf);
                ExperimentViewer frame = new ExperimentViewer();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * With the call of the Constructor the data is loaded and everything is
     * initialized. The first question is showed.
     */
    public ExperimentViewer() {
        setLayout(new BorderLayout());
        setTitle("Aufgaben");
        setSize(800, 600);
        setLocationRelativeTo(null);

        File experimentFile = new File(Constants.DEFAULT_FILE);

        if (!experimentFile.exists()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "*.xml"));

            int returnCode = fileChooser.showOpenDialog(this);

            if (returnCode == JFileChooser.APPROVE_OPTION) {
                experimentFile = fileChooser.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(this, "Kein Experiment ausgewählt.");
                System.exit(0);
            }

            if (!experimentFile.getParentFile().equals(new File("."))) {
                JOptionPane.showMessageDialog(this, "Experiment nicht im aktuellen Verzeichnis.");
                System.exit(0);
            }
        }

        QTreeNode myTree = QTreeXMLHandler.loadExperimentXML(experimentFile);

        if (myTree != null) {
            tree = myTree;
        } else {
            JOptionPane.showMessageDialog(this, "Keine gültige Experiment-Datei.");
            System.exit(0);
        }

//        String fileName = Constants.DEFAULT_FILE;
//        if (!(new File(fileName).exists())) {
//            fileName = JOptionPane.showInputDialog("Bitte Experiment angeben:");
//            if (fileName == null) {
//                System.exit(0);
//            }
//            if (!fileName.endsWith(".xml")) {
//                fileName += ".xml";
//            }
//        }
//        try {
//            boolean isInDir =
//                    new File(fileName).getCanonicalFile().getParentFile().equals(new File(".").getCanonicalFile());
//            if (!isInDir) {
//                JOptionPane.showMessageDialog(this, "Experiment nicht im aktuellen Verzeichnis.");
//                System.exit(0);
//            }
//            QTreeNode myTree = QTreeXMLHandler.loadFromXML(fileName);
//            if (myTree != null) {
//                tree = myTree;
//            } else {
//                JOptionPane.showMessageDialog(this, "Keine g\u00fcltige Experiment-Datei.");
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
//            System.exit(0);
//        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent arg0) {
                if (experimentNotRunning) {
                    System.exit(0);
                } else if (JOptionPane
                        .showConfirmDialog(null, "Das Experiment ist noch nicht abgeschlossen. Experiment beenden?",
                                "Best\u00e4tigung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    exitExperiment = true;
                    currentViewPane.clickSubmit();
                }
            }
        });
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        PluginList.experimentViewerRun(this);

        // Starte Experiment
        QTreeNode superRoot = new QTreeNode(null, EXPERIMENT, ""); // TODO was soll der Quatsch?
        superRoot.addChild(tree);
        tree.setParent(null);
        currentNode = superRoot;
        textPanes = new HashMap<>();
        times = new HashMap<>();
        totalTime = new StopwatchLabel(null, "Gesamtzeit");
        timePanel = new JPanel();
        enteredNodes = new HashSet<>();
        nextNode();
    }

    private boolean nextNode() {
        if (denyNextNode()) {
            return false;
        }
        pauseClock();
        if (currentNode == tree) {
            String subject = currentNode.getAnswer(Constants.KEY_SUBJECT);
            if (subject == null || subject.length() == 0) {
                JOptionPane.showMessageDialog(this, "Bitte Probanden-Code eingeben!", "Fehler!",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            String experiment = currentNode.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue();
            if (experiment == null) {
                experiment = "default";
            }
            saveDir = new File(experiment + "_" + subject);
            if (saveDir.exists()) {
                int i = 1;
                while (saveDir.exists()) {
                    saveDir = new File(experiment + "_" + subject + "_" + i);
                    i++;
                }
            }
            totalTime.start();
            experimentNotRunning = false;
        }

        //step down if we may enter and there are children, else step aside if there is a sibling, else step up
        if (!denyEnterNode() && currentNode.getChildCount() != 0) {
            currentNode = currentNode.getChild(0);
        } else {
            exitNode();
            while (currentNode.getNextSibling() == null) {
                currentNode = currentNode.getParent();
                if (currentNode == null) {
                    return false;
                }
                exitNode();
            }
            currentNode = currentNode.getNextSibling();
        }

        //check if found node is visitable
        if (denyEnterNode()) {
            return nextNode();
        } else {
            boolean doNotShowContent =
                    Boolean.parseBoolean(currentNode.getAttribute(Constants.KEY_DONOTSHOWCONTENT).getValue());
            if (doNotShowContent) {
                enterNode();
                return nextNode();
            } else {
                refresh();
                enterNode();
                return true;
            }
        }
    }

    private boolean previousNode() {
        if (denyNextNode()) {
            return false;
        }
        pauseClock();
        if (currentNode.getType() == QUESTION) {
            QTreeNode tempNode = currentNode;
            while (tempNode.getPreviousSibling() != null) {
                tempNode = tempNode.getPreviousSibling();
                if (!denyEnterNode()) {
                    exitNode();
                    currentNode = tempNode;
                    refresh();
                    enterNode();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean denyEnterNode() {
        return exitExperiment || denyEnterNode(currentNode);
    }

    public static boolean denyEnterNode(QTreeNode node) {
        return PluginList.denyEnterNode(node);
    }

    private void enterNode() {
        enteredNodes.add(currentNode);
        PluginList.enterNode(currentNode);
        currentViewPane.grabFocus();
    }

    private boolean denyNextNode() {
        if (ignoreDenyNextNode || exitExperiment) {
            return false;
        }
        String message = PluginList.denyNextNode(currentNode);
        if (message != null) {
            if (message.length() > 0) {
                JOptionPane.showMessageDialog(this, message);
            }
            return true;
        }
        return false;
    }

    private void exitNode() {
        if (enteredNodes.contains(currentNode)) {
            PluginList.exitNode(currentNode);
            if (currentNode.getType() == EXPERIMENT) {
                endQuestionnaire();
            }
            enteredNodes.remove(currentNode);
        }
    }

    private void pauseClock() {
        StopwatchLabel clock = times.get(currentNode);
        if (clock != null) {
            clock.pause();
        }
    }

    private void refresh() {
        this.setEnabled(false);

        if (currentViewPane != null) {
            contentPane.remove(currentViewPane);
        }
        if (timePanel != null) {
            contentPane.remove(timePanel);
        }
        if (currentNode == null) {
            return;
        }
        currentViewPane = textPanes.get(currentNode);
        if (currentViewPane == null) {
            currentViewPane = new QuestionViewPane(currentNode);
            currentViewPane.addActionListener(myActionListener);
            textPanes.put(currentNode, currentViewPane);
        }
        contentPane.add(currentViewPane, BorderLayout.CENTER);

        timePanel.removeAll();
        StopwatchLabel clock = times.get(currentNode);
        if (clock == null) {
            if (currentNode.getType() == EXPERIMENT) {
                clock = new StopwatchLabel(currentNode, null);
            } else {
                clock = new StopwatchLabel(currentNode, "Aktuell");
            }
            times.put(currentNode, clock);
        }
        clock.start();

        timePanel.add(clock);
        timePanel.add(totalTime);
        contentPane.add(timePanel, BorderLayout.SOUTH);
        contentPane.updateUI();

        this.setEnabled(true);
    }

    /**
     * method which is called when the last question is answered
     */
    private void endQuestionnaire() {
        contentPane.removeAll();
        contentPane.updateUI();
        JTextPane output = new JTextPane();
        output.setEditable(false);
        output.setEditorKit(new HTMLEditorKit());
        String endMessage = "Befragung beendet.";
        String outputString = "<p>" + endMessage + "</p>";
//        QuestionTreeXMLHandler.saveXMLAnswerTree(tree,
//                saveDir.getPath() + System.getProperty("file.separator") + Constants.FILE_ANSWERS);

        try {
            QTreeXMLHandler.saveAnswerXML(tree, new File(saveDir, Constants.FILE_ANSWERS));
        } catch (IOException e) {
            System.err.println("Could not save the answers.xml. " + e); //TODO logging or JDialog
        }

        outputString += PluginList.finishExperiment();
        output.setText(outputString);
        output.setCaretPosition(0);
        contentPane.add(output, BorderLayout.CENTER);
        experimentNotRunning = true;
    }

    public QTreeNode getTree() {
        return tree;
    }

    public File getSaveDir() {
        return saveDir;
    }

    public void forceNext(boolean hard) {
        ignoreDenyNextNode = hard;
        currentViewPane.clickSubmit();
        ignoreDenyNextNode = false;
    }

    public void saveCurrentAnswers() {
        currentViewPane.saveCurrentAnswersToNode();
    }

    public JPanel getContentPanel() {
        return contentPane;
    }

    public boolean getExperimentNotRunning() {
        return experimentNotRunning;
    }
}
