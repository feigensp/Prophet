package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.Dimension;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
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

    private QTreeNode tree;

    /**
     * Constructs a new <code>EViewer</code> and starts the experiment.
     */
    public EViewer() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.tree = loadExperiment();

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Locates the experiment XML file and deserializes it. If no experiment XML can be found or there is an error
     * deserializing the found XML file this method will terminate the JVM with exit status 0.
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
                System.out.println("Could not set the look and feel to the system look and feel.\n" + e.getMessage());
            }

            new EViewer().setVisible(true);
        });
    }
}
