package experimentGUI.experimentEditor.tabbedPane.settingsEditor;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.util.List;

import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
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
			// adding checkboxes for the given possible settings in Settings.java
			for (PluginInterface plugin : PluginList.getPlugins()) {
				List<SettingsComponentDescription> descriptions = plugin.getSettingsComponentDescriptions(selected);
				if (descriptions!=null) {
					for (SettingsComponentDescription desc : descriptions) {
						add(desc.build(selected));
					}
				}
			}
		}
	}
}