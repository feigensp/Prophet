package plugins.codeViewer.codeViewerPlugins.SearchBar;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import plugins.codeViewer.CodeViewer;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.tabbedPane.EditorPanel;
import util.QuestionTreeNode;
import util.SearchBar;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsCheckBox;

public class SearchBarPlugin implements CodeViewerPlugin {

	public List<SettingsComponentDescription> getSettingsComponentDescriptions() {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		result.add(new SettingsComponentDescription(SettingsCheckBox.class,"searchable", "Suchfunktion einschalten"));
		return result;
	}
	
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {		
	}

	public void onEditorPanelCreate(QuestionTreeNode selected,
			EditorPanel editorPanel) {
		if (Boolean.parseBoolean(selected.getAttributeValue("searchable"))) {
			RSyntaxTextArea textPane = editorPanel.getTextArea();
			SearchBar searchBar = new SearchBar(textPane);
			searchBar.setVisible(false);
			editorPanel.add(searchBar, BorderLayout.SOUTH);			
			textPane.addKeyListener(new SearchBarListener(searchBar));
		}
	}
}
