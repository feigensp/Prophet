package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JSpinner</code> to set a number value.
 */
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

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(caption);
        add(Box.createHorizontalGlue());
        add(spinner);
    }

    @Override
    public void setCaption(String caption) {
        this.caption.setText(caption);
    }

    @Override
    public void loadValue() {
        String key = attribute.getKey();
        String value = attribute.getValue();
        Number numberValue;

        if (value.isEmpty()) {
            return;
        }

        try {
            NumberFormat numberFormat = NumberFormat.getInstance();

            if (numberFormat != null) {
                numberValue = numberFormat.parse(value);
                spinner.setValue(numberValue);
            } else {
                System.err.println("Could not get a NumberFormat instance to parse the value for " + key);
            }
        } catch (ParseException e) {
            System.err.printf("Could not parse value \"%s\" for %s to a Number. Setting it to 0.%n", value, key);
        }
    }

    @Override
    public void saveValue() {
        attribute.setValue(spinner.getValue().toString());
    }
}
