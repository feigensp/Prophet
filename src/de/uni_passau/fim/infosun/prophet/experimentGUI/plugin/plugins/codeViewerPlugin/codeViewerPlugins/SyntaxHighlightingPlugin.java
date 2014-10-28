package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.util.HashMap;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class SyntaxHighlightingPlugin implements CodeViewerPlugin {

    public final static String KEY = "syntaxhighlighting";
    private HashMap<String, String> extensionMap;
    private boolean enabled;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new SettingsCheckBox(attribute, getClass().getSimpleName());
        setting.setCaption(UIElementNames.getLocalized("SYNTAX_HIGHLIGHTING_ENABLE"));

        return setting;
    }

    @Override
    public void init(Attribute selected) {
        enabled = Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue());
        if (enabled) {
            extensionMap = new HashMap<>();

            extensionMap.put("makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
            extensionMap.put(".asm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
            extensionMap.put(".bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
            extensionMap.put(".c", SyntaxConstants.SYNTAX_STYLE_C);
            extensionMap.put(".cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            extensionMap.put(".cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
            extensionMap.put(".css", SyntaxConstants.SYNTAX_STYLE_CSS);
            extensionMap.put(".dfm", SyntaxConstants.SYNTAX_STYLE_DELPHI);
            extensionMap.put(".dpr", SyntaxConstants.SYNTAX_STYLE_DELPHI);
            extensionMap.put(".h", SyntaxConstants.SYNTAX_STYLE_C);
            extensionMap.put(".htm", SyntaxConstants.SYNTAX_STYLE_HTML);
            extensionMap.put(".html", SyntaxConstants.SYNTAX_STYLE_HTML);
            extensionMap.put(".java", SyntaxConstants.SYNTAX_STYLE_JAVA);
            extensionMap.put(".js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            extensionMap.put(".pas", SyntaxConstants.SYNTAX_STYLE_DELPHI);
            extensionMap.put(".php", SyntaxConstants.SYNTAX_STYLE_PHP);
            extensionMap.put(".py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
            extensionMap.put(".rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
            extensionMap.put(".sql", SyntaxConstants.SYNTAX_STYLE_SQL);
            extensionMap.put(".xml", SyntaxConstants.SYNTAX_STYLE_XML);
        }
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        if (enabled) {
            String fileName = editorPanel.getPath().toLowerCase();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') == -1 ? 0 : fileName.lastIndexOf('.'));
            String mimeType = extensionMap.get(fileExtension);
            if (mimeType == null) {
                mimeType = SyntaxConstants.SYNTAX_STYLE_NONE;
            }
            Document doc = editorPanel.getTextArea().getDocument();

            DocumentListener[] listeners = removeDocumentListener((RSyntaxDocument) doc);
            editorPanel.getTextArea().setSyntaxEditingStyle(mimeType);
            readdDocumentListeners((RSyntaxDocument) doc, listeners);
        }
    }

    private DocumentListener[] removeDocumentListener(RSyntaxDocument doc) {
        DocumentListener[] listeners = doc.getDocumentListeners();
        for (DocumentListener listener : listeners) {
            doc.removeDocumentListener(listener);
        }
        return listeners;
    }

    private void readdDocumentListeners(RSyntaxDocument doc, DocumentListener[] listeners) {
        for (DocumentListener listener : listeners) {
            doc.addDocumentListener(listener);
        }
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        // TODO Auto-generated method stub

    }
}
