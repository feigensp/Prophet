package plugins;

import java.util.Vector;

import plugins.QuestionSwitching.ExperimentQuestionSwitchingPlugin;
import plugins.codeViewer.ExperimentCodeViewerPlugin;

public class ExperimentPluginList {
	private static Vector<ExperimentPlugin> plugins = new Vector<ExperimentPlugin>() {
		{
			add(new ExperimentCodeViewerPlugin());
			add(new ExperimentQuestionSwitchingPlugin());
			add(new DebugPlugin());
		}
	};
	
	public static Vector<ExperimentPlugin> getPlugins() {
		return plugins;
	}
	public static void add(ExperimentPlugin plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(ExperimentPlugin plugin) {
		return plugins.remove(plugin);
	}
}
