package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.experimentEditor.macroEditor.MacroEditor;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * <code>JComboBox</code> for the <code>ContentEditorToolBar</code> to enable using predefined macros.<br>
 * The macros will be loaded from a file called 'macros.xml' in the current working directory.<br>
 * Macros are defined in a {@literal <macro name='MacroName' key='k'>Macro Text Replacement: %s</macro>} tag.<br>
 * The value of the 'key' attribute must have exactly one character and all occurrences of '%s' in the macro text
 * will be replaced by the current selection in the defined <code>RSyntaxTextArea</code>.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class MacroComboBox extends JComboBox<String> {

    private static final Pattern REPLACE_DUMMY = Pattern.compile("%s");

    private RSyntaxTextArea textArea;

    private Map<Integer, String> keyCodeMap = new HashMap<>();
    private Map<String, String> namesMap = new LinkedHashMap<>();

    /**
     * Constructs a new <code>MacroComboBox</code> that applies recognised macros to the given <code>textArea</code>.
     *
     * @param textArea
     *         the <code>RSyntaxTextArea</code> this <code>MacroComboBox</code> affects
     */
    public MacroComboBox(RSyntaxTextArea textArea) {
        this.textArea = textArea;

        addItem(UIElementNames.getLocalized("MENU_TAB_EDITOR_MACROS"));

        addActionListener(event -> {
            if (getSelectedIndex() != 0) {
                useMacro(namesMap.get(getItemAt(getSelectedIndex())));
                setSelectedIndex(0);
            }
        });

        this.textArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                String macroText;

                if (ke.isControlDown()) {
                    macroText = keyCodeMap.get(ke.getKeyCode());

                    if (macroText != null) {
                        useMacro(macroText);
                    }
                }
            }
        });

        loadMacros();
    }

    /**
     * Loads the macros from 'macros.xml' thereby filling <code>keyCodeMap</code>, <code>namesMap</code>, and
     * adding descriptions of the macros to this <code>JComboBox</code>.
     */
    private void loadMacros() {
        List<MacroEditor.Macro> macros = MacroEditor.loadMacros();

        macros.forEach(macro -> {
            String macroKey = macro.getKey();
            String macroName = macro.getName();
            String macroContent = macro.getText();
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(macroKey.charAt(0));
            String keyDescription = KeyEvent.getKeyText(keyCode);

            macroName += String.format("\t [%s + %s]", UIElementNames.getLocalized("KEYBOARD_CTRL"), keyDescription);

            if (!keyCodeMap.containsKey(keyCode)) {
                keyCodeMap.put(keyCode, macroContent);
                namesMap.put(macroName, macroContent);
            }
        });

        namesMap.keySet().forEach(this::addItem);
    }

    /**
     * Applies the given <code>macroText</code> to the <code>textArea</code>.
     *
     * @param macroText the macro text
     */
    private void useMacro(String macroText) {
        String selectedText = textArea.getSelectedText() == null ? "" : textArea.getSelectedText();
        String replacementText = REPLACE_DUMMY.matcher(macroText).replaceAll(selectedText);

        textArea.replaceSelection(replacementText);
    }
}
