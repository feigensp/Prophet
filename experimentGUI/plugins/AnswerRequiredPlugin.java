package experimentGUI.plugins;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextArea;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class AnswerRequiredPlugin implements PluginInterface {
	
	public static final String KEY = "answerRequired";
	
	private boolean enabled;
	private String requiredAnswers;
	private String wrongComponents = "";
	
	private ExperimentViewer experimentViewer;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
				"Benötigte Antworten:");
		if (node.isCategory() || node.isQuestion()) {
			result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY,
					"Komponenten die Antworten enthalten müssen (zeilenweise eingeben):"));
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
		if (node.isCategory() || node.isQuestion()) {
			enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (enabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				requiredAnswers = attributes.getAttributeValue(KEY);
			}
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		if(enabled) {
			String[] components = requiredAnswers.split("\n");
			TreeMap answers = node.getAnswers();
			Set keys = answers.keySet();
			for(int i=0; i<components.length; i++) {
				if(keys.contains(components[i])) {
					System.out.println("contains " + components[i]);
					if(answers.get(components[i]).equals("")) {
						//experimentViewer.setNoNextFlag("Sie müssen " + components[i] + " ausfüllen um weiter zu können.");
					}
				} else {
					System.out.println("contains not " + components[i]);	
					wrongComponents += "<br>" + components[i];
				}
			}
			if(wrongComponents.length()>0) {
				wrongComponents = "<br>im Knoten " + node.toString() + " nicht gefunden werden.";
			}
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		if(wrongComponents.length()>0) {
			return "<html>Es konnten die Komponenten"+wrongComponents+"</html>";
		}
		return null;
	}

}
