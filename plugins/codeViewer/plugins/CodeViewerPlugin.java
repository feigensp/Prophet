package plugins.codeViewer.plugins;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;

public interface CodeViewerPlugin {
	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected);
}
