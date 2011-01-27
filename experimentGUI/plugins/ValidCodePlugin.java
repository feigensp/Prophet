package experimentGUI.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import experimentGUI.Constants;
import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsFilePathChooser;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextArea;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ValidCodePlugin implements PluginInterface {

	public static final String KEY = "validCode";
	public static final String KEY_CODES = "codes";
	public static final String KEY_FILE = "file";
	public static final String KEY_PATH = "path";
	public static final String KEY_IGNORE_CASE = "ignorecase";
	private boolean checked = false;
	private boolean enabled;
	QuestionTreeNode experimentNode;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isExperiment()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Auf Probandencode überprüfen");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY_CODES,
			"gültige Codes (optional)"));
			result.addSubComponent(new SettingsComponentDescription(SettingsFilePathChooser.class, KEY_PATH,
			"Probandencodedatei (optional)"));
			result.addSubComponent(new SettingsComponentDescription(SettingsCheckBox.class, KEY_IGNORE_CASE,
			"Groß- und Kleinschreibung ignorieren"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		if (node.isExperiment()) {
			enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			experimentNode = node;
		} else if (!checked && enabled) {
			String subjectCode = experimentNode.getAnswer(Constants.KEY_SUBJECT);
			boolean ignoreCase = Boolean.parseBoolean(experimentNode.getAttribute(KEY).getAttributeValue(KEY_IGNORE_CASE));
			if (ignoreCase) {
				subjectCode = subjectCode.toLowerCase();
			}
			
			String codes = experimentNode.getAttribute(KEY).getAttributeValue(KEY_CODES);
			if (codes!=null && !codes.equals("")) {
				Scanner sc = new Scanner(codes);
				while(sc.hasNext()) {
					String line = sc.next();
					if (ignoreCase) {
						line = line.toLowerCase();
					}
					if (subjectCode.equals(line)) {
						checked = true;
						return null;
					}
				}
			}
			
			String path = experimentNode.getAttribute(KEY).getAttributeValue(KEY_PATH);
			if (path!=null && !path.equals("")) {				
				try {
					Scanner sc = new Scanner(new FileReader(path));
					while(sc.hasNext()) {
						String line = sc.next();
						if (ignoreCase) {
							line = line.toLowerCase();
						}
						if (subjectCode.equals(line)) {
							checked = true;
							return null;
						}
					}
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null,
							"Datei mit gültigen Probandencodes existiert nicht, Programm wird beendet.", "Fehler",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}				
			}
			JOptionPane.showMessageDialog(null,
					"Kein gültiger Probandencode, Programm wird beendet.", "Fehler",
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
