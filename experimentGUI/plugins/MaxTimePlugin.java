package experimentGUI.plugins;

import java.util.HashMap;
import java.util.HashSet;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class MaxTimePlugin implements PluginInterface {

	private static final String KEY = "max_time";
	private static final String KEY_MAX_TIME = "time";
	private static final String KEY_HARD_EXIT = "hard_exit";
	private static final String KEY_WARNING = "show_warning";
	private static final String KEY_WARNING_TIME = "warning_time";
	private static final String KEY_IGNORE_TIMEOUT = "ignore_timeout";

	private ExperimentViewer experimentViewer;
	private HashMap<QuestionTreeNode,Integer> startTimes;
	private HashMap<QuestionTreeNode,Thread> threads;
	private HashSet<QuestionTreeNode> timeOuts;

	private QuestionTreeNode experimentNode;
	private boolean activateForExperiment;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
				"Timeout");
		if (node.isExperiment()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_MAX_TIME,
					"Maximale Laufzeit (gesamtes Experiment, in Minuten):"));
		}
		if (node.isCategory()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_MAX_TIME,
					"Maximale Laufzeit (gesamte Kategorie, in Sekunden):"));
		}
		if (node.isQuestion()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_MAX_TIME,
					"Maximale Laufzeit (diese Frage, in Sekunden):"));
		}
		SettingsPluginComponentDescription hardExit = new SettingsPluginComponentDescription(KEY_HARD_EXIT,
				"Angezeigte Frage bei Zeitüberschreitung beenden (harter Timeout)");
		SettingsPluginComponentDescription warning = new SettingsPluginComponentDescription(KEY_WARNING,
				"Probanden vorwarnen");
		warning.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,KEY_WARNING_TIME,
				"Vorwarnzeit (Sekunden):"));
		hardExit.addSubComponent(warning);
		result.addSubComponent(hardExit);
		if (node.isCategory()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsCheckBox.class,KEY_IGNORE_TIMEOUT,
					"Diese Kategorie auch bei abgelaufenem Experiment-Timeout anzeigen; eigener Timeout dann wirkungslos"));
		}
		return result;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		this.experimentViewer = experimentViewer;
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}
	
	@Override
	public void enterNode(QuestionTreeNode node) {
		boolean enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
		if (node.isExperiment()) {
			experimentNode = node;
			if (enabled) {
				activateForExperiment = true;
			}
			return;
		}
		if (activateForExperiment) {
			//TimeOut für Experiment starten
			activateForExperiment=false;
		}
		if (enabled) {
			//TimeOut für aktuellen knoten starten
		}
	}

	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node) {
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		if(timeOuts.contains(experimentNode)) {
			return "Experiment wurde wegen Zeitüberschreitung beendet.";
		}
		return null;
	}
}
