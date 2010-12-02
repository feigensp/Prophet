package experimentGUI;

import java.util.Vector;

import experimentGUI.plugins.CodeViewerPlugin;
import experimentGUI.plugins.DebugPlugin;
import experimentGUI.plugins.DoNotShowContentPlugin;
import experimentGUI.plugins.QuestionListPlugin;
import experimentGUI.plugins.QuestionSwitchingPlugin;


public class PluginList {
	private static Vector<PluginInterface> plugins = new Vector<PluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new DoNotShowContentPlugin());
			add(new CodeViewerPlugin());
			add(new QuestionSwitchingPlugin());
			add(new DebugPlugin());
			add(new QuestionListPlugin());
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
