package plugins.questionLists;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import plugins.ExperimentPlugin;
import util.QuestionTreeNode;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentViewer.ExperimentViewer;
import experimentViewer.HTMLFileView;


public class QuestionListPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(
			QuestionTreeNode node) {
		return null;
	}

	@Override
	public void experimentEditorRun(ExperimentEditor experimentEditor) {
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		QuestionListPanel overview = new QuestionListPanel();
		overview.setPreferredSize(new Dimension(150, 2));
		experimentViewer.getContentPane().add(overview, BorderLayout.WEST);
		overview.addCategories(experimentViewer.getTree());
	}

	@Override
	public Object enterNode(QuestionTreeNode node, HTMLFileView htmlFileView) {
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, HTMLFileView htmlFileView, Object pluginData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKey() {
		return "questionlist";
	}

}
