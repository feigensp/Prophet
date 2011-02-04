package experimentGUI.plugins.codeViewerPlugin.tabbedPane;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	private QuestionTreeNode selected;
	private File showDir, saveDir;

	public EditorTabbedPane(QuestionTreeNode selected, File showDir, File saveDir) {
		super(JTabbedPane.TOP);
		this.selected = selected;
		this.showDir = showDir;
		this.saveDir = saveDir;
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void openFile(String path) {
		if (!path.startsWith(System.getProperty("file.separator"))) {
			path = System.getProperty("file.separator") + path;
		}
		EditorPanel e = getEditorPanel(path);
		if (e != null) {
			this.setSelectedComponent(e);
			e.grabFocus();
			return;
		}
		File file = new File(saveDir.getPath() + path);
		if (!file.exists()) {
			file = new File(showDir.getPath() + path);
		}
		if (file.exists()) {
			EditorPanel myPanel = new EditorPanel(file, path, selected);
			add(file.getName(), myPanel);
			setSelectedIndex(indexOfComponent(myPanel));
			this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(this, myPanel));
			myPanel.grabFocus();
		} else {
			JOptionPane.showMessageDialog(this, "Datei " + path
					+ " konnte nicht automatisch geöffnet werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void closeFile(String path) {
		closeEditorPanel(getEditorPanel(path));
	}

	public void closeEditorPanel(EditorPanel editorPanel) {
		if (editorPanel.isChanged()) {
			int n = JOptionPane.showConfirmDialog(null, "Änderungen speichern?", "Speichern?",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				saveEditorPanel(editorPanel);
			}
		}
		for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onEditorPanelClose(editorPanel);
		}
		this.remove(editorPanel);
	}

	public EditorPanel getEditorPanel(String path) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFilePath().equals(path)) {
				return (EditorPanel) myComp;
			}
		}
		return null;
	}

	public File getShowDir() {
		return showDir;
	}

	public File getSaveDir() {
		return saveDir;
	}

	public void saveActiveFile() {
		Component activeComp = getSelectedComponent();
		if (activeComp != null && activeComp instanceof EditorPanel) {
			saveEditorPanel((EditorPanel) activeComp);
		}
	}

	public void saveAllFiles() {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if (myComp instanceof EditorPanel) {
				saveEditorPanel((EditorPanel) myComp);
			}
		}
	}

	protected void saveEditorPanel(EditorPanel editorPanel) {
		File file = new File(getSaveDir().getPath() + editorPanel.getFilePath());
		FileWriter fileWriter = null;
		try {
			file.getParentFile().mkdirs();
			fileWriter = new FileWriter(file);
			fileWriter.write(editorPanel.getTextArea().getText());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
