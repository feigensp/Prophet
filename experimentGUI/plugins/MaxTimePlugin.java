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
	private int maxTimeExperiment = 0;
	private int maxTimeCategory = 0;
	private boolean experimentEnabled;
	private boolean categoryEnabled;
	private long startTimeCategory = 0;
	
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
		try {
			if (node.isExperiment()) {
				experimentEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
				if (experimentEnabled) {
					QuestionTreeNode attributes = node.getAttribute(KEY);
					maxTimeExperiment = Integer.parseInt(attributes.getAttributeValue(MAX_TIME));
				}
			}
			if (node.isCategory()) {
				categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
				if (categoryEnabled) {
					QuestionTreeNode attributes = node.getAttribute(KEY);
					maxTimeCategory = Integer.parseInt(attributes.getAttributeValue(MAX_TIME));
					startTimeCategory = System.currentTimeMillis();
				} else {
					maxTimeCategory = 0;
				}
			}
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		if (experimentEnabled && maxTimeExperiment != 0) {
			int currentTime = (int) ((experimentViewer.getTime() / 1000) / 60);
			if (currentTime >= maxTimeExperiment) {
				experimentViewer.setEndFlag();
				experimentEnded = true;
				return;
			}
		}
		if (categoryEnabled && startTimeCategory != 0 && maxTimeCategory != 0) {
			long currentTime = System.currentTimeMillis();
			currentTime -= startTimeCategory;
			if ((int) (currentTime / 1000) >= maxTimeCategory) {
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
			return "Experiment wurde wegen Zeitüberschreitung beendet";
		}
		return null;
	}

}
