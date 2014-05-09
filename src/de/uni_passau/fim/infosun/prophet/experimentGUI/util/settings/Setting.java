package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;

/**
 * Implementations of this class represent a value setting (e.g. for a <code>Plugin</code>) that is stored in an
 * <code>Attribute</code> attached to a <code>QTreeNode</code>.
 *
 * Depending on the type of attribute implementing classes should choose an appropriate graphical representation and
 * controls that enable editing the value.
 */
public abstract class Setting extends JPanel {

    protected Attribute attribute;

    /**
     * Constructs a new <code>Setting</code> for the given Attribute. If <code>borderDesc</code> is not
     * <code>null</code> this <code>JPanel</code> will be surrounded by a titled border with the given title.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param borderDesc
     *         the title for the border or <code>null</code> for no border
     */
    public Setting(Attribute attribute, String borderDesc) {
        this.attribute = attribute;

        if (borderDesc != null) {
            setBorder(BorderFactory.createTitledBorder(borderDesc));
        }
    }

    /**
     * Sets the description for this <code>Settings</code> object.
     *
     * @param caption the new caption
     */
    public abstract void setCaption(String caption);

    /**
     * Loads the current value of the <code>Attribute</code> and displays it so it can be edited.
     */
    public abstract void loadValue();

    /**
     * Saves the current value in the <code>Attribute</code>.
     */
    public abstract void saveValue();
}
