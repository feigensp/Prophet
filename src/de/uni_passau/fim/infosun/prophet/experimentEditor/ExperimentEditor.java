package de.uni_passau.fim.infosun.prophet.experimentEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import de.uni_passau.fim.infosun.prophet.experimentEditor.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;

/**
 * An editor for experiments that can be viewed by experimentees using the <code>EViewer</code>.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditor extends JFrame {

    private static final String CONFIG_FILENAME = "config";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
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
            } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                System.err.println("Could not set the look and feel to the system look and feel.");
                System.err.println(e.getMessage());
            }

            new ExperimentEditor().setVisible(true);
        });
    }

    /**
     * Constructs a new <code>ExperimentEditor</code>.
     */
    public ExperimentEditor() {

        loadConfig();
        initLanguage();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(getClass().getSimpleName());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JSplitPane splitPane = new JSplitPane();
        add(splitPane, BorderLayout.CENTER);

        QTree tree = new QTree();

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder());

        splitPane.setLeftComponent(treeScrollPane);

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

        pack();
        splitPane.setDividerLocation(0.15);
        setLocationRelativeTo(null);
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
            CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder();

            try (Reader reader = new InputStreamReader(new FileInputStream(configFile), dec) ){
                config.load(reader);
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Could not load properties file " + configFile.getAbsolutePath());
            }
        }

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                CharsetEncoder enc = StandardCharsets.UTF_8.newEncoder();

                try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), enc)) {
                    config.store(writer, null);
                } catch (IOException e) {
                    System.err.println("Could not store properties file " + configFile.getAbsolutePath());
                    System.err.println(e.getMessage());
                }

                super.windowClosing(event);
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

        languages.put("German", Locale.GERMAN);
        languages.put("English", Locale.ENGLISH);
        languages.put("Portuguese (Brazil)", new Locale("pt", "BR"));

        values = languages.keySet().toArray();
        selectedLanguage = JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, null,
                values, values[0]);

        return languages.getOrDefault(selectedLanguage, Locale.ENGLISH);
    }
}
