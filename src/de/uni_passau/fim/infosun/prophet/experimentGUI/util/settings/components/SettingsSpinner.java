package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

public class SettingsSpinner extends Setting {

    private JLabel caption;
    private JSpinner spinner;

    /**
     * Constructs a new <code>Setting</code> for the given Attribute. If <code>borderDesc</code> is not
     * <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     * @param model
     *         the model for the <code>JSpinner</code> this <code>SettingsSpinner</code> displays
     */
    public SettingsSpinner(Attribute attribute, String borderDesc, SpinnerNumberModel model) {
        super(attribute, borderDesc);

        caption = new JLabel();
        spinner = new JSpinner(model);

        setLayout(new FlowLayout());
        add(caption);
        add(spinner);
    }

    @Override
    public void setCaption(String caption) {
        this.caption.setText(caption);
    }

    @Override
    public void loadValue() {
        Number value;

        try {
            value = NumberFormat.getInstance().parse(attribute.getValue());
        } catch (ParseException e) {
            value = 0;
        }

        spinner.setValue(value);
    }

    @Override
    public void saveValue() {
        attribute.setValue(spinner.getValue().toString());
    }
}
