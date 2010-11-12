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
			for (ExperimentPlugin plugin : ExperimentPluginList.getPlugins()) {
				List<SettingsComponentFactory> factories = plugin.getSettingsComponentFactories(selected);
				if (factories!=null) {
					for (SettingsComponentFactory factory : factories) {
						if (factory!=null) {
							add(factory.build());
						}
					}
				}						
			}
		}
	}
}