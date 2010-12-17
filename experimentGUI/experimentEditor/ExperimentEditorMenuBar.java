package experimentGUI.experimentEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import experimentGUI.util.Language;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

@SuppressWarnings("serial")
public class ExperimentEditorMenuBar extends JMenuBar {
	private ExperimentEditor questionEditor;
	private File currentFile;

	public ExperimentEditorMenuBar(ExperimentEditor qE) {
		questionEditor = qE;
		currentFile = null;
		JMenu fileMenu = new JMenu(Language.getValue("ExperimentEditorMenu_fileMenu")/*"Datei"*/);
		add(fileMenu);

		JMenuItem newMenuItem = new JMenuItem(Language.getValue("ExperimentEditorMenu_newMenuItem")/*"Neu"*/);
		fileMenu.add(newMenuItem);
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				questionEditor.newTree();
			}
		});

		JMenuItem loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					currentFile = fc.getSelectedFile();
					QuestionTreeNode newRoot = QuestionTreeXMLHandler.loadXMLTree(currentFile
							.getAbsolutePath());
					questionEditor.loadTree(newRoot);
				}
			}
		});

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentFile != null) {
					QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
							currentFile.getAbsolutePath());
				}
			}
		});

		JMenuItem saveAsMenuItem = new JMenuItem("Speichern unter");
		fileMenu.add(saveAsMenuItem);
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						int n = JOptionPane.showConfirmDialog(null, "Wollen sie " + file.getName()
								+ " ersetzen?", "Ersetzen?", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
									file.getAbsolutePath());
						}
					} else {
						QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
								file.getAbsolutePath());
					}
				}
			}
		});

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
}
