package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class SyntaxHighlightingPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "syntaxhighlighting";
	private HashMap<String,String> extensionMap;
	private boolean enabled;
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class,KEY, "Syntaxhighlighting einschalten");
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));
		if (enabled) {
			extensionMap = new HashMap<String,String>();
			
			extensionMap.put("makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
			extensionMap.put(".asm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
			extensionMap.put(".bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
			extensionMap.put(".c", SyntaxConstants.SYNTAX_STYLE_C);
			extensionMap.put(".cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
			extensionMap.put(".cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
			extensionMap.put(".css", SyntaxConstants.SYNTAX_STYLE_CSS);
			extensionMap.put(".dfm", SyntaxConstants.SYNTAX_STYLE_DELPHI);
			extensionMap.put(".dpr", SyntaxConstants.SYNTAX_STYLE_DELPHI);
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
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		if (enabled) {
			String fileName = editorPanel.getFilePath().toLowerCase();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.')==-1 ? 0 : fileName.lastIndexOf('.'));
			String mimeType = extensionMap.get(fileExtension);
			if (mimeType==null) {
				mimeType = SyntaxConstants.SYNTAX_STYLE_NONE;
			}
			editorPanel.getTextArea().setSyntaxEditingStyle(mimeType);
		}
	}

	@Override
	public void onClose() {
	}

	@Override
	public String getKey() {
		return KEY;
	}
}
