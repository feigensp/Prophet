package experimentGUI.plugins;

import java.util.List;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class DebugPlugin implements PluginInterface {
	public final static String KEY = "debugplugin";
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		System.out.println("Trying to get component descriptions.");
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		System.out.println("Running experiment viewer.");
	}

	@Override
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		System.out.println("--> "+node.getType()+": "+node.getName());
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData) {
		System.out.println("<-- "+node.getType()+": "+node.getName());
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
