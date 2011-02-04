package experimentGUI;

import java.util.Vector;

import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.plugins.AnswerRequiredPlugin;
import experimentGUI.plugins.CodeViewerPlugin;
import experimentGUI.plugins.ExternalProgramsPlugin;
import experimentGUI.plugins.InactivityPlugin;
import experimentGUI.plugins.MailPlugin;
import experimentGUI.plugins.MaxTimePlugin;
import experimentGUI.plugins.QuestionListPlugin;
import experimentGUI.plugins.ValidSubjectCodePlugin;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;

/**
 * Class to store active plugins
 * 
 * @author Andreas Hasselberg
 * @author Markus Köppen
 *
 */
public class PluginList {
	private static Vector<PluginInterface> plugins = new Vector<PluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new InactivityPlugin());
			add(new ValidSubjectCodePlugin());
			add(new CodeViewerPlugin());
			add(new QuestionListPlugin());
			add(new MailPlugin());
			add(new MaxTimePlugin());
			add(new ExternalProgramsPlugin());
			add(new AnswerRequiredPlugin());
		}
	};
	/**
	 * 
	 * @return
	 * 	Vector describing all active plugins
	 */
	public static Vector<PluginInterface> getPlugins() {
		return plugins;
	}
	/**
	 * Adds and activates a plugin
	 * @param plugin
	 * 	Plugin to be added and activated
	 */
	public static void add(PluginInterface plugin) {
		plugins.add(plugin);
	}
	/**
	 * Removes and deactives a plugin
	 * @param plugin
	 * 	Plugin to be deactivated
	 * @return
	 * 	<b>true</b> if a plugin is removed<br>
	 * 	<b>false</b> if it wasn't active
	 */
	public static boolean remove(PluginInterface plugin) {
		return plugins.remove(plugin);
	}

	public static SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		SettingsComponentDescription result = null;
		for (PluginInterface plugin : plugins) {
			SettingsComponentDescription desc = plugin.getSettingsComponentDescription(node);
			if (desc!=null) {
				if (result==null) {
					result=desc;
				} else {
					result.addNextComponent(desc);
				}
			}
		}
		return result;
	}

	public static void experimentViewerRun(ExperimentViewer experimentViewer) {
		for (PluginInterface plugin : plugins) {
			plugin.experimentViewerRun(experimentViewer);
		}		
	}

	public static boolean denyEnterNode(QuestionTreeNode node) {
		for (PluginInterface plugin : plugins) {
			if (plugin.denyEnterNode(node)) {
				return true;
			}
		}
		return false;
	}

	public static void enterNode(QuestionTreeNode node) {
		for (PluginInterface plugin : plugins) {
			plugin.enterNode(node);
		}
	}

	public static String denyNextNode(QuestionTreeNode currentNode) {
		String result = null;
		for (PluginInterface plugin : plugins) {
			String ret = plugin.denyNextNode(currentNode);
			if (ret!=null) {
				result="";
				if (ret.length()>0) {
					return ret;
				}
			}
		}
		return result;
	}

	public static void exitNode(QuestionTreeNode node) {
		for (PluginInterface plugin : plugins) {
			plugin.exitNode(node);
		}
	}

	public static String finishExperiment() {
		String result = "";
		for (PluginInterface plugin : plugins) {
			String ret = plugin.finishExperiment();
			if (ret!=null && ret.length()>0) {
				result+="<p>"+ret+"</p>";
			}
		}
		return result;
	}
}
