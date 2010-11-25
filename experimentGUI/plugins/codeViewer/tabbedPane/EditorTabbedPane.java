package experimentGUI.plugins.codeViewer.tabbedPane;

import java.awt.Component;
import java.io.File;

import javax.swing.JTabbedPane;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	QuestionTreeNode selected;
	
	public EditorTabbedPane(QuestionTreeNode selected) {
		super(JTabbedPane.TOP);
		this.selected=selected;
	}

	public void openFile(File file) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel)
					&& ((EditorPanel) myComp).getFile().equals(file)) {
				setSelectedIndex(i);
				((EditorPanel) myComp).grabFocus();
				return;
			}
		}
		EditorPanel myPanel = new EditorPanel(file, selected);
		add(file.getName(), myPanel);
		setSelectedIndex(indexOfComponent(myPanel));
		this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(
				this));
		myPanel.grabFocus();
		 //System.out.println(rec.getFileTime());
	}
}
