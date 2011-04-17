package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.searchBar.SearchBar;
import experimentGUI.util.searchBar.SearchBarListener;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsCheckBox;

public class SearchBarPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "searchable";
	private boolean enabled;
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class,KEY, "Suchfunktion einschalten");
	}
	@Override
	public void init(QuestionTreeNode selected) {
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));		
	}
	@Override
	public void onFrameCreate(CodeViewer viewer) {
	}
	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		if (enabled) {
			RSyntaxTextArea textPane = editorPanel.getTextArea();
			SearchBar searchBar = new SearchBar(textPane);
			searchBar.setVisible(false);
			editorPanel.add(searchBar, BorderLayout.SOUTH);			
			textPane.addKeyListener(new SearchBarListener(searchBar));
		}
	}
	@Override
	public void onClose() {
	}
	@Override
	public void onEditorPanelClose(EditorPanel editorPanel) {
		// TODO Auto-generated method stub
		
	}
}
