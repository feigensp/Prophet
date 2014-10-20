package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JTextArea</code> to get user input.
 */
public class SettingsTextArea extends Setting {

    private JLabel caption;
    private JTextArea textArea;

    /**
     * Constructs a new <code>SettingsTextArea</code> for the given <code>Attribute</code>. If <code>borderDesc</code>
     * is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public SettingsTextArea(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        setPreferredSize(new Dimension(500, 150));

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    @Override
    public void setCaption(String cap) {
        caption.setText(cap);
    }

    @Override
    public void loadValue() {
        textArea.setText(attribute.getValue());
    }

    @Override
    public void saveValue() {
        attribute.setValue(textArea.getText());
    }
}
