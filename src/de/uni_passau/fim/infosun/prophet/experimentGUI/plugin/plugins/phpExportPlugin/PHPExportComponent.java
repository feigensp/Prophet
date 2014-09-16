package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.phpExportPlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

public class PHPExportComponent extends Setting {

    private static final long serialVersionUID = 1L;

    private static byte[] index = new byte[0];
    private static byte[] addexperiment = new byte[0];
    private static byte[] constants = new byte[0];
    private static byte[] sql = new byte[0];

    private static final String PHP_INDEX_PHP = "/php/index.php";
    private static final String PHP_ADDEXPERIMENT_PHP = "/php/addexperiment.php";
    private static final String PHP_CONSTANTS_PHP = "/php/constants.php";
    private static final String PHP_PREPARATION_SQL = "/php/preparation.sql";

    static {

        try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_INDEX_PHP)) {
            index = readAllBytes(is);
        } catch (IOException e) {
            System.err.println("Could not read " + PHP_INDEX_PHP);
            System.err.println(e.getMessage());
        }

        try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_ADDEXPERIMENT_PHP)) {
            addexperiment = readAllBytes(is);
        } catch (IOException e) {
            System.err.println("Could not read " + PHP_ADDEXPERIMENT_PHP);
            System.err.println(e.getMessage());
        }

        try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_CONSTANTS_PHP)) {
            constants = readAllBytes(is);
        } catch (IOException e) {
            System.err.println("Could not read " + PHP_CONSTANTS_PHP);
            System.err.println(e.getMessage());
        }

        try (InputStream is = PHPExportComponent.class.getResourceAsStream(PHP_PREPARATION_SQL)) {
            sql = readAllBytes(is);
        } catch (IOException e) {
            System.err.println("Could not read " + PHP_PREPARATION_SQL);
            System.err.println(e.getMessage());
        }
    }

    public PHPExportComponent(String borderDesc) {
        super(null, borderDesc);

        setBorder(BorderFactory.createTitledBorder(getLocalized("PHP_PHP_EXPORT")));
        setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

        add(new JLabel(getLocalized("PHP_HOST") + ":")); // Server Label

        final JTextField serverField = new JTextField("localhost", 20);
        add(serverField);

        add(new JLabel(getLocalized("PHP_NAME_OF_DATABASE") + ":")); // DB Label

        final JTextField dbField = new JTextField(20);
        add(dbField);

        add(new JLabel(getLocalized("PHP_USER_NAME") + ":")); // Username Label

        final JTextField usernameField = new JTextField(20);
        add(usernameField);

        add(new JLabel(getLocalized("PHP_PASSWORD") + ":")); // Password label

        final JPasswordField passwordField = new JPasswordField(20);
        add(passwordField);

        JButton exportButton = new JButton(getLocalized("PHP_EXPORT_SCRIPT"));
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
                        .showConfirmDialog(null, getLocalized("PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE"),
                                getLocalized("PHP_DIALOG_TITLE_CONFIRM"), JOptionPane.YES_NO_OPTION);
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

                JOptionPane.showMessageDialog(null, getLocalized("PHP_MESSAGE_EXPORT_FINISHED"));
            }
        });
    }

    /**
     * Reads all available bytes from the given <code>InputStream</code> and returns them as a <code>byte[]</code>.
     *
     * @param inputStream
     *         the <code>InputStream</code> to read from
     *
     * @return the read bytes as an array
     *
     * @throws IOException
     *         if an <code>IOException</code> occurs reading from the <code>InputStream</code>
     */
    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        int numRead;
        byte[] buffer = new byte[1024 * 32];

        while ((numRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            byteOut.write(buffer, 0, numRead);
        }
        byteOut.flush();

        return byteOut.toByteArray();
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
