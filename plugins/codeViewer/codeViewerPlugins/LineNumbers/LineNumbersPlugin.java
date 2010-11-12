package plugins.codeViewer.codeViewerPlugins.LineNumbers;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class LineNumbersPlugin implements CodeViewerPlugin {

	@Override
	public SettingsComponentFactory getSettingsComponentFactory(
			QuestionTreeNode selected) {
		if (selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_linenumbers", "Zeilennummern anzeigen",selected.getAttribute("codeviewer_linenumbers"));
		}
		return null;
	}
}
