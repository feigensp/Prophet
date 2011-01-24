package experimentGUI.experimentEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * The menu bar of the ExperimentViewer. Separated to enhance readability. 
 * @author Andreas Hasselberg
 * @author Markus Köppen
 *
 */
public class ExperimentEditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private ExperimentEditor questionEditor;
	private File currentFile;
	private JMenuItem saveAsMenuItem;
	
	public final static String MENU_FILE = "Datei";
	public final static String MENU_FILE_NEW = "Neu";
	public final static String MENU_FILE_LOAD = "Laden";
	public final static String MENU_FILE_SAVE = "Speichern";
	public final static String MENU_FILE_SAVE_AS = "Speichern unter...";
	public final static String MENU_FILE_QUIT = "Beenden";
	
	public final static String MESSAGE_FILE_NOT_FOUND = "Datei nicht gefunden";
	public final static String MESSAGE_FILE_NOT_FOUND_TITLE = "Fehler";
	
	public final static String MESSAGE_REPLACE_FILE = " ist bereits vorhanden.\nWollen Sie sie ersetzen?";
	public final static String MESSAGE_REPLACE_FILE_TITLE = "Speichern unter bestätigen";
	

	/**
	 * Constructor
	 * @param qE
	 * 	The ExperimentEditor object this menu bar is added to
	 */
	public ExperimentEditorMenuBar(ExperimentEditor qE) {
		questionEditor = qE;
		currentFile = null;
		JMenu fileMenu = new JMenu(MENU_FILE);
		add(fileMenu);

		JMenuItem newMenuItem = new JMenuItem(MENU_FILE_NEW);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileMenu.add(newMenuItem);
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				questionEditor.newTree();
			}
		});

		JMenuItem loadMenuItem = new JMenuItem(MENU_FILE_LOAD);
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					currentFile = fc.getSelectedFile();
					QuestionTreeNode newRoot;
					try {
						newRoot = QuestionTreeXMLHandler.loadXMLTree(currentFile
								.getAbsolutePath());
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(questionEditor, MESSAGE_FILE_NOT_FOUND, MESSAGE_FILE_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					}
					questionEditor.loadTree(newRoot);
				}
			}
		});

		JMenuItem saveMenuItem = new JMenuItem(MENU_FILE_SAVE);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentFile != null) {
					QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
							currentFile.getAbsolutePath());
				} else {
					saveAsMenuItem.doClick();
				}
			}
		});

		saveAsMenuItem = new JMenuItem(MENU_FILE_SAVE_AS);
		fileMenu.add(saveAsMenuItem);
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(currentFile);
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						int n = JOptionPane.showConfirmDialog(null, file.getName()+MESSAGE_REPLACE_FILE, MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							currentFile = file;
							QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
									file.getAbsolutePath());
						}
					} else {
						currentFile = file;
						QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
								file.getAbsolutePath());
					}
				}
			}
		});

		JMenuItem closeMenuItem = new JMenuItem(MENU_FILE_QUIT);
		fileMenu.add(closeMenuItem);
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
}
