package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.logingTreeNode.LoggingTreeNode;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;

public class SearchedPlugin implements RecorderPluginInterface{
	public final static String KEY = "search";
	
	private boolean enabled;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		//return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Suchanfragen");
		return null;
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
