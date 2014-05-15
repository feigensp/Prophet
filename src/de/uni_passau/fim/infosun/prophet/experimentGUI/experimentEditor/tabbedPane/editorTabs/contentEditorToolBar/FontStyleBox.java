package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * Box for the ContentEditorToolBar, adding HTML tags to change font styles
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
@SuppressWarnings("serial")
public class FontStyleBox extends JComboBox implements ActionListener {

    private RSyntaxTextArea editArea;
    private ArrayList<Pair<String, String>> fontFace;

    public FontStyleBox(RSyntaxTextArea editArea) {
        super();
        this.editArea = editArea;
        fontFace = new ArrayList<>();
        fontFace.add(new Pair<>(UIElementNames.FONT_FACE_BOLD, "b"));
        fontFace.add(new Pair<>(UIElementNames.FONT_FACE_ITALIC, "i"));
        fontFace.add(new Pair<>(UIElementNames.FONT_FACE_UNDERLINE, "u"));

        this.addItem(UIElementNames.MENU_TAB_EDITOR_FONT_FACE);
        for (Pair<String, String> aFontFace : fontFace) {
            this.addItem(aFontFace.getKey());
        }
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        if (this.getSelectedIndex() != 0) {
            String tag = fontFace.get(this.getSelectedIndex() - 1).getValue();
            String text = editArea.getSelectedText();
            text = text == null ? "" : text;
            editArea.replaceSelection("<" + tag + ">" + text + "</" + tag + ">");
            this.setSelectedIndex(0);
        }
    }
}
