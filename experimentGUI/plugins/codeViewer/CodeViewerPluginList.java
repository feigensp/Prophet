package experimentGUI.plugins.codeViewer;

import java.util.Vector;

import experimentGUI.plugins.codeViewer.codeViewerPlugins.EditabilityPlugin;
import experimentGUI.plugins.codeViewer.codeViewerPlugins.LineNumbersPlugin;
import experimentGUI.plugins.codeViewer.codeViewerPlugins.PathPlugin;
import experimentGUI.plugins.codeViewer.codeViewerPlugins.SearchBarPlugin;
import experimentGUI.plugins.codeViewer.codeViewerPlugins.SyntaxHighlightingPlugin;


public class CodeViewerPluginList {
	private static Vector<CodeViewerPluginInterface> plugins = new Vector<CodeViewerPluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new PathPlugin());
			add(new EditabilityPlugin());
			add(new LineNumbersPlugin());
			add(new SearchBarPlugin());
			add(new SyntaxHighlightingPlugin());
		}
	};
	
	public static Vector<CodeViewerPluginInterface> getPlugins() {
		return plugins;
	}
	public static void add(CodeViewerPluginInterface plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(CodeViewerPluginInterface plugin) {
		return plugins.remove(plugin);
	}
}
