package plugins.codeViewer.codeViewerPlugins.Editability;

import java.util.List;
import java.util.Vector;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class EditabilityPlugin implements CodeViewerPlugin {
	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected) {
		Vector<SettingsComponentFactory> result = new Vector<SettingsComponentFactory>();
		if (selected.isCategory()) {
			result.add(new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_editable", "Quelltext editierbar"));
		}
		return result;
	}
}
