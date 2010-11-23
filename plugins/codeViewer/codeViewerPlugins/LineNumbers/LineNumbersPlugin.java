package plugins.codeViewer.codeViewerPlugins.LineNumbers;

import java.util.List;
import java.util.Vector;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class LineNumbersPlugin implements CodeViewerPlugin {

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_default", "Zeilennummern anzeigen"));
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"linenumbers_toggle", "Zeilennummern ein- und ausschaltbar"));
		return result;
	}
	
	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		boolean lineNumbers = Boolean.parseBoolean(selected.getAttributeValue("linenumbers_default"));
		editorPanel.getScrollPane().setLineNumbersEnabled(lineNumbers);
	}
}
