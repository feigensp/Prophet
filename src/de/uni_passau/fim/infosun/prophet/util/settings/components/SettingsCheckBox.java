package de.uni_passau.fim.infosun.prophet.util.settings.components;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;

import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JCheckBox</code> to present a true/false choice.
 */
public class SettingsCheckBox extends Setting {

    private JCheckBox myCheckBox;

    /**
     * Constructs a new <code>SettingsCheckBox</code> for the given <code>Attribute</code>. If <code>borderDesc</code>
     * is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
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
