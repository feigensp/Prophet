package de.uni_passau.fim.infosun.prophet.plugin.plugins.phpExportPlugin;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * <code>Setting</code> that adds the ability to export PHP and SQL scripts to enable executing the experiment in a 
 * browser and storing the answers in a database.
 */
public class PHPExportComponent extends Setting {

    private static final String PHP_INDEX_PHP = "index.php";
    private static final String PHP_ADDEXPERIMENT_PHP = "addexperiment.php";
    private static final String PHP_CONSTANTS_PHP = "constants.php";
    private static final String PHP_PREPARATION_SQL = "preparation.sql";
    private static final String PHP_CONFIG_PHP = "config.php";

    private static final String PHP_CONFIG_TEMPLATE =
            "<?php%n" 
                + "  $db_server = '%s';%n" 
                + "  $db_base = '%s';%n" 
                + "  $db_user = '%s';%n" 
                + "  $db_pass = '%s';%n"
                + "%n"
                + "  mysql_connect($db_server, $db_user, $db_pass)%n"
                + "    or die('Connection to database failed: ' . mysql_error());%n" 
                + "  mysql_select_db($db_base)%n"
                + "    or die('Selecting database failed: ' . mysql_error());%n" + 
            "?>%n";
    
    /**
     * Constructs a new <code>PHPExportComponent</code> with the given border description.
     *
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public PHPExportComponent(String borderDesc) {
        super(null, borderDesc);

        setBorder(BorderFactory.createTitledBorder(getLocalized("PHP_PHP_EXPORT")));
        setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

        add(new JLabel(getLocalized("PHP_HOST") + ":")); // Server Label

        JTextField serverField = new JTextField("localhost", 20);
        add(serverField);

        add(new JLabel(getLocalized("PHP_NAME_OF_DATABASE") + ":")); // DB Label

        JTextField dbField = new JTextField(20);
        add(dbField);

        add(new JLabel(getLocalized("PHP_USER_NAME") + ":")); // Username Label

        JTextField usernameField = new JTextField(20);
        add(usernameField);

        add(new JLabel(getLocalized("PHP_PASSWORD") + ":")); // Password label

        JPasswordField passwordField = new JPasswordField(20);
        add(passwordField);
        
        ActionListener exportAction = ignored -> {
            String message = getLocalized("PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE");
            String title = getLocalized("PHP_DIALOG_TITLE_CONFIRM");

            if (showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }

            File exportDir = new File(".", String.format("phpExport_%1$tF_%1$tH-%1$tM-%1$tS", new Date()));

            if (!exportDir.mkdirs()) {
                System.err.println("Could not create the export directory " + exportDir.getAbsolutePath());
                return; 
            }

            try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_INDEX_PHP)) {
                Files.copy(is, new File(exportDir, PHP_INDEX_PHP).toPath());    
            } catch (IOException e) {
                System.err.println("Could not export " + PHP_INDEX_PHP);
                System.err.println(e.getMessage());
            }
            
            try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_ADDEXPERIMENT_PHP)) {
                Files.copy(is, new File(exportDir, PHP_ADDEXPERIMENT_PHP).toPath());
            } catch (IOException e) {
                System.err.println("Could not export " + PHP_ADDEXPERIMENT_PHP);
                System.err.println(e.getMessage());
            }
            
            try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_CONSTANTS_PHP)) {
                Files.copy(is, new File(exportDir, PHP_CONSTANTS_PHP).toPath());
            } catch (IOException e) {
                System.err.println("Could not export " + PHP_CONSTANTS_PHP);
                System.err.println(e.getMessage());
            }

            try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_PREPARATION_SQL)) {
                Files.copy(is, new File(exportDir, PHP_PREPARATION_SQL).toPath());
            } catch (Exception e) {
                System.err.println("Could not export " + PHP_PREPARATION_SQL);
                System.err.println(e.getMessage());
            }

            String db_server = serverField.getText();
            String db_base = dbField.getText();
            String db_user = usernameField.getText();
            String db_pass = new String(passwordField.getPassword());
            String config = String.format(PHP_CONFIG_TEMPLATE, db_server, db_base, db_user, db_pass);
            
            File configFile = new File(exportDir, PHP_CONFIG_PHP);
            try (Writer w = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
                w.write(config);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, getLocalized("PHP_MESSAGE_EXPORT_FINISHED"));
        };
        
        JButton exportButton = new JButton(getLocalized("PHP_EXPORT_SCRIPT"));
        exportButton.addActionListener(exportAction);
        add(exportButton);
    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void loadValue() {

    }

    @Override
    public void saveValue() {

    }
}
