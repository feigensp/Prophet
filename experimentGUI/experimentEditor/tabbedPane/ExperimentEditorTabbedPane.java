package experimentGUI.experimentEditor.tabbedPane;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import experimentGUI.experimentEditor.tabbedPane.editorTabs2.ContentEditorPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs2.ContentViewerPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs2.NoteEditorPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs2.SettingsEditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class ExperimentEditorTabbedPane extends JTabbedPane {
	private QuestionTreeNode selected;
	
	public ExperimentEditorTabbedPane() {
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				activate();
			}
		});
		
		addEditorPanel("Editor", new ContentEditorPanel());
		addEditorPanel("Vorschau", new ContentViewerPanel());
		addEditorPanel("Einstellungen", new SettingsEditorPanel());
		addEditorPanel("Notizen", new NoteEditorPanel());
	}

	public void addEditorPanel(String caption, ExperimentEditorTab panel) {
		addTab(caption, null, panel, null);
	}
	public void setSelected(QuestionTreeNode selected) {
		this.selected=selected;
		activate();
	}
	private void activate() {
		((ExperimentEditorTab)getSelectedComponent()).activate(selected);
	}
	public static void recursiveSetOpaque(JComponent component) {
		component.setOpaque(false);
		for (Component child : component.getComponents()) {
			if (child instanceof JComponent) {
				recursiveSetOpaque((JComponent)child);
			}
		}
	}
}
