package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class SettingsCheckBox extends Setting {

    private JCheckBox myCheckBox;

    public SettingsCheckBox(Attribute attribute, String borderDesc) {
        super(attribute, borderDesc);

        myCheckBox = new JCheckBox();
        add(myCheckBox, BorderLayout.CENTER);
    }

    @Override
    public void setCaption(String cap) {
        myCheckBox.setText(cap);
    }

    @Override
    public void loadValue() {
        myCheckBox.setSelected(Boolean.parseBoolean(attribute.getValue()));
    }

    @Override
    public void saveValue() {
        attribute.setValue(String.valueOf(myCheckBox.isSelected()));
    }
}
