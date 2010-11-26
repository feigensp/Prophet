package experimentGUI.plugins;

import java.util.List;
import java.util.Vector;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.experimentViewer.HTMLFileView;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class CodeViewerPlugin implements PluginInterface {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(
			QuestionTreeNode node) {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
			SettingsPluginComponentDescription desc = new SettingsPluginComponentDescription("codeviewer", "Codeviewer aktivieren");
			for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
				if (plugin.getSettingsComponentDescriptions()!=null) {
					for (SettingsComponentDescription pluginDescription : plugin.getSettingsComponentDescriptions()) {
						desc.addSubComponent(pluginDescription);
					}
				}
			}
			result.add(desc);
		}
		return result;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		if (Boolean.parseBoolean(node.getAttributeValue("codeviewer"))) {
			CodeViewer cv = new CodeViewer(node.getAttribute("codeviewer"));
			cv.setVisible(true);
			return cv;
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData) {
		CodeViewer cv = (CodeViewer)pluginData;
		if (cv!=null) {
			cv.dispose();
		}
	}

	@Override
	public String getKey() {
		return "codeviewer";
	}
}
