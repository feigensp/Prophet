package experimentGUI.plugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class InactivityPlugin implements PluginInterface {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(QuestionTreeNode node) {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		if (node.isCategory() || node.isQuestion()) {
			result.add(new SettingsComponentDescription(SettingsCheckBox.class, "inactive", "Deaktivieren"));
		}
		return result;
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
		return "inactivity";
	}

}
