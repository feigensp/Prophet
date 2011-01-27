package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class LineNumbersPlugin implements CodeViewerPluginInterface {
	private QuestionTreeNode selected;
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsComponentDescription result = new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_default", "Zeilennummern anzeigen");
		result.addNextComponent(new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_toggle", "Zeilennummern ein- und ausschaltbar"));
		return result;
	}
	
	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		this.selected=selected;
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		boolean lineNumbers = Boolean.parseBoolean(selected.getAttributeValue("linenumbers_default"));
		editorPanel.getScrollPane().setLineNumbersEnabled(lineNumbers);
	}

	@Override
	public void onClose() {
	}

	@Override
	public String getKey() {
		return "linenumbers";
	}

	@Override
	public void onEditorPanelClose(EditorPanel editorPanel) {
		// TODO Auto-generated method stub
		
	}
}
