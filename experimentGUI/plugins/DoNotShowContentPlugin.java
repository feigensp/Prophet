package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class DoNotShowContentPlugin implements PluginInterface {
	public final static String KEY = "donotshowcontent";

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isExperiment() || node.isCategory()) {
			return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Inhalt nicht anzeigen");
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
