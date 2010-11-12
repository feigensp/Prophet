package plugins.codeViewer.codeViewerPlugins.SearchBar;

import java.util.List;
import java.util.Vector;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class SearchBarPlugin implements CodeViewerPlugin {

	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected) {
		Vector<SettingsComponentFactory> result = new Vector<SettingsComponentFactory>();
		if (selected.isCategory()) {
			result.add(new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_searchable", "Suchfunktion einschalten"));
		}
		return result;
	}
}
