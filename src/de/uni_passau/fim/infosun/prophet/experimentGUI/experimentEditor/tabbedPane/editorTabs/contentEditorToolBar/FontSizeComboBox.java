package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

/**
 * <code>JComboBox</code> for the <code>ContentEditorToolBar</code> to enable adding HTML tags that change font sizes.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class FontSizeComboBox extends JComboBox<String> {

    /**
     * Constructs a new <code>FontSizeComboBox</code> that adds its tags to the given <code>RSyntaxTextArea</code>.
     *
     * @param textArea
     *         the <code>RSyntaxTextArea</code> this <code>FontSizeBox</code> affects
     */
    public FontSizeComboBox(RSyntaxTextArea textArea) {
        List<Pair<String, String>> fontSizes = new ArrayList<>();

        fontSizes.add(new Pair<>("-3", "-3"));
        fontSizes.add(new Pair<>("-2", "-2"));
        fontSizes.add(new Pair<>("-1", "-1"));
        fontSizes.add(new Pair<>("+1", "+1"));
        fontSizes.add(new Pair<>("+2", "+2"));
        fontSizes.add(new Pair<>("+3", "+3"));

        addItem(getLocalized("MENU_TAB_EDITOR_FONT_SIZE"));
        fontSizes.forEach(pair -> addItem(pair.getKey()));

        addActionListener(event -> {

            if (getSelectedIndex() == 0) {
                return;
            }

            String size = fontSizes.get(getSelectedIndex() - 1).getValue();
            String text = textArea.getSelectedText();

            text = text == null ? "" : text;
            textArea.replaceSelection(String.format("<font size=\"%s\">%s</font>", size, text));
            setSelectedIndex(0);
        });
    }
}
