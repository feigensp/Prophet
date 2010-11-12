package plugins.codeViewer.codeViewerPlugins;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;

public interface CodeViewerPlugin {
	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected);
}
