package experimentView.EditorTabbedPane;

import java.awt.Component;
import java.io.File;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	boolean searchable;
	boolean editable;

	// UserDataRecorder rec;

	public EditorTabbedPane() {
		super(JTabbedPane.TOP);
		searchable = true;
		editable = false;
	}

	public EditorTabbedPane(boolean searchable, boolean editable) {
		super(JTabbedPane.TOP);
		this.searchable = searchable;
		this.editable = editable;
		 //rec = new UserDataRecorder();
		 //rec.startFileTime(this);
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
		EditorPanel myPanel = new EditorPanel(file, searchable, editable);
		add(file.getName(), myPanel);
		setSelectedIndex(indexOfComponent(myPanel));
		this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(
				this));
		myPanel.grabFocus();
		 //System.out.println(rec.getFileTime());
	}
}
