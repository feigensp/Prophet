package experimentGUI.plugins;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextArea;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class AnswerRequiredPlugin implements PluginInterface {
	
	private static final String KEY = "answers_required";
	private static final String KEY_NAMES = "names";

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
			"Benötigte Antworten");
		result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY_NAMES,
			"Komponenten, die Antworten enthalten müssen (zeilenweise eingeben):"));
		return result;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}
	
	@Override
	public void enterNode(QuestionTreeNode node) {
	}

	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
		boolean enabled = Boolean.parseBoolean(currentNode.getAttributeValue(KEY));
			if (enabled) {
				String requiredAnswers = currentNode.getAttribute(KEY).getAttributeValue(KEY_NAMES);
				
				TreeMap<String,String> answers = currentNode.getAnswers();
				
				Scanner sc = new Scanner(requiredAnswers);
				while(sc.hasNext()) {
					String requiredAnswerKey = sc.next();
					if(!answers.containsKey(requiredAnswerKey) || answers.get(requiredAnswerKey).equals("")) {
						return "Bitte alle benötigten Felder ausfüllen";
					}
				}
		}
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
		return null;
	}
}
