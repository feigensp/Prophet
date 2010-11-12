package plugins.codeViewer.plugins.SyntaxHighlighting;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.plugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class SyntaxHighlightingPlugin implements CodeViewerPlugin {

	@Override
	public SettingsComponentFactory getSettingsComponentFactory(
			QuestionTreeNode selected) {
		if(selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_syntaxhighlighting", "Syntaxhighlighting einschalten",selected.getAttribute("codeviewer_syntaxhighlighting"));
		}
		return null;
	}

}
