package plugins.codeViewer.plugins;

import java.util.Vector;

import plugins.codeViewer.plugins.Editability.EditabilityPlugin;
import plugins.codeViewer.plugins.LineNumbers.LineNumbersPlugin;
import plugins.codeViewer.plugins.SearchBar.SearchBarPlugin;
import plugins.codeViewer.plugins.SyntaxHighlighting.SyntaxHighlightingPlugin;
import plugins.codeViewer.plugins.UndoRedo.UndoRedoPlugin;

public class CodeViewerPlugins {
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
