package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Locale;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * An ExperimentEditor is a frame which allows its user to create and edit
 * experiments usable in the ExperimentViewer
 *
 * @author Andreas Hasselberg
 * @author Markus K�ppen
 */
public class ExperimentEditor extends JFrame {

    /**
     * The window title for the main frame
     */
    public static final String TITLE = "ExperimentEditor";

    /**
     * JTree component on the left side of the ExperimentEditor
     */
    private QTree tree;

    /**
     * JTabbedPane component on the right side of the ExperimentEditor
     */
    private ExperimentEditorTabbedPane questionEditorTabbedPane;

    /**
     * Main method to launch the ExperimentEditor
     *
     * @param args
     *         not used
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                String laf = UIManager.getSystemLookAndFeelClassName();
                UIManager.setLookAndFeel(laf);
                ExperimentEditor frame = new ExperimentEditor();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor of the ExperimentEditor, called by the main() method;<br>
     * sets some basic settings and adds used components
     */
    public ExperimentEditor() {
        String selectedLanguage = getPreferredLanguage();

        // TODO initialize with selected language
        Locale locale = Locale.ENGLISH;
        if (selectedLanguage.equals("English")) {
            locale = Locale.ENGLISH;
        } else if (selectedLanguage.equals("German")) {
            locale = Locale.GERMAN;
        }

        UIElementNames.setUIElements(locale);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle(TITLE);

        JSplitPane splitPane = new JSplitPane();
        add(splitPane, BorderLayout.CENTER);

        ExperimentEditorTabbedPane tabbedPane = new ExperimentEditorTabbedPane();
        tabbedPane.setBorder(null); // TODO nötig?
        splitPane.setRightComponent(tabbedPane);

        QTree tree = new QTree();
        tree.setPreferredSize(new Dimension(175, 10));
        tree.addTreeSelectionListener(event -> {
            TreePath selectionPath = tree.getSelectionPath();

            if (selectionPath != null) {
                tabbedPane.setSelected((QTreeNode) selectionPath.getLastPathComponent());
            }
        });
        tree.setBorder(null); // TODO nötig?
        splitPane.setLeftComponent(tree);

        splitPane.setBorder(null); //TODO nötig?
        for (Component component : splitPane.getComponents()) {
            if (component instanceof BasicSplitPaneDivider) {
                ((BasicSplitPaneDivider) component).setBorder(null);
            }
        }

        ExperimentEditorMenuBar menuBar = new ExperimentEditorMenuBar(tree, tabbedPane);
        setJMenuBar(menuBar);
    }

    private String getPreferredLanguage() {
        //TODO better modularization
        Object[] possibleValues = {"German", "English"};
        Object selectedLanguage = JOptionPane
                .showInputDialog(null, "Select a language for PROPHET", "Input", JOptionPane.INFORMATION_MESSAGE, null,
                        possibleValues, possibleValues[0]);
        if (selectedLanguage == null) {
            selectedLanguage = "English";
        }
        System.out.println("Language: " + selectedLanguage);

        return selectedLanguage.toString();
    }
}
