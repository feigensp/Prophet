package experimentGUI.plugins.codeViewer.codeViewerPlugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewer.CodeViewer;
import experimentGUI.plugins.codeViewer.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewer.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class EditabilityPlugin implements CodeViewerPluginInterface {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"editable", "Quelltext editierbar"));
		return result;
	}
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {		
	}

	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		editorPanel.getTextArea().setEditable(Boolean.parseBoolean(selected.getAttributeValue("editable")));	
	}
}
