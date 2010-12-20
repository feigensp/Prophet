package experimentGUI;

import java.util.Vector;

import experimentGUI.plugins.CodeViewerPlugin;
import experimentGUI.plugins.DebugPlugin;
import experimentGUI.plugins.MailPlugin;
import experimentGUI.plugins.QuestionListPlugin;


public class PluginList {
	private static Vector<PluginInterface> plugins = new Vector<PluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new CodeViewerPlugin());
			add(new DebugPlugin());
			add(new QuestionListPlugin());
			add(new MailPlugin());
		}
	};
	
	public static Vector<PluginInterface> getPlugins() {
		return plugins;
	}
	public static void add(PluginInterface plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(PluginInterface plugin) {
		return plugins.remove(plugin);
	}
}
