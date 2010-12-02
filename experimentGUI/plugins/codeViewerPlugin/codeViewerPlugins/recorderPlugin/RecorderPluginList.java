package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import java.util.Vector;

import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins.ChangePlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins.ScrollingPlugin;


public class RecorderPluginList {
	private static Vector<RecorderPluginInterface> plugins = new Vector<RecorderPluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new ChangePlugin());
			add(new ScrollingPlugin());
		}
	};
	
	public static Vector<RecorderPluginInterface> getPlugins() {
		return plugins;
	}
	public static void add(RecorderPluginInterface plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(RecorderPluginInterface plugin) {
		return plugins.remove(plugin);
	}
}
