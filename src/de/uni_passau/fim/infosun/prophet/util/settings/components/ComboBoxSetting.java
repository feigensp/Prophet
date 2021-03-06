package de.uni_passau.fim.infosun.prophet.util.settings.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * A <code>Setting</code> that uses a <code>JComboBox</code> to present a choice of several items.
 */
public class ComboBoxSetting extends Setting {

    private static final String[] EMPTY_VALUES = new String[0];
    private static final String[] EMPTY_REPRESENTATIONS = new String[0];

    private JLabel caption;
    private JComboBox<String> comboBox;

    private String[] values;
    private String[] representations;

    /**
     * Constructs a new <code>ComboBoxSetting</code> for the given <code>Attribute</code>. If <code>borderDesc</code>
     * is not <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     * <code>itemMappings</code> contains mappings from <code>String</code> values that will be stored in the given
     * attribute to a <code>String</code> that will be used to represent them in the <code>JComboBox</code>. This
     * feature may be used for localizing the descriptions.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     * @param itemMappings
     *         the item mappings as described above
     */
    public ComboBoxSetting(Attribute attribute, String borderDesc, List<Pair<String, String>> itemMappings) {
        super(attribute, borderDesc);

        this.caption = new JLabel();

        if (itemMappings != null) {
            itemMappings = new ArrayList<>(itemMappings);
            Collections.sort(itemMappings, (p1, p2) -> p1.getFirst().compareTo(p2.getFirst()));

            values = new String[itemMappings.size()];
            representations = new String[itemMappings.size()];

            int index = 0;
            for (Pair<String, String> itemMapping : itemMappings) {
                values[index] = itemMapping.getFirst();
                representations[index] = itemMapping.getSecond();
                index++;
            }

            comboBox = new JComboBox<>(representations);
        } else {
            values = EMPTY_VALUES;
            representations = EMPTY_REPRESENTATIONS;

            comboBox = new JComboBox<>();
        }

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
        int index = Arrays.binarySearch(values, attribute.getValue(), String::compareTo);

        if (index != -1) {
            comboBox.setSelectedItem(representations[index]);
        }
    }

    @Override
    public void saveValue() {
        int index = comboBox.getSelectedIndex();

        if (index != -1 && index < values.length) {
            attribute.setValue(values[index]);
        }
    }
}
