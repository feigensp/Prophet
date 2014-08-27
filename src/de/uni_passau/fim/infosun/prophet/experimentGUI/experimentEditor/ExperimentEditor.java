package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.swing.*;
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

    private static final String CONFIG_FILENAME = "config";
    private Properties config;

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

        loadConfig();
        initLanguage();

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

    /**
     * Loads the 'config' file as a <code>Properties</code> object. If there is no such file an empty
     * <code>Properties</code> object will be used. Also attaches a window listener that writes the config file to
     * disk when the current <code>Window</code> is closing.
     */
    private void loadConfig() {
        File configFile = new File(CONFIG_FILENAME);

        config = new Properties();
        if (configFile.exists() && !configFile.isDirectory()) {
            try {
                config.load(new FileInputStream(configFile));
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Could not load properties file " + configFile.getAbsolutePath());
            }
        }

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                try {
                    config.store(new FileOutputStream(configFile), null);
                } catch (IOException e1) {
                    System.err.println("Could not store properties file " + configFile.getAbsolutePath());
                }

                super.windowClosing(e);
            }
        });
    }

    /**
     * Initialises the <code>Locale</code> this <code>ExperimentEditor</code> is using. If no language is defined
     * in the config file the user is asked to select one.
     */
    private void initLanguage() {
        String langKey = "language";
        String language = config.getProperty(langKey);
        Locale locale;

        if (language == null) {
            locale = getPreferredLanguage();
            config.setProperty(langKey, locale.toLanguageTag());
        } else {
            locale = Locale.forLanguageTag(language);
        }

        UIElementNames.setLocale(locale);
    }

    /**
     * Shows a dialog asking the user to select a language. If the user makes no selection <code>Locale.ENGLISH</code>
     * is returned.
     *
     * @return the selected <code>Locale</code>
     */
    private Locale getPreferredLanguage() {
        Map<String, Locale> languages = new HashMap<>();
        String message = "Select a language for PROPHET";
        String title = "Input";
        Object selectedLanguage;
        Object[] values;

        languages.put("German", Locale.forLanguageTag("de"));
        languages.put("English", Locale.forLanguageTag("en"));

        values = languages.keySet().toArray();
        selectedLanguage = JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, null,
                values, values[0]);

        return languages.getOrDefault(selectedLanguage, Locale.ENGLISH);
    }
}
