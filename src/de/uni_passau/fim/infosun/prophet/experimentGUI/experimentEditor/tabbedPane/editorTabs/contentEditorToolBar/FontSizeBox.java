package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * Box for the ContentEditorToolBar, adding HTML tags to change font sizes
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
@SuppressWarnings("serial")
public class FontSizeBox extends JComboBox implements ActionListener {

    private RSyntaxTextArea editArea;
    private ArrayList<Pair<String, String>> fontSizes;

    /**
     * Constructor
     *
     * @param editArea
     *         The editor area it is working with
     */
    public FontSizeBox(RSyntaxTextArea editArea) {
        super();
        this.editArea = editArea;
        fontSizes = new ArrayList<>();
        fontSizes.add(new Pair<>("-3", "-3"));
        fontSizes.add(new Pair<>("-2", "-2"));
        fontSizes.add(new Pair<>("-1", "-1"));
        fontSizes.add(new Pair<>("+1", "+1"));
        fontSizes.add(new Pair<>("+2", "+2"));
        fontSizes.add(new Pair<>("+3", "+3"));

        this.addItem(UIElementNames.MENU_TAB_EDITOR_FONT_SIZE);
        for (Pair<String, String> fontSize : fontSizes) {
            this.addItem(fontSize.getKey());
        }
        this.addActionListener(this);
    }

    /**
     * triggered if the box is changed
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (this.getSelectedIndex() != 0) {
            String size = fontSizes.get(this.getSelectedIndex() - 1).getValue();
            String text = editArea.getSelectedText();
            text = text == null ? "" : text;
            editArea.replaceSelection("<font size=\"" + size + "\">" + text + "</font>");
            this.setSelectedIndex(0);
        }
    }
}
