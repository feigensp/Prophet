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
	public static final String KEY_PATH = "startPath";
	

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Datei standardmäßig öffnen");
		result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,KEY_PATH, "Zu öffnende Datei (vom Quelltextpfad aus):"));
		return result;
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		if(Boolean.parseBoolean(selected.getAttributeValue(KEY))) {
			QuestionTreeNode attributes = selected.getAttribute(KEY);
			String path = attributes.getAttributeValue(KEY_PATH).replace('/', System.getProperty("file.separator").charAt(0));
			viewer.getTabbedPane().openFile(path);
			viewer.getFileTree().selectFile(path);
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
