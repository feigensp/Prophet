package experimentGUI.plugins.codeViewer.codeViewerPlugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewer.CodeViewer;
import experimentGUI.plugins.codeViewer.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewer.tabbedPane.EditorPanel;
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
