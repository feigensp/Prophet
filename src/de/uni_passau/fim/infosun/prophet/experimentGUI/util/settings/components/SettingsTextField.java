package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

@SuppressWarnings("serial")
public class SettingsTextField extends Setting {

    private JLabel caption;
    private JTextField textField;

    public SettingsTextField(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textField = new JTextField();
        add(textField, BorderLayout.CENTER);
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
