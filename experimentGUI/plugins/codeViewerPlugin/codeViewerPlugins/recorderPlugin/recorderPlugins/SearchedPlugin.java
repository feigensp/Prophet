package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class SearchedPlugin implements RecorderPluginInterface{
	public final static String KEY = "search";
	
	boolean enabled;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Suchanfragen");
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer,
			LoggingTreeNode currentNode) {
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));
	}

	@Override
	public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
