package experimentGUI.plugins;

import java.io.IOException;

import javax.swing.JOptionPane;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ExternalProgrammPlugin implements PluginInterface {
	//TODO: Prozess angenehm beenden

	public static final String KEY = "startExternalProg";
	public static final String COMMAND = "command";
	private Process p;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isCategory()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Externes Programm starten");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, COMMAND,
					"Programmpfad"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		if (node.isCategory()) {				
			boolean categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (categoryEnabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				String command = attributes.getAttributeValue(COMMAND);
				try {
					p = Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Eine externe Anwendung konnte nicht gestartet werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
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
