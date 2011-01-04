package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class EditabilityPlugin implements CodeViewerPluginInterface {
	public final static String KEY = "editable";
	private boolean editable;
	private EditorTabbedPane tabbedPane;
	private HashMap<EditorPanel,Boolean> changed;

	public void saveActiveFile() {
		Component activeComp = tabbedPane.getSelectedComponent();
		if (activeComp != null && activeComp instanceof EditorPanel) {
			saveEditorPanel((EditorPanel)activeComp);
		}
	}

	public void saveAllFiles() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Component myComp = tabbedPane.getComponentAt(i);
			if (myComp instanceof EditorPanel) {
				saveEditorPanel((EditorPanel)myComp);
			}
		}
	}
	
	protected void saveEditorPanel(EditorPanel editorPanel) {
			File file = new File(tabbedPane.getSaveDir().getPath()+editorPanel.getFilePath());
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
	
	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Quelltext editierbar");
	}
	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer) {
		editable = Boolean.parseBoolean(selected.getAttributeValue(KEY));
		if (editable) {
			tabbedPane = viewer.getTabbedPane();
			viewer.getViewerMenuBar().setVisible(true);
			JMenu fileMenu = viewer.getFileMenu();
			JMenuItem saveMenuItem = new JMenuItem("Speichern");
			fileMenu.add(saveMenuItem);
			saveMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveActiveFile();
				}
			});
			JMenuItem saveAllMenuItem = new JMenuItem("Alle Speichern");
			fileMenu.add(saveAllMenuItem);
			saveAllMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAllFiles();
				}
			});
			changed = new HashMap<EditorPanel,Boolean>();
		}
	}

	@Override
	public void onEditorPanelCreate(final EditorPanel editorPanel) {
		if (editable) {
			changed.put(editorPanel, false);
			editorPanel.getTextArea().setEditable(true);
			editorPanel.getTextArea().getDocument().addDocumentListener(new DocumentListener() {			
				private void changeOccured() {
					changed.put(editorPanel, true);
				}
				public void changedUpdate(DocumentEvent arg0) {
					changeOccured();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					changeOccured();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					changeOccured();
				}			
			});
		}
	}
	@Override
	public void onEditorPanelClose(EditorPanel editorPanel) {
		if (changed.get(editorPanel).booleanValue()) {
			int n = JOptionPane.showConfirmDialog(null, "Änderungen speichern?", "Speichern?",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				saveEditorPanel(editorPanel);
			}
		}
		changed.remove(editorPanel);
	}
	@Override
	public void onClose() {
	}
	@Override
	public String getKey() {
		return KEY;
	}
}
