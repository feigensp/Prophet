package experimentGUI.plugins.codeViewerPlugin.tabbedPane;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTabbedPane;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	private QuestionTreeNode selected;

	public EditorTabbedPane(QuestionTreeNode selected) {
		super(JTabbedPane.TOP);
		this.selected = selected;
	}

	public void openFile(File file) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFile().equals(file)) {
				setSelectedIndex(i);
				((EditorPanel) myComp).grabFocus();
				return;
			}
		}
		EditorPanel myPanel = new EditorPanel(file, selected);
		add(file.getName(), myPanel);
		setSelectedIndex(indexOfComponent(myPanel));
		this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(this));
		myPanel.grabFocus();
	}

	public void saveActiveFile(String path) {
		Component activeComp = this.getSelectedComponent();
		if (activeComp != null && activeComp instanceof EditorPanel) {
			saveEditorPanel(path, (EditorPanel)activeComp);
		}
	}

	public void saveAllFiles(String path) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if (myComp instanceof EditorPanel) {
				saveEditorPanel(path, (EditorPanel)myComp);
			}
		}
	}
	
	private void saveEditorPanel(String path, EditorPanel editorPanel) {
			String compPath = editorPanel.getFile().getPath();
			FileWriter fileWriter = null;
			if (System.getProperty("file.separator").equals("/")) {
				if (!path.endsWith("/")) {
					compPath = "/" + compPath;
				}
			} else {
				if (!path.endsWith("\\")) {
					compPath = "\\" + compPath;
				}
			}
			try {
				createDir(new File(path + compPath).getParent());
				fileWriter = new FileWriter(path + compPath);
				fileWriter.write(editorPanel.getTextArea().getText());
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	
	private boolean createDir(String path) {
		if(path != null) {
			if(!new File(path).exists()) {
				createDir(new File(path).getParent());
			}
			return new File(path).mkdir();
		}
		return false;
	}

	public void removeFile(File file) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFile().equals(file)) {
				this.remove(i);
				return;
			}
		}
	}

	public EditorPanel getEditorPanel(File file) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFile().equals(file)) {
				return (EditorPanel) myComp;
			}
		}
		return null;
	}
}
