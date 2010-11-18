package plugins.questionLists;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import plugins.ExperimentPlugin;
import experimentEditor.ExperimentEditor;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentDescription;
import experimentViewer.QuestionListPanel;
import experimentViewer.ExperimentViewer;


public class QuestionListPlugin implements ExperimentPlugin {

	@Override
	public List<SettingsComponentDescription> getSettingsComponentDescriptions(
			String type) {
		return null;
	}

	@Override
	public void experimentEditorRun(ExperimentEditor experimentEditor) {
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		QuestionListPanel overview = new QuestionListPanel(experimentTree);
		overview.setPreferredSize(new Dimension(150, 2));
		contentPane.add(overview, BorderLayout.WEST);
	}

}
