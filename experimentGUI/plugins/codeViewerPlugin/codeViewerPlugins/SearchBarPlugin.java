package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.recorder.loggingTreeNode.LoggingTreeNode;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.searchBar.SearchBar;
import experimentGUI.util.searchBar.SearchBarCtrlFListener;
import experimentGUI.util.searchBar.SearchBarListener;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsCheckBox;

public class SearchBarPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "searchable";
	
	public final static String TYPE_SEARCH = "search";
	public final static String ATTRIBUTE_ACTION = "action";
	public final static String ATTRIBUTE_QUERY = "query";
	public final static String ATTRIBUTE_SUCCESS = "success";
	private boolean enabled;
	private CodeViewer viewer;
	
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
		this.viewer=viewer;
	}
	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		if (enabled) {
			RSyntaxTextArea textPane = editorPanel.getTextArea();
			SearchBar searchBar = new SearchBar(textPane);
			searchBar.setVisible(false);
			searchBar.addSearchBarListener(new SearchBarListener() {

				@Override
				public void searched(String action, String query,
						boolean success) {
					LoggingTreeNode node = new LoggingTreeNode(TYPE_SEARCH);
					node.setAttribute(ATTRIBUTE_ACTION, action);
					node.setAttribute(ATTRIBUTE_QUERY, query);
					node.setAttribute(ATTRIBUTE_SUCCESS, ""+success);
					viewer.getRecorder().addLoggingTreeNode(node);		
				}
				
			});
			editorPanel.add(searchBar, BorderLayout.SOUTH);			
			textPane.addKeyListener(new SearchBarCtrlFListener(searchBar));
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
