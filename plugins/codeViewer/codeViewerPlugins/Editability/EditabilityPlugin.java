package plugins.codeViewer.codeViewerPlugins.Editability;

import java.util.List;
import java.util.Vector;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class EditabilityPlugin implements CodeViewerPlugin {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"editable", "Quelltext editierbar"));
		return result;
	}
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {		
	}

	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		editorPanel.getTextPane().setEditable(Boolean.parseBoolean(selected.getAttributeValue("editable")));	
	}
}
