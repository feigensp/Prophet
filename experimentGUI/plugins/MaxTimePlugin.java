package experimentGUI.plugins;

import javax.swing.JOptionPane;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class MaxTimePlugin implements PluginInterface {

	public static final String KEY = "maxTime";
	public static final String MAX_TIME = "maxTime";

	private ExperimentViewer experimentViewer;
	private int maxTimeExperiment;
	private int maxTimeCategory;
	private boolean experimentEnabled;
	private boolean categoryEnabled;
	private long startTimeCategory;
	private long startTimeExperiment;
	
	private boolean experimentEnded = false;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
				"Maximale Bearbeitungszeit");
		if (node.isExperiment()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, MAX_TIME,
					"Maximale Laufzeit (in Minuten):"));
			return result;
		}
		if (node.isCategory()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, MAX_TIME,
					"Maximale Laufzeit (in Sekunden):"));
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
		if (node.isExperiment()) {
			experimentEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (experimentEnabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				maxTimeExperiment = Integer.parseInt(attributes.getAttributeValue(MAX_TIME));
				startTimeExperiment = System.currentTimeMillis();
			}
		}
		if (node.isCategory()) {
			categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (categoryEnabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				maxTimeCategory = Integer.parseInt(attributes.getAttributeValue(MAX_TIME));
				startTimeCategory = System.currentTimeMillis();
			}
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		if (experimentEnabled) {
			long currentTimeInMins = (System.currentTimeMillis() - startTimeExperiment) / 1000 / 60;
			if (currentTimeInMins >= maxTimeExperiment) {
				experimentViewer.setEndFlag();
				experimentEnded = true;
				return;
			}
		}
		if (categoryEnabled) {
			categoryEnabled=false;
			long currentTimeInSecs = System.currentTimeMillis() - startTimeCategory / 1000;
			if (currentTimeInSecs >= maxTimeCategory) {
				experimentViewer.setNextCategoryFlag();
				JOptionPane.showMessageDialog(null, "Bearbeitungszeit für Kategorie abgelaufen.");
				return;
			}
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		if(experimentEnded) {
			return "Experiment wurde wegen Zeitüberschreitung beendet.";
		}
		return null;
	}

}
