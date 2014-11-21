package de.uni_passau.fim.infosun.prophet.util.settings.components;

import java.awt.BorderLayout;
import java.util.Base64;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * A <code>Setting</code> for editing password settings using a <code>JPasswordField</code>.
 */
public class SettingsPasswordField extends Setting {

    private JLabel caption;
    private JTextField textField;
    private final static byte XOR_KEY = 77;

    /**
     * Constructs a new <code>SettingsPasswordField</code> for the given Attribute. If <code>borderDesc</code> is not
     * <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public SettingsPasswordField(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textField = new JPasswordField();
        add(textField, BorderLayout.CENTER);
    }

    /**
     * Returns a new <code>byte[]</code> representing the result of applying the XOR operation with <code>XOR_KEY</code>
     * to every byte in <code>input</code>.
     *
     * @param input
     *         the <code>byte[]</code> to be XOR'ed
     *
     * @return a new array containing the result
     */
    private static byte[] xor(byte[] input) {
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) (input[i] ^ XOR_KEY);
        }

        return result;
    }

    /**
     * Encodes the given <code>String</code> so that it is no longer human-readable and a (tiny) bit more secure to
     * store.
     *
     * @param s the <code>String</code> to encode
     * @return the encoded <code>String</code>
     */
    private static String encode(String s) {
        return Base64.getEncoder().encodeToString(xor(s.getBytes()));
    }

    /**
     * Decodes the given <code>String</code> from the <code>SettingsPasswordField</code> storage format.
     *
     * @param s the <code>String</code> to decode
     * @return the decoded <code>String</code>
     */
    public static String decode(String s) {
        return new String(xor(Base64.getDecoder().decode(s)));
    }

    @Override
    public void setCaption(String cap) {
        caption.setText(cap);
    }

    @Override
    public void loadValue() {
        textField.setText(decode(attribute.getValue()));
    }

    @Override
    public void saveValue() {
        attribute.setValue(encode(textField.getText()));
    }
}
