package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class SettingsTextArea extends Setting {

    private JLabel caption;
    private JTextArea textArea;

    public SettingsTextArea(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        setPreferredSize(new Dimension(500, 150));

        caption = new JLabel();
        add(caption, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void setCaption(String cap) {
        caption.setText(cap);
    }

    public void loadValue() {
        textArea.setText(attribute.getValue());
    }

    public void saveValue() {
        attribute.setValue(textArea.getText());
    }
}
