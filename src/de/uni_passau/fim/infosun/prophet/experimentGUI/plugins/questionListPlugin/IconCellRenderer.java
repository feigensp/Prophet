package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.questionListPlugin;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
class IconCellRenderer extends JLabel implements ListCellRenderer {

    private JList listBox;
    private int iconType;

    public IconCellRenderer(JList listBox, int iconType) {
        setOpaque(true);
        this.listBox = listBox;
        this.iconType = iconType;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (listBox.isSelectedIndex(index)) {
            setBackground(listBox.getSelectionBackground());
            setForeground(listBox.getSelectionForeground());
        } else {
            setBackground(listBox.getBackground());
            setForeground(listBox.getForeground());
        }
        setText(value.toString());
        setIcon(new ListIcon(iconType));
        return this;
    }
}
