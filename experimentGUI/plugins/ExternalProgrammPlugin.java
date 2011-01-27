package experimentGUI.plugins;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ExternalProgrammPlugin implements PluginInterface {
	//TODO: Prozess angenehm beenden

	private static final String KEY = "start_external_prog";
	private static final String KEY_COMMAND = "command";
	private HashMap<QuestionTreeNode,Process> processes;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isCategory()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Externes Programm starten");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_COMMAND,
					"Programmpfad"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		processes = new HashMap<QuestionTreeNode,Process>();
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}
	
	@Override
	public void enterNode(QuestionTreeNode node) {
		if (node.isCategory()) {				
			boolean categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (categoryEnabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				String command = attributes.getAttributeValue(KEY_COMMAND);
				try {
					processes.put(node, Runtime.getRuntime().exec(command));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Eine externe Anwendung konnte nicht gestartet werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node) {
		Process p = processes.get(node);
		if (node.isCategory() && p!=null) {
			try {
				p.exitValue();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Bitte das gestartete Programm beenden.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
				try {
					p.waitFor();
				} catch (InterruptedException e1) {
					p.destroy();
				}
			}
			p=null;
		}
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
