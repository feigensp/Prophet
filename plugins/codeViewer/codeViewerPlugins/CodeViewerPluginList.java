package plugins.codeViewer.codeViewerPlugins;

import java.util.Vector;

import plugins.codeViewer.codeViewerPlugins.Editability.EditabilityPlugin;
import plugins.codeViewer.codeViewerPlugins.LineNumbers.LineNumbersPlugin;
import plugins.codeViewer.codeViewerPlugins.SearchBar.SearchBarPlugin;
import plugins.codeViewer.codeViewerPlugins.SyntaxHighlighting.SyntaxHighlightingPlugin;
import plugins.codeViewer.codeViewerPlugins.UndoRedo.UndoRedoPlugin;

public class CodeViewerPluginList {
	private static Vector<CodeViewerPlugin> plugins = new Vector<CodeViewerPlugin>() {
		{
			add(new EditabilityPlugin());
			add(new LineNumbersPlugin());
			add(new SearchBarPlugin());
			add(new SyntaxHighlightingPlugin());
			add(new UndoRedoPlugin());
		}
	};
	
	public static Vector<CodeViewerPlugin> getPlugins() {
		return plugins;
	}
	public static void add(CodeViewerPlugin plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(CodeViewerPlugin plugin) {
		return plugins.remove(plugin);
	}
}
