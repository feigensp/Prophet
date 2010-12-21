package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginList;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.loggingTreeNode.LoggingTreeXMLHandler;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class RecorderPlugin implements CodeViewerPluginInterface,ChangeListener {
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
	
	private HashSet<Component> openTabs;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Recorder aktivieren");
		result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_FILENAME, "Dateiname der Aufnahme (default: \"recorder\"):"));
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
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));
		if (enabled) {
			rootNode = new LoggingTreeNode(LoggingTreeNode.TYPE_LOGFILE);
			currentNode = new LoggingTreeNode(LoggingTreeNode.TYPE_NOFILE);
			rootNode.add(currentNode);
			
			codeViewer=viewer;
			tabbedPane=codeViewer.getTabbedPane();
			currentTab=null;
			
			openTabs = new HashSet<Component>();
			
			viewer.getTabbedPane().addChangeListener(this);
			for (RecorderPluginInterface plugin : RecorderPluginList.getPlugins()) {
				plugin.onFrameCreate(selected.getAddAttribute(KEY),codeViewer, currentNode);
			}
		}
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		HashSet<Component> nowOpenTabs = new HashSet<Component>();
		//auf neue Tabs prüfen
		for (int i=0; i<tabbedPane.getTabCount(); i++) {
			EditorPanel ed = (EditorPanel)tabbedPane.getComponentAt(i);
			nowOpenTabs.add(ed);
			if (!openTabs.contains(ed)) {
				openTabs.add(ed);
				LoggingTreeNode openedNode = new LoggingTreeNode(TYPE_OPENED);
				openedNode.setAttribute(ATTRIBUTE_PATH, ed.getFilePath());
				currentNode.add(openedNode);
			}
		}
		//auf geschlossene Tabs prüfen
		Iterator<Component> it = openTabs.iterator();
		while(it.hasNext()) {
			EditorPanel ed = (EditorPanel)it.next();
			if (!nowOpenTabs.contains(ed)) {
				it.remove();
				LoggingTreeNode closedNode = new LoggingTreeNode(TYPE_CLOSED);
				closedNode.setAttribute(ATTRIBUTE_PATH, ed.getFilePath());
				currentNode.add(closedNode);
			}
		}
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
	
	@Override
	public void onClose() {
		if(enabled) {
			LoggingTreeNode node = new LoggingTreeNode(TYPE_VIEWERCLOSED);
			currentNode.add(node);
			LoggingTreeXMLHandler.saveXMLTree(rootNode, codeViewer.getSaveDir().getPath()+System.getProperty("file.separator")+"recorder.xml");
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
