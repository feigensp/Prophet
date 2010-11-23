package plugins.codeViewer.codeViewerPlugins.UndoRedo;

import java.util.List;
import java.util.Vector;

import javax.swing.undo.UndoManager;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class UndoRedoPlugin implements CodeViewerPlugin {

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"undoredo", "Undo und Redo einschalten"));
		return result;
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
//		if (Boolean.parseBoolean(selected.getAttributeValue("undoredo"))) {
//			UndoManager undoManager = new UndoManager();
//			editorPanel.getTextArea().getDocument().addUndoableEditListener(undoManager);
//			editorPanel.getTextArea().addKeyListener(new UndoRedoKeyListener(undoManager));
//		}
	}
}
