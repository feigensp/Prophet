package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.phpExportPlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.swing.*;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class PHPExportComponent extends Setting {

    private static final long serialVersionUID = 1L;

    private static byte[] index = new byte[0];
    private static byte[] addexperiment = new byte[0];
    private static byte[] constants = new byte[0];
    private static byte[] sql = new byte[0];

    static {
        File file;
        try {
            file = new File(PHPExportComponent.class.getResource("/php/index.php").toURI());
            index = Files.readAllBytes(file.toPath());

            file = new File(PHPExportComponent.class.getResource("/php/addexperiment.php").toURI());
            addexperiment = Files.readAllBytes(file.toPath());

            file = new File(PHPExportComponent.class.getResource("/php/constants.php").toURI());
            constants = Files.readAllBytes(file.toPath());

            file = new File(PHPExportComponent.class.getResource("/php/preparation.sql").toURI());
            sql = Files.readAllBytes(file.toPath());
        } catch (URISyntaxException | IOException e) {
            System.err.println("Error while reading php and sql resources for PHP Export: " + e.getMessage());
        }
    }

    public PHPExportComponent(String borderDesc) {
        super(null, borderDesc);

        setBorder(BorderFactory.createTitledBorder(UIElementNames.get("PHP_PHP_EXPORT")));
        setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

        add(new JLabel(UIElementNames.get("PHP_HOST") + ":")); // Server Label

        final JTextField serverField = new JTextField("localhost", 20);
        add(serverField);

        add(new JLabel(UIElementNames.get("PHP_NAME_OF_DATABASE") + ":")); // DB Label

        final JTextField dbField = new JTextField(20);
        add(dbField);

        add(new JLabel(UIElementNames.get("PHP_USER_NAME") + ":")); // Username Label

        final JTextField usernameField = new JTextField(20);
        add(usernameField);

        add(new JLabel(UIElementNames.get("PHP_PASSWORD") + ":")); // Password label

        final JPasswordField passwordField = new JPasswordField(20);
        add(passwordField);

        JButton exportButton = new JButton(UIElementNames.get("PHP_EXPORT_SCRIPT"));
        add(exportButton);
        exportButton.addActionListener(new ActionListener() {

            private void writeToFile(File fileName, byte[] content) {
                assert fileName != null : "fileName must not be null";
                assert content != null : "content must not be null";

                try (FileOutputStream out = new FileOutputStream(fileName)) {
                    out.write(content);
                } catch (IOException e) {
                    System.err.printf("Error while writing to file '%s': %s%n", fileName, e.getMessage());
                }
            }

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int confirm = JOptionPane
                        .showConfirmDialog(null, UIElementNames.get("PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE"),
                                UIElementNames.get("PHP_DIALOG_TITLE_CONFIRM"), JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }

                Path dirName = Paths.get(".", String.format("phpExport_%1$tF_%1$tH-%1$tM-%1$tS", new Date()));

                boolean mkdirSuccess = dirName.toFile().mkdirs();

                if (!mkdirSuccess) {
                    return; // if dir could not be created then exit
                }

                writeToFile(Paths.get(dirName.toString(), "index.php").toFile(), index);
                writeToFile(Paths.get(dirName.toString(), "addexperiment.php").toFile(), addexperiment);
                writeToFile(Paths.get(dirName.toString(), "constants.php").toFile(), constants);
                writeToFile(Paths.get(dirName.toString(), "preparation.sql").toFile(), sql);

                StringBuilder config = new StringBuilder();
                config.append("<?php\n");
                config.append(" $db_server = '" + serverField.getText() + "';\n");
                config.append(" $db_base = '").append(dbField.getText()).append("';\n");
                config.append(" $db_user = '").append(usernameField.getText()).append("';\n");
                config.append(" $db_pass = '").append(new String(passwordField.getPassword())).append("';\n");
                config.append("\n");
                config.append("//=================================================================\n");
                config.append("\n");
                config.append(" //libxml_use_internal_errors(true);\n");
                config.append(" mysql_connect($db_server, $db_user, $db_pass)\n");
                config.append("  or die('Connection to database failed: ' . mysql_error());\n");
                config.append(" mysql_select_db($db_base)\n");
                config.append("  or die('Selecting database failed: ' . mysql_error());\n");
                config.append("?>");

                writeToFile(Paths.get(dirName.toString(), "config.php").toFile(), config.toString().getBytes());

                JOptionPane.showMessageDialog(null, UIElementNames.get("PHP_MESSAGE_EXPORT_FINISHED"));
            }
        });
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
