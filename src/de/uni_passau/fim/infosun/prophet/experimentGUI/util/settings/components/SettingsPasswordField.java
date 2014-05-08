package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SettingsPasswordField extends Setting {

    private JLabel caption;
    private JTextField textField;
    private final static byte XOR_KEY = 77;

    public SettingsPasswordField(Attribute attribute) {
        super(attribute);

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textField = new JPasswordField();
        add(textField, BorderLayout.CENTER);
    }

    private static byte[] xor(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) (input[i] ^ XOR_KEY);
        }
        return result;
    }

    private static String encode(String s) {
        return new BASE64Encoder().encode(xor(s.getBytes()));
    }

    public static String decode(String s) {
        try {
            return new String(xor(new BASE64Decoder().decodeBuffer(s)));
        } catch (IOException e) {
            return null;
        }
    }

    public void setCaption(String cap) {
        caption.setText(cap);
    }

    public void loadValue() {
        textField.setText(decode(attribute.getValue()));
    }

    public void saveValue() {
        attribute.setValue(encode(textField.getText()));
    }
}
