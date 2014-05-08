package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class SettingsCheckBox extends Setting {

    private JCheckBox myCheckBox;

    public SettingsCheckBox(Attribute attribute) {
        super(attribute);

        myCheckBox = new JCheckBox();
        add(myCheckBox, BorderLayout.CENTER);
    }

    public void setCaption(String cap) {
        myCheckBox.setText(cap);
    }

    public void loadValue() {
        myCheckBox.setSelected(Boolean.parseBoolean(attribute.getValue()));
    }

    public void saveValue() {
        attribute.setValue(String.valueOf(myCheckBox.isSelected()));
    }
}
