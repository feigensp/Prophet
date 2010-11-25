package experimentGUI.plugins.codeViewer.codeViewerPlugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;
import experimentGUI.experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsPathChooser;
import experimentGUI.plugins.codeViewer.CodeViewer;
import experimentGUI.plugins.codeViewer.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewer.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class PathPlugin implements CodeViewerPluginInterface {
	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsPathChooser.class, "path", "Pfad der Quelltexte:"));
		return result;
	}
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {		
	}

	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
	}
}
