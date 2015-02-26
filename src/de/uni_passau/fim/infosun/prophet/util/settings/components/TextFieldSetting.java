package de.uni_passau.fim.infosun.prophet.util.settings.components;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JTextField</code> to get user input.
 */
public class TextFieldSetting extends Setting {

    private JLabel caption;
    private JTextField textField;

    /**
     * Constructs a new <code>TextFieldSetting</code> for the given <code>Attribute</code>. If <code>borderDesc</code>
     * is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public TextFieldSetting(Attribute attribute, String borderDesc) {
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
