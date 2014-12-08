package de.uni_passau.fim.infosun.prophet.util.settings.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * A <code>Setting</code> that uses a <code>JFileChooser</code> enable choosing a path. The chosen path will be
 * displayed in a <code>JTextField</code>.
 */
public class SettingsPathChooser extends Setting {

    /**
     * The type of path to enable choosing.
     */
    public enum Type {

        /**
         * Enable choosing file paths only.
         */
        FILES(JFileChooser.FILES_ONLY),

        /**
         * Enable choosing directory paths only.
         */
        DIRECTORIES(JFileChooser.DIRECTORIES_ONLY),

        /**
         * Enable choosing directory or file paths.
         */
        BOTH(JFileChooser.FILES_AND_DIRECTORIES);

        private int mode;

        /**
         * Constructs a new <code>SettingsPathChooser.Type</code> containing the given constant.
         *
         * @param mode
         *         should be one of {@link javax.swing.JFileChooser#FILES_ONLY},
         *         {@link javax.swing.JFileChooser#DIRECTORIES_ONLY},
         *         {@link javax.swing.JFileChooser#FILES_AND_DIRECTORIES}
         */
        Type(int mode) {
            this.mode = mode;
        }

        /**
         * Returns the <code>JFileChooser</code> magic int constant appropriate for the type.
         *
         * @return the appropriate constant
         */
        public int getMode() {
            return mode;
        }
    }

    private Type type;
    private JLabel caption;
    private JTextField textField;

    /**
     * Constructs a new <code>SettingsPathChooser</code> for the given <code>Attribute</code>.
     * If <code>borderDesc</code> is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled
     * border with the given title. This <code>SettingsPathChooser</code> will only allow choosing files.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public SettingsPathChooser(Attribute attribute, String borderDesc) {
        this(attribute, borderDesc, Type.FILES);
    }

    /**
     * Constructs a new <code>SettingsPathChooser</code> for the given <code>Attribute</code>.
     * If <code>borderDesc</code> is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled
     * border with the given title. Using the <code>type</code> argument the <code>JFileChooser</code> can
     * be configured to allow choosing files, directories or both.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     * @param type
     *         the type of path that can be chosen
     */
    public SettingsPathChooser(Attribute attribute, String borderDesc, Type type) {
        super(attribute, borderDesc);
        JButton pathButton;

        this.type = type;

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textField = new JTextField();
        textField.setColumns(20);
        add(textField, BorderLayout.CENTER);

        pathButton = new JButton(getLocalized("BUTTON_LABEL_FIND"));
        pathButton.addActionListener(this::choose);
        add(pathButton, BorderLayout.EAST);
    }

    /**
     * Used as an <code>ActionListener</code> for the button that displays the path chooser.
     *
     * @param event
     *         the <code>ActionEvent</code> produced by the button, ignored by this method
     */
    private void choose(ActionEvent event) {
        File workingDir = new File(".");
        JFileChooser fc = new JFileChooser(workingDir);

        fc.setFileSelectionMode(type.getMode());

        if (fc.showSaveDialog(SwingUtilities.getRoot(this)) == JFileChooser.APPROVE_OPTION) {
            String selectedPath;
            try {
                selectedPath = fc.getSelectedFile().getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            String currentPath;
            try {
                currentPath = workingDir.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (selectedPath.startsWith(currentPath)) {
                selectedPath = selectedPath.substring(currentPath.length() + 1);
                JOptionPane.showMessageDialog(null, getLocalized("MESSAGE_RELATIVE_PATH_NOTIFICATION"));
            }

            textField.setText(selectedPath.replace('\\', '/'));
        }
    }

    @Override
    public void setCaption(String cap) {
        caption.setText(cap);
    }

    @Override
    public void loadValue() {
        textField.setText(attribute.getValue());
    }

    @Override
    public void saveValue() {
        attribute.setValue(textField.getText());
    }
}
