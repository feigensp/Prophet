package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JComboBox</code> to present a choice of several items.
 */
public class SettingsComboBox extends Setting {

    private JLabel caption;
    private JComboBox<String> comboBox;

    /**
     * Constructs a new <code>Setting</code> for the given Attribute. If <code>borderDesc</code> is not
     * <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     * @param items
     *         the items for the <code>JComboBox</code>
     */
    public SettingsComboBox(Attribute attribute, String borderDesc, String... items) {
        super(attribute, borderDesc);

        caption = new JLabel();
        comboBox = (items != null) ? new JComboBox<>(items) : new JComboBox<>();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(caption);
        add(Box.createHorizontalGlue());
        add(comboBox);
    }

    @Override
    public void setCaption(String caption) {
        this.caption.setText(caption);
    }

    @Override
    public void loadValue() {
        comboBox.setSelectedItem(attribute.getValue());
    }

    @Override
    public void saveValue() {
        Object selected = comboBox.getSelectedItem();

        if (selected != null && selected instanceof String) {
            attribute.setValue((String) selected);
        }
    }
}
