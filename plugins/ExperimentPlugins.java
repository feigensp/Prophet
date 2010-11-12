package plugins;

import java.util.Vector;

import plugins.QuestionSwitching.QuestionSwitchingExperimentPlugin;
import plugins.codeViewer.CodeViewerExperimentPlugin;

public class ExperimentPlugins {
	private static Vector<ExperimentPlugin> plugins = new Vector<ExperimentPlugin>() {
		{
			add(new CodeViewerExperimentPlugin());
			add(new QuestionSwitchingExperimentPlugin());
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
