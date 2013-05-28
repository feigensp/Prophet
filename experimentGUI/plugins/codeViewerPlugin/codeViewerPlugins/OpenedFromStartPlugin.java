package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.Recorder;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.language.UIElementNames;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsTextField;

public class OpenedFromStartPlugin implements CodeViewerPluginInterface{
	
	public static final String KEY = "openedByStart";
	public static final String KEY_PATH = "startPath";
	private QuestionTreeNode selected;
	

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, UIElementNames.OPENED_FROM_START_OPEN_FILE_ON_START, true);
		result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,KEY_PATH, UIElementNames.OPENED_FROM_START_FILE_TO_OPEN + ":"));
		return result;
	}

	@Override
	public void init(QuestionTreeNode selected) {
		this.selected = selected;		
	}

	@Override
	public void onFrameCreate(CodeViewer viewer) {
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
	public void onClose() {
		// TODO Auto-generated method stub
		
	}

}
