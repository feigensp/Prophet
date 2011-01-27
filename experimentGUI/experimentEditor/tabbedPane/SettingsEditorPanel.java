package experimentGUI.experimentEditor.tabbedPane;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import experimentGUI.Constants;
import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponent;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.util.VerticalLayout;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class SettingsEditorPanel extends ExperimentEditorTab {
	JPanel myPanel;

	/**
	 * Constructor which defines the appearance and do the initialisation of the
	 * variables etc.
	 * 
	 * @param id
	 *            String identifier of this Dialog
	 */
	public SettingsEditorPanel() {
		setLayout(new BorderLayout());
		myPanel = new JPanel();
		myPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		myPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));
		add(new JScrollPane(myPanel), BorderLayout.CENTER);
	}

	public void activate(QuestionTreeNode selected) {
		myPanel.removeAll();
		myPanel.updateUI();

		if (selected != null) {
			if (selected.isExperiment()) {
				myPanel.add(new SettingsComponentDescription(SettingsTextField.class, Constants.KEY_EXPERIMENT_CODE,
						"Experiment-Code: ").build(selected));
			}
			if (selected.isCategory()) {
				myPanel.add(new SettingsComponentDescription(SettingsCheckBox.class,
						Constants.KEY_DONOTSHOWCONTENT, "Inhalt nicht anzeigen").build(selected));
				myPanel.add(new SettingsComponentDescription(SettingsCheckBox.class,
						Constants.KEY_QUESTIONSWITCHING, "Vor- und Zurückblättern erlauben").build(selected));
			}
			for (PluginInterface plugin : PluginList.getPlugins()) {
				SettingsComponentDescription desc = plugin.getSettingsComponentDescription(selected);
				if (desc != null) {
					myPanel.add(desc.build(selected));
					while ((desc = desc.getNextComponentDescription()) != null) {
						myPanel.add(desc.build(selected));
					}
				}
			}
		}
	}
}