package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class SettingsFilePathChooser extends Setting {

    private int mode = JFileChooser.FILES_ONLY;
    private static final long serialVersionUID = 1L;
    private JLabel caption;
    private JTextField textField;
    private JButton pathButton;

    public SettingsFilePathChooser(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textField = new JTextField();
        textField.setColumns(20);
        add(textField, BorderLayout.CENTER);

        pathButton = new JButton(UIElementNames.get("BUTTON_LABEL_FIND"));
        pathButton.addActionListener(arg0 -> {
            File userDir = new File(".");
            JFileChooser fc = new JFileChooser(userDir);
            fc.setFileSelectionMode(mode);
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String selectedPath;
                try {
                    selectedPath = fc.getSelectedFile().getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                String currentPath;
                try {
                    currentPath = userDir.getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (selectedPath.startsWith(currentPath)) {
                    selectedPath = selectedPath.substring(currentPath.length() + 1);
                    JOptionPane.showMessageDialog(null, UIElementNames.get("MESSAGE_RELATIVE_PATH_NOTIFICATION"));
                }
                textField.setText(selectedPath.replace('\\', '/'));
            }
        });
        add(pathButton, BorderLayout.EAST);
    }

    public void setMode(int mode) {
        this.mode = mode;
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
