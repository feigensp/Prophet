package plugins.codeViewer.plugins.SearchBar;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.plugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class SearchBarPlugin implements CodeViewerPlugin {

	@Override
	public SettingsComponentFactory getSettingsComponentFactory(
			QuestionTreeNode selected) {
		if(selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_searchable", "Suchfunktion einschalten",selected.getAttribute("codeviewer_searchable"));
		}
		return null;
	}

}
