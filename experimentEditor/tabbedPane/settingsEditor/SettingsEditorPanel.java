package experimentEditor.tabbedPane.settingsEditor;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.util.List;

import plugins.ExperimentPlugin;
import plugins.ExperimentPluginList;
import util.QuestionTreeNode;
import util.VerticalFlowLayout;
import experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;


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
			if (selected.isCategory() || selected.isQuestion()) {
				add(new SettingsComponentDescription(SettingsCheckBox.class, "inactive", "Deaktivieren").build(selected));
			}
			if (selected.isExperiment() || selected.isCategory()) {
				add(new SettingsComponentDescription(SettingsCheckBox.class, "donotshowcontent", "Inhalt nicht anzeigen").build(selected));
			}
			// adding checkboxes for the given possible settings in Settings.java
			for (ExperimentPlugin plugin : ExperimentPluginList.getPlugins()) {
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