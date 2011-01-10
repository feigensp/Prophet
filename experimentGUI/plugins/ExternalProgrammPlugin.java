package experimentGUI.plugins;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ExternalProgrammPlugin implements PluginInterface {

	public static final String KEY = "startExternalProg";
	public static final String COMMAND = "command";
	private boolean categoryEnabled = false;
	private Process p;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isCategory()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Externes Programm starten/Kommandozeilenbefehl ausführen");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, COMMAND,
					"Programmpfad/Kommandozeilenbefehl:"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		try {
			if (node.isCategory()) {
				if (categoryEnabled) {
					p.destroy();
				}
				categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
				if (categoryEnabled) {
					QuestionTreeNode attributes = node.getAttribute(KEY);
					String command = attributes.getAttributeValue(COMMAND);
					p = Runtime.getRuntime().exec(command);
				}
			}
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishExperiment() {
		p.destroy();
		return null;
	}

}
