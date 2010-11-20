package plugins.codeViewer;

import java.util.List;
import java.util.Vector;

import plugins.ExperimentPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsPathChooser;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsPluginComponent;
import experimentViewer.ExperimentViewer;
import experimentViewer.HTMLFileView;

public class ExperimentCodeViewerPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(
			QuestionTreeNode node) {
		Vector<SettingsComponentDescription> result = new Vector<SettingsComponentDescription>();
		if (node.getType().equals(QuestionTreeNode.TYPE_CATEGORY)) {
			SettingsComponentDescription desc = new SettingsComponentDescription(SettingsPluginComponent.class, "codeviewer","Codeviewer aktivieren");
			desc.addSubComponent(new SettingsComponentDescription(SettingsPathChooser.class, "path", "Pfad der Quelltexte:"));
			for (CodeViewerPlugin plugin : CodeViewerPluginList.getPlugins()) {
				for (SettingsComponentDescription pluginDescription : plugin.getSettingsComponentDescriptions()) {
					desc.addSubComponent(pluginDescription);
				}
			}
			result.add(desc);
		}
		return result;
	}

	@Override
	public void experimentEditorRun(ExperimentEditor experimentEditor) {
		// TODO Auto-generated method stub
		
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
