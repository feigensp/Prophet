package experimentGUI.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsPathChooser;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class CodeViewerPlugin implements PluginInterface {
	public final static String KEY = "codeviewer";
	ExperimentViewer experimentViewer;
	int count = 1;

	private static Logger logger;
	private Handler fileHandler;
	private Formatter formatter;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Codeviewer aktivieren");
			result.addSubComponent(new SettingsComponentDescription(SettingsPathChooser.class, CodeViewer.KEY_PATH, "Pfad der Quelltexte:"));
			for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
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
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		this.experimentViewer=experimentViewer;
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		//logger 
		try {
			logger = Logger.getLogger("experimentGUI.experimentViewer.ExperimentViewer");
			fileHandler = new FileHandler("experimentGUI.experimentViewer.ExperimentViewer.txt");
			formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.addHandler(fileHandler);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		
		logger.info("enter \"enterNode()\"");
		boolean enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
		logger.info("enabled: " + enabled);
		if (enabled) {
			String savePath = experimentViewer.getSaveDir().getPath()+System.getProperty("file.separator")+(count++)+"_"+node.getName()+"_codeviewer";		
			CodeViewer cv = new CodeViewer(node.getAttribute(KEY), new File(savePath));
			cv.setLocationRelativeTo(experimentViewer);
			cv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			cv.setVisible(true);
			experimentViewer.setVisible(true);
			for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
				logger.info("plugin: " + plugin.toString());
				plugin.onFrameCreate(node.getAttribute(KEY), cv);
			}
			logger.info("exit \"enterNode()\" --> normal");
			return cv;
		}
		logger.info("exit \"enterNode()\" --> enabled == false");
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		CodeViewer cv = (CodeViewer)pluginData;
		if (cv!=null) {
			for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
				plugin.onClose();
			}
			cv.dispose();
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		return null;
	}
}
