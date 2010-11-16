package plugins.codeViewer.codeViewerPlugins.SyntaxHighlighting;

import java.util.List;
import java.util.Vector;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class SyntaxHighlightingPlugin implements CodeViewerPlugin {

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"syntaxhighlighting_default", "Syntaxhighlighting einschalten"));
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"syntaxhighlighting_toggle", "Syntaxhighlighting ein- und ausschaltbar"));
		return result;
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		// TODO Auto-generated method stub
		
	}
}
