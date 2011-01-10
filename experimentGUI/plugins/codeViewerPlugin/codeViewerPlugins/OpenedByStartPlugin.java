package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class OpenedByStartPlugin implements CodeViewerPluginInterface{
	
	public static final String KEY = "openedByStart";
	public static final String PATH = "startPath";
	

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Datei standartmäßig öffnen");
		result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,PATH, "Zu öffnende Datei "));
		return result;
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		System.out.println(Boolean.parseBoolean(selected.getAttributeValue(KEY)));
		if(Boolean.parseBoolean(selected.getAttributeValue(KEY))) {
			QuestionTreeNode attributes = selected.getAttribute(KEY);
			String path = attributes.getAttributeValue(PATH);
			System.out.println(path);
			viewer.getTabbedPane().openFile(path);
		}
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditorPanelClose(EditorPanel editorPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}

}
