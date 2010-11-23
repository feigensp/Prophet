package plugins.codeViewer.codeViewerPlugins.SyntaxHighlighting;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class SyntaxHighlightingPlugin implements CodeViewerPlugin {
	private HashMap<String,String> extensionMap;

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"syntaxhighlighting", "Syntaxhighlighting einschalten"));
		return result;
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
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

	@Override
	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		boolean syntaxHighlighting = Boolean.parseBoolean(selected.getAttributeValue("syntaxhighlighting"));
		if (syntaxHighlighting) {
			String fileName = editorPanel.getFile().getName().toLowerCase();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.')==-1 ? 0 : fileName.lastIndexOf('.'));
			String mimeType = extensionMap.get(fileExtension);
			if (mimeType==null) {
				mimeType = SyntaxConstants.SYNTAX_STYLE_NONE;
			}
			System.out.println(fileName+" "+fileExtension+" "+mimeType);
			editorPanel.getTextArea().setSyntaxEditingStyle(mimeType);
		}
	}
}
