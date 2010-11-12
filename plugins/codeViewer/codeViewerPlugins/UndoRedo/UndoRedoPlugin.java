package plugins.codeViewer.codeViewerPlugins.UndoRedo;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class UndoRedoPlugin implements CodeViewerPlugin {

	@Override
	public SettingsComponentFactory getSettingsComponentFactory(
			QuestionTreeNode selected) {
		if(selected.isCategory()) {
			return new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_undoredo", "Undo und Redo einschalten",selected.getAttribute("codeviewer_undoredo"));
		}
		return null;
	}

}
