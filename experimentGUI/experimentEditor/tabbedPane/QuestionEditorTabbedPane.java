package experimentGUI.experimentEditor.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class QuestionEditorTabbedPane extends JPanel {
	private QuestionTreeNode selected;
	private JTabbedPane myTabbedPane;
	
	public QuestionEditorTabbedPane() {
		setLayout(new BorderLayout());
		myTabbedPane=new JTabbedPane();
		add(myTabbedPane,BorderLayout.CENTER);
		
		myTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				((ExperimentEditorTab)myTabbedPane.getSelectedComponent()).activate(selected);
			}
		});
		
		addEditorPanel("Editor", new ContentEditorPanel());
		addEditorPanel("Vorschau", new ContentViewerPanel());
		addEditorPanel("Einstellungen", new SettingsEditorPanel());
		addEditorPanel("Notizen", new NoteEditorPanel());
	}
	public void addEditorPanel(String caption, ExperimentEditorTab panel) {
		myTabbedPane.addTab(caption, null, (Component)panel, null);
	}
	public void setSelected(QuestionTreeNode selected) {
		this.selected=selected;
		((ExperimentEditorTab)myTabbedPane.getSelectedComponent()).activate(selected);
	}
}
