package experimentGUI.plugins;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;

import experimentGUI.PluginInterface;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.plugins.codeViewerPlugin.Recorder;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;
import experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;
import experimentGUI.util.settingsComponents.components.SettingsDirectoryPathChooser;

public class CodeViewerPlugin implements PluginInterface {
	public final static String KEY = "codeviewer";
	
	private ExperimentViewer experimentViewer;
	private int count = 1;
	
	private HashMap<QuestionTreeNode,CodeViewer> codeViewers;

	private Rectangle bounds;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "Codeviewer aktivieren", true);
			result.addSubComponent(new SettingsComponentDescription(SettingsDirectoryPathChooser.class, CodeViewer.KEY_PATH, "Pfad der Quelltexte:"));
			result.addNextComponent(Recorder.getSettingsComponentDescription());
			SettingsComponentDescription desc = CodeViewerPluginList.getSettingsComponentDescription();
			if (desc!=null) {
				result.addSubComponent(desc);
				while ((desc = desc.getNextComponentDescription()) != null) {
					result.addSubComponent(desc);
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		this.experimentViewer=experimentViewer;
		codeViewers = new HashMap<QuestionTreeNode,CodeViewer>();
	}

	@Override
	public boolean denyEnterNode(QuestionTreeNode node) {
		return false;
	}

	@Override
	public void enterNode(QuestionTreeNode node) {	
		boolean enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
		if (enabled) {
			String savePath = experimentViewer.getSaveDir().getPath()+System.getProperty("file.separator")+(count++)+"_"+node.getName()+"_codeviewer";		
			CodeViewer cv = new CodeViewer(node.getAttribute(KEY), new File(savePath));
			if (bounds==null) {
				Point location = experimentViewer.getLocation();
				cv.setLocation(new Point(location.x+20, location.y+20));
			} else {
				cv.setBounds(bounds);
			}
			cv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			cv.setVisible(true);
			codeViewers.put(node, cv);
		}
	}

	@Override
	public String denyNextNode(QuestionTreeNode currentNode) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node) {
		CodeViewer cv = codeViewers.get(node);
		if (cv!=null) {
			CodeViewerPluginList.onClose();
			cv.getRecorder().onClose();
			bounds = cv.getBounds();
			cv.dispose();
			codeViewers.remove(node);
		}
	}

	@Override
	public String finishExperiment() {
		return null;
	}
}
