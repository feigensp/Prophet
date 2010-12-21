package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class DebugPlugin implements PluginInterface {
	public final static String KEY = "debugplugin";
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		System.out.println("DEBUG-PLUGIN: getSettingsComponentDescription()");
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		System.out.println("DEBUG-PLUGIN: experimentViewerRun()");
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		System.out.println("DEBUG-PLUGIN: --> "+node.getType()+": "+node.getName());
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		System.out.println("DEBUG-PLUGIN: <-- "+node.getType()+": "+node.getName());
	}

	@Override
	public String getKey() {
		System.out.println("DEBUG-PLUGIN: getKey()");
		return KEY;
	}

	@Override
	public String finishExperiment() {
		System.out.println("DEBUG-PLUGIN: finishExperiment()");
		return null;
	}

}
