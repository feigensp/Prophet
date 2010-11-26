package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.searchBar.SearchBar;
import experimentGUI.util.searchBar.SearchBarListener;

public class SearchBarPlugin implements CodeViewerPluginInterface {

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
