package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;

/**
 * An editor for experiments that can be viewed by experimentees using the <code>ExperimentViewer</code>.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditor extends JFrame {

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

            new ExperimentEditor().setVisible(true);
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
        setTitle(getClass().getSimpleName());
        setLocationRelativeTo(null);
        setSize(800, 600);

        JSplitPane splitPane = new JSplitPane();
        add(splitPane, BorderLayout.CENTER);

        QTree tree = new QTree();
        tree.setPreferredSize(new Dimension(getSize().width / 5, splitPane.getPreferredSize().height));
        splitPane.setLeftComponent(tree);

        ExperimentEditorTabbedPane tabbedPane = new ExperimentEditorTabbedPane(tree);
        splitPane.setRightComponent(tabbedPane);

        splitPane.setBorder(null);
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
