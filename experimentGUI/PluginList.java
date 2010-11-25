package experimentGUI;

import java.util.Vector;

import experimentGUI.plugins.DebugPlugin;
import experimentGUI.plugins.DoNotShowContentPlugin;
import experimentGUI.plugins.InactivityPlugin;
import experimentGUI.plugins.QuestionSwitchingPlugin;
import experimentGUI.plugins.codeViewer.CodeViewerPlugin;
import experimentGUI.plugins.questionLists.QuestionListPlugin;


public class PluginList {
	private static Vector<PluginInterface> plugins = new Vector<PluginInterface>() {
		{
			add(new InactivityPlugin());
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
