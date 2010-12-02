package experimentGUI.plugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class QuestionSwitchingPlugin implements PluginInterface {
	public final static String KEY = "questionswitching";

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
			return new SettingsComponentDescription(SettingsCheckBox.class,KEY, "Vor- und Zurückblättern erlauben");
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
