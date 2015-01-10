package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * <code>JComboBox</code> for the <code>ContentEditorToolBar</code> to enable adding HTML tags that change font styles.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class FontStyleComboBox extends JComboBox<String> {

    /**
     * Constructs a new <code>FontStyleComboBox</code> that adds its tags to the given <code>RSyntaxTextArea</code>.
     *
     * @param textArea
     *         the <code>RSyntaxTextArea</code> this <code>FontStyleComboBox</code> affects
     */
    public FontStyleComboBox(RSyntaxTextArea textArea) {
        List<Pair<String, String>> fontFaces = new ArrayList<>();

        fontFaces.add(Pair.of(UIElementNames.getLocalized("FONT_FACE_BOLD"), "b"));
        fontFaces.add(Pair.of(UIElementNames.getLocalized("FONT_FACE_ITALIC"), "i"));
        fontFaces.add(Pair.of(UIElementNames.getLocalized("FONT_FACE_UNDERLINE"), "u"));

        addItem(UIElementNames.getLocalized("MENU_TAB_EDITOR_FONT_FACE"));
        fontFaces.forEach(pair -> addItem(pair.getFirst()));

        addActionListener(event -> {

            if (getSelectedIndex() == 0) {
                return;
            }

            String tag = fontFaces.get(getSelectedIndex() - 1).getSecond();
            String text = textArea.getSelectedText();

            text = text == null ? "" : text;
            textArea.replaceSelection(String.format("<%s>%s</%s>", tag, text, tag));
            setSelectedIndex(0);
        });
    }
}
