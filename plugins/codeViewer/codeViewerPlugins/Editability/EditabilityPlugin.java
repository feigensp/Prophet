package plugins.codeViewer.codeViewerPlugins.Editability;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class EditabilityPlugin implements CodeViewerPlugin {
	public SettingsComponentFactory getSettingsComponentFactory(QuestionTreeNode selected) {
		if(selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_editable", "Quelltext editierbar",selected.getAttribute("codeviewer_editable"));
		}
		return null;
	}
}
