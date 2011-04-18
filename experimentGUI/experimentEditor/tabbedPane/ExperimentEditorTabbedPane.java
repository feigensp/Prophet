package experimentGUI.experimentEditor.tabbedPane;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentViewerPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs.NoteEditorPanel;
import experimentGUI.experimentEditor.tabbedPane.editorTabs.SettingsEditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class ExperimentEditorTabbedPane extends JTabbedPane {
	private QuestionTreeNode selected;
	private ExperimentEditorTab currentTab;
	
	public ExperimentEditorTabbedPane() {
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (currentTab!=null) {
					save();
				}
				activate();
				currentTab = (ExperimentEditorTab)getSelectedComponent();
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
		currentTab.save();
		this.selected=selected;
		activate();
	}
	private void activate() {
		((ExperimentEditorTab)getSelectedComponent()).activate(selected);
	}
	public void save() {
		currentTab.save();
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
