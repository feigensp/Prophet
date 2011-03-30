package experimentGUI.experimentEditor.tabbedPane.editorTabs;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import experimentGUI.Constants;
import experimentGUI.PluginList;
import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import experimentGUI.util.VerticalLayout;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsCheckBox;
import experimentGUI.util.settingsComponents.components.SettingsTextField;

@SuppressWarnings("serial")
public class SettingsEditorPanel extends ExperimentEditorTab {
	private HashMap<QuestionTreeNode, JScrollPane> scrollPanes;
		
	public SettingsEditorPanel() {
		setLayout(new BorderLayout());
		scrollPanes = new HashMap<QuestionTreeNode,JScrollPane>();
		this.setOpaque(false);
	}

	public void activate(QuestionTreeNode selected) {
		this.removeAll();
		this.updateUI();
		if (selected!=null) {
			JScrollPane scrollPane = scrollPanes.get(selected);
			if (scrollPane==null) {
				JPanel settingsPanel = new JPanel();
				settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				settingsPanel.setLayout(new VerticalLayout(5,VerticalLayout.LEFT,VerticalLayout.TOP));
				
				if (selected.isExperiment()) {
					settingsPanel.add(new SettingsComponentDescription(SettingsTextField.class, Constants.KEY_EXPERIMENT_CODE,
							"Experiment-Code: ").build(selected));
				}
				if (selected.isCategory()) {
					settingsPanel.add(new SettingsComponentDescription(SettingsCheckBox.class,
							Constants.KEY_DONOTSHOWCONTENT, "Inhalt nicht anzeigen").build(selected));
					settingsPanel.add(new SettingsComponentDescription(SettingsCheckBox.class,
							Constants.KEY_QUESTIONSWITCHING, "Vor- und Zurückblättern erlauben").build(selected));
				}
				SettingsComponentDescription desc =PluginList.getSettingsComponentDescription(selected);
				if (desc != null) {
					settingsPanel.add(desc.build(selected));
					while ((desc = desc.getNextComponentDescription()) != null) {
						settingsPanel.add(desc.build(selected));
					}
				}
				scrollPane = new JScrollPane(settingsPanel);
				scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);
				ExperimentEditorTabbedPane.recursiveSetOpaque(scrollPane);
				scrollPanes.put(selected, scrollPane);
			}
			add(scrollPane, BorderLayout.CENTER);
		}
	}
}