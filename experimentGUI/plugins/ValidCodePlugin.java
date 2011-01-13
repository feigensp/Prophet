package experimentGUI.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import experimentGUI.Constants;
import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsFilePathChooser;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ValidCodePlugin implements PluginInterface {

	public static final String KEY = "validCode";
	public static final String PATH = "path";
	private boolean checked = false;
	private boolean enabled;
	private ExperimentViewer experimentViewer;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isExperiment()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Probandencodeliste");
			result.addSubComponent(new SettingsComponentDescription(SettingsFilePathChooser.class, PATH,
					"Probandencodedatei"));
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
			enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
		} else if (!checked && enabled) {
			QuestionTreeNode experimentNode = node;
			while (!experimentNode.isExperiment()) {
				experimentNode = (QuestionTreeNode) experimentNode.getParent();
			}
			String subjectCode = experimentNode.getAttributeValue(Constants.KEY_SUBJECT);
			String path = experimentNode.getAttribute(KEY).getAttributeValue(PATH);
			try {
				if (new File(path).exists()) {
					BufferedReader in = new BufferedReader(new FileReader(path));
					String line = null;
					while ((line = in.readLine()) != null) {
						if (line.equals(subjectCode)) {
							checked = true;
							return null;
						}
					}
					JOptionPane.showMessageDialog(null,
							"Kein gültiger Probandencode, Programm wird beendet.", "Ungültiger Code",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			} catch (IOException e) {
				// do that what happens after the catch Block
			}
			JOptionPane.showMessageDialog(null,
					"Problem beim auslesen der zulässigen Probandencodes, Programm wird beendet.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
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
