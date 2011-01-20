package experimentGUI;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
/**
 * Interface for all plugins to be used immediately within the product. It represents methods called on special occasions.
 * 
 * @author Andreas Hasselberg
 * @author Markus Köppen
 *
 */
public interface PluginInterface {
	/**
	 * Delivers settings components shown in the settings tab of the experiment editor
	 * @param node
	 * 	The currently active node within the experiment viewer
	 * @return
	 * 	A SettingsComponentDescription that describes the possible settings of the plugin or null if there are none
	 */
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node);
	/**
	 * Called once when the experiment viewer is initialized. May be used to manipulate the viewer.
	 * @param experimentViewer
	 * 	The initialized experiment viewer
	 */
	public void experimentViewerRun(ExperimentViewer experimentViewer);
	/**
	 * Called when a node is entered, at the beginning of categories or questions. Is not called on inactive categories or questions.
	 * @param node
	 * 	The node entered
	 * @return
	 * 	Data that is returned in exitNode() as pluginData, only for plugin-intern use. May be null.
	 */	
	public Object enterNode(QuestionTreeNode node);
	/**
	 * Called when a node is exited, e.g. after a question or after all active questions of a category are exited. Is not called on inactive categories or questions.
	 * @param node
	 * @param pluginData
	 */
	public void exitNode(QuestionTreeNode node, Object pluginData);
	/**
	 * @return
	 * 	A (hopefully) unique name for the plugin.
	 */
	public String getKey();
	/**
	 * Called after all nodes have been visited. Allows the plugin to do last steps upon finishing the experiment
	 * @return
	 * 	A message shown to the subject at experiment's end
	 */
	public String finishExperiment();
}
