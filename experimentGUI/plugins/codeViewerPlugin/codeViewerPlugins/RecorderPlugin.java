package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginList;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.loggingTreeNode.LoggingTreeNode;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.loggingTreeNode.LoggingTreeXMLHandler;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;

public class RecorderPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "recorder";
	public final static String KEY_FILENAME = "filename";
	public final static String TYPE_OPENED = "opened";
	public final static String TYPE_CLOSED = "closed";
	public final static String TYPE_VIEWERCLOSED = "viewerclosed";	
	public static final String ATTRIBUTE_PATH = "path";
	
	private boolean enabled;
	
	private LoggingTreeNode rootNode;
	private LoggingTreeNode currentNode;
	
	private CodeViewer codeViewer;
	private EditorTabbedPane tabbedPane;
	private EditorPanel currentTab;
	
	private QuestionTreeNode selected;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Recorder aktivieren");
		for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
			SettingsComponentDescription desc = plugin.getSettingsComponentDescription();
			if (desc!=null) {
				result.addSubComponent(desc);
				while ((desc = desc.getNextComponentDescription()) != null) {
					result.addSubComponent(desc);
				}
			}
		}
		return result;
	}

	@Override
	public void init(QuestionTreeNode selected) {
		this.selected = selected;
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));
	}

	@Override
	public void onFrameCreate(CodeViewer viewer) {
		if (enabled) {
			rootNode = new LoggingTreeNode(LoggingTreeNode.TYPE_LOGFILE);
			currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
			rootNode.add(currentNode);
			
			codeViewer=viewer;
			tabbedPane=codeViewer.getTabbedPane();
			currentTab=null;
			
			viewer.getTabbedPane().addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					if (currentTab!=tabbedPane.getSelectedComponent()) {
						//Baum aktualisieren: neuer Zweig
						currentTab = (EditorPanel)tabbedPane.getSelectedComponent();
						if (currentTab == null) {
							currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
						} else {
							currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_FILE);
							currentNode.setAttribute(ATTRIBUTE_PATH, currentTab.getFilePath());
						}
						rootNode.add(currentNode);
						//Plugins aktualisieren
						for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
							plugin.onNodeChange(currentNode,currentTab);
						}
					}
				}
			});
			for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
				plugin.onFrameCreate(selected.getAddAttribute(KEY),codeViewer, currentNode);
			}
		}
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
		if (enabled) {
			LoggingTreeNode openedNode = new LoggingTreeNode(TYPE_OPENED);
			openedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFilePath());
			currentNode.add(openedNode);
		}
	}
	
	@Override
	public void onEditorPanelClose(EditorPanel editorPanel) {
		if (enabled) {
			LoggingTreeNode closedNode = new LoggingTreeNode(TYPE_CLOSED);
			closedNode.setAttribute(ATTRIBUTE_PATH, editorPanel.getFilePath());
			currentNode.add(closedNode);
		}
	}
	
	@Override
	public void onClose() {
		if(enabled) {
			LoggingTreeNode node = new LoggingTreeNode(TYPE_VIEWERCLOSED);
			currentNode.add(node);
			LoggingTreeXMLHandler.saveXMLTree(rootNode, codeViewer.getSaveDir().getPath()+System.getProperty("file.separator")+"recorder.xml");
		}
	}
}
