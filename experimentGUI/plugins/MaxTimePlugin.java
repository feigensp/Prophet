package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class MaxTimePlugin implements PluginInterface {

	public static final String KEY = "maxTime";
	public static final String MAX_TIME = "maxTime";
	
	ExperimentViewer experimentViewer;
	int maxTime = 0;
	boolean enabled;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isExperiment()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Maximale Bearbeitungszeit");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, MAX_TIME,
					"Maximale Laufzeit:"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		this.experimentViewer = experimentViewer;
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {

		try {
			if (node.isExperiment()) {
				enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
				if (enabled) {
					QuestionTreeNode attributes = node.getAttribute(KEY);
					maxTime = Integer.parseInt(attributes.getAttributeValue(MAX_TIME));
				}
			}
		} catch (Exception e) {
			//do nothing
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		if(enabled && maxTime != 0) {
			int currentTime = (int) (experimentViewer.getTime()/1000);
			if(currentTime >= maxTime) {
				experimentViewer.setEndFlag();
			}
		}
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishExperiment() {
		// TODO Auto-generated method stub
		return null;
	}

}
