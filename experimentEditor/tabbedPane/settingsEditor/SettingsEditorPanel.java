package experimentEditor.tabbedPane.settingsEditor;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.QuestionTreeNode;
import util.VerticalFlowLayout;
import experimentEditor.tabbedPane.ExperimentEditorTab;


@SuppressWarnings("serial")
public class SettingsEditorPanel extends ExperimentEditorTab {
	private QuestionTreeNode selected;
	ActionListener myActionListener;

	/**
	 * Constructor which defines the appearance and do the initialisation of the
	 * variables etc.
	 * 
	 * @param id
	 *            String identifier of this Dialog
	 */
	public SettingsEditorPanel() {
		setLayout(new VerticalFlowLayout());
		myActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SettingsComponent source = (SettingsComponent) event.getSource();
				selected.setAttribute(source.getOptionKey(), source.getValue());
			}
		};
	}
	
	public void setSelected(QuestionTreeNode sel) {
		selected=sel;
		activate();
	}
	public void activate() {
		removeAll();
		updateUI();
		
		if (selected!=null) {
			// adding checkboxes for the given possible settings in Settings.java
			for (SettingsOption setting : Settings.settings) {
				SettingsComponent opt = setting.newInstance(selected.getAttribute(setting.getKey()));
				opt.addActionListener(myActionListener);
				add((Component)opt);			
			}
		}
	}
}