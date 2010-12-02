package experimentGUI.experimentEditor.tabbedPane;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.util.List;

import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.util.VerticalFlowLayout;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class SettingsEditorPanel extends ExperimentEditorTab {

	/**
	 * Constructor which defines the appearance and do the initialisation of the
	 * variables etc.
	 * 
	 * @param id
	 *            String identifier of this Dialog
	 */
	public SettingsEditorPanel() {
		setLayout(new VerticalFlowLayout());
	}
	
	public void activate(QuestionTreeNode selected) {
		removeAll();
		updateUI();
		
		if (selected!=null) {
			add(new SettingsComponentDescription(SettingsCheckBox.class, "inactive", "Deaktivieren").build(selected));
			for (PluginInterface plugin : PluginList.getPlugins()) {
				SettingsComponentDescription desc = plugin.getSettingsComponentDescription(selected);
				if (desc!=null) {
					add(desc.build(selected));
					while ((desc=desc.getNextComponentDescription())!=null) {
						add(desc.build(selected));
					}
				}
			}
		}
	}
}