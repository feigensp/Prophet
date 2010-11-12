package plugins.codeViewer.codeViewerPlugins;

import java.util.List;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;

public interface CodeViewerPlugin {
	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected);
}
