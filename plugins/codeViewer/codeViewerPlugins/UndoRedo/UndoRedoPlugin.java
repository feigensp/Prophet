package plugins.codeViewer.codeViewerPlugins.UndoRedo;

import java.util.List;
import java.util.Vector;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import util.QuestionTreeNode;

public class UndoRedoPlugin implements CodeViewerPlugin {

	public List<SettingsComponentFactory> getSettingsComponentFactories(QuestionTreeNode selected) {
		Vector<SettingsComponentFactory> result = new Vector<SettingsComponentFactory>();
		if (selected.isCategory()) {
			result.add(new SettingsComponentFactory(selected, new SettingsCheckBox(),"codeviewer_undoredo", "Undo und Redo einschalten")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return result;
	}
}
