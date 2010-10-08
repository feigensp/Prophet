package EditorTabbedPane;

import java.awt.Component;
import java.io.File;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	
	public EditorTabbedPane() {
		super(JTabbedPane.TOP);
	}
	
	public void openFile(File file) {
		for (int i=0; i<getTabCount(); i++) {
			Component myComp=getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel)myComp).getFile().equals(file)) {
				setSelectedIndex(i);
				((EditorPanel)myComp).grabFocus();
				return;
			}
		}
		EditorPanel myPanel = new EditorPanel(file);
		add(file.getName(), myPanel);
		setSelectedIndex(indexOfComponent(myPanel));
		myPanel.grabFocus();
	}
}
