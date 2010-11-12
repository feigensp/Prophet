package plugins.codeViewer.codeViewerPlugins.LineNumbers;

import java.util.List;
import java.util.Vector;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class LineNumbersPlugin implements CodeViewerPlugin {

	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected) {
		Vector<SettingsComponentFactory> result = new Vector<SettingsComponentFactory>();
		if (selected.isCategory()) {
			result.add(new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_linenumbers_default", "Zeilennummern anzeigen"));
			result.add(new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_linenumbers_toggle", "Zeilennummern ein- und ausschaltbar"));
		}
		return result;
	}
}
