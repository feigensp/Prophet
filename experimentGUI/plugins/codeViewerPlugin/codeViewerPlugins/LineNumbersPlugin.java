package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class LineNumbersPlugin implements CodeViewerPluginInterface {

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_default", "Zeilennummern anzeigen"));
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_toggle", "Zeilennummern ein- und ausschaltbar"));
		return result;
	}
	
	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		boolean lineNumbers = Boolean.parseBoolean(selected.getAttributeValue("linenumbers_default"));
		editorPanel.getScrollPane().setLineNumbersEnabled(lineNumbers);
	}
}
