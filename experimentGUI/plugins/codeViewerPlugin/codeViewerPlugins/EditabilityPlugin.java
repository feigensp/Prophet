package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class EditabilityPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "editable";
	boolean editable;
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Quelltext editierbar");
	}
	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		editable = Boolean.parseBoolean(selected.getAttributeValue(KEY));
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		editorPanel.getTextArea().setEditable(editable);	
	}
	@Override
	public void onClose() {
	}
	@Override
	public String getKey() {
		return KEY;
	}
}
