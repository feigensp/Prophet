package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.cdmckay.coffeedom.CoffeeDOMException;
import org.cdmckay.coffeedom.Document;
import org.cdmckay.coffeedom.Element;
import org.cdmckay.coffeedom.input.SAXBuilder;
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

    private static final String MACROS_XML_FILENAME = "macros.xml";

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

        addItem(UIElementNames.MENU_TAB_EDITOR_MACROS);

        addActionListener(event -> {
            if (getSelectedIndex() != 0) {
                useMacro(namesMap.get(getItemAt(getSelectedIndex())));
                setSelectedIndex(0);
            }
        });

        this.textArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.isControlDown()) {
                    useMacro(keyCodeMap.get(ke.getKeyCode()));
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
        File file = new File(MACROS_XML_FILENAME);

        if (!file.exists()) {
            return;
        }

        Document document;
        try {
            document = new SAXBuilder().build(file);
        } catch (IOException e) {
            System.err.println("Error reading the " + MACROS_XML_FILENAME + " file. " + e);
            return;
        } catch (CoffeeDOMException e) {
            System.err.println("Could not parse the " + MACROS_XML_FILENAME + " file. " + e);
            return;
        }

        Element root = document.getRootElement();
        List<Element> elements = root.getChildren("macro");

        for (Element element : elements) {
            String macroName = element.getAttributeValue("name", "Unnamed Macro");
            String macroKey = element.getAttributeValue("key");
            String macroContent = element.getTextNormalize();

            boolean keyInvalid = macroKey == null || macroKey.length() > 1 || macroKey.trim().isEmpty();
            boolean contentMissing = macroContent.isEmpty();

            if (keyInvalid || contentMissing) {
                System.err.println("Ignoring invalid macro " + macroName + ".");

                if (keyInvalid) {
                    System.err.println("Missing or invalid attribute 'key'. Must be one character exactly.");
                }

                if (contentMissing) {
                    System.err.println("Missing content of macro element.");
                }

                continue;
            }

            int keyCode = KeyEvent.getExtendedKeyCodeForChar(macroKey.charAt(0));
            String keyDescription = KeyEvent.getKeyText(keyCode);

            macroName += String.format("\t [%s + %s]", UIElementNames.KEYBOARD_CTRL, keyDescription);

            if (!keyCodeMap.containsKey(keyCode)) {
                keyCodeMap.put(keyCode, macroContent);
                namesMap.put(macroName, macroContent);
            }
        }

        namesMap.keySet().forEach(this::addItem);
    }

    /**
     * Applies the given <code>macroText</code> to the <code>textArea</code>.
     *
     * @param macroText the macro text
     */
    private void useMacro(String macroText) {
        String selectedText = textArea.getSelectedText() == null ? "" : textArea.getSelectedText();
        String replacementText = macroText.replaceAll("%s", selectedText);

        textArea.replaceSelection(replacementText);
    }
}
