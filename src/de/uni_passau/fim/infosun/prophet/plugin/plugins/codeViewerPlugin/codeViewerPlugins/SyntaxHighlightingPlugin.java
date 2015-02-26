package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.CheckBoxSetting;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * A <code>Plugin</code> that will activate syntax highlighting appropriate for the file type that is opened in an
 * <code>EditorPanel</code>.
 */
public class SyntaxHighlightingPlugin implements Plugin {

    private static final String KEY = "syntaxhighlighting";
    private static final Map<String, String> extensionMap;

    static {
        Map<String, String> values = new HashMap<>();

        values.put("makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        values.put(".asm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        values.put(".bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        values.put(".c", SyntaxConstants.SYNTAX_STYLE_C);
        values.put(".cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        values.put(".cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        values.put(".css", SyntaxConstants.SYNTAX_STYLE_CSS);
        values.put(".dfm", SyntaxConstants.SYNTAX_STYLE_DELPHI);
        values.put(".dpr", SyntaxConstants.SYNTAX_STYLE_DELPHI);
        values.put(".h", SyntaxConstants.SYNTAX_STYLE_C);
        values.put(".htm", SyntaxConstants.SYNTAX_STYLE_HTML);
        values.put(".html", SyntaxConstants.SYNTAX_STYLE_HTML);
        values.put(".java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        values.put(".js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        values.put(".pas", SyntaxConstants.SYNTAX_STYLE_DELPHI);
        values.put(".php", SyntaxConstants.SYNTAX_STYLE_PHP);
        values.put(".py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        values.put(".rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
        values.put(".sql", SyntaxConstants.SYNTAX_STYLE_SQL);
        values.put(".xml", SyntaxConstants.SYNTAX_STYLE_XML);

        extensionMap = Collections.unmodifiableMap(values);
    }

    private boolean enabled;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new CheckBoxSetting(attribute, getClass().getSimpleName());
        setting.setCaption(getLocalized("SYNTAX_HIGHLIGHTING_ENABLE"));

        return setting;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        Attribute selected = viewer.getAttribute();
        enabled = selected.containsSubAttribute(KEY) && Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue());
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {

        if (!enabled) {
            return;
        }

        String fileExtension = getExtension(editorPanel.getFile());
        String mimeType = extensionMap.getOrDefault(fileExtension, SyntaxConstants.SYNTAX_STYLE_NONE);

        RSyntaxTextArea textArea = editorPanel.getTextArea();
        Document doc = textArea.getDocument();

        DocumentListener[] listeners = removeListeners((RSyntaxDocument) doc);
        textArea.setSyntaxEditingStyle(mimeType);
        addListeners((RSyntaxDocument) doc, listeners);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {

    }

    /**
     * Returns the file extension (including the '.') of the <code>File</code>. If the filename does not contain a dot
     * the whole name will be returned.
     *
     * @param file
     *         the <code>File</code> whose extension is to be returned
     *
     * @return the extension or the whole filename
     */
    private String getExtension(File file) {
        String name = file.getName().toLowerCase();
        int index = name.lastIndexOf('.');

        return index == -1 ? name : name.substring(index);
    }

    /**
     * Removes all <code>DocumentListener</code>s from the given <code>RSyntaxDocument</code> and returns them.
     *
     * @param doc
     *         the <code>RSyntaxDocument</code> to remove listeners from
     *
     * @return the removed listeners
     */
    private DocumentListener[] removeListeners(RSyntaxDocument doc) {
        DocumentListener[] listeners = doc.getDocumentListeners();

        for (DocumentListener listener : listeners) {
            doc.removeDocumentListener(listener);
        }

        return listeners;
    }

    /**
     * Adds all given <code>DocumentListener</code>s to the <code>RSyntaxDocument</code>.
     *
     * @param doc
     *         the <code>RSyntaxDocument</code> to add the <code>DocumentListener</code>s to
     * @param listeners
     *         the <code>DocumentListener</code>s to add
     */
    private void addListeners(RSyntaxDocument doc, DocumentListener[] listeners) {

        for (DocumentListener listener : listeners) {
            doc.addDocumentListener(listener);
        }
    }
}
