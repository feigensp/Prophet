package experimentGUI;

import java.util.Vector;

import experimentGUI.plugins.CodeViewerPlugin;
import experimentGUI.plugins.ExternalProgramsPlugin;
import experimentGUI.plugins.MailPlugin;
import experimentGUI.plugins.MaxTimePlugin;
import experimentGUI.plugins.QuestionListPlugin;
import experimentGUI.plugins.ValidCodePlugin;

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
			add(new ValidCodePlugin());
			add(new CodeViewerPlugin());
			add(new QuestionListPlugin());
			add(new MailPlugin());
			add(new MaxTimePlugin());
			add(new ExternalProgramsPlugin());
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
}
