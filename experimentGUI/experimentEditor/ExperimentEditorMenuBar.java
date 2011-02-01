package experimentGUI.experimentEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

/**
 * The menu bar of the ExperimentViewer. Separated to enhance readability.
 * 
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
	public final static String MENU_FILE_OPEN = "Laden";
	public final static String MENU_FILE_SAVE = "Speichern";
	public final static String MENU_FILE_SAVE_AS = "Speichern unter...";
	public final static String MENU_FILE_QUIT = "Beenden";

	public final static String MESSAGE_FILE_NOT_FOUND = "Datei nicht gefunden";
	public final static String MESSAGE_FILE_NOT_FOUND_TITLE = "Fehler";

	public final static String MESSAGE_REPLACE_FILE = " ist bereits vorhanden.\nWollen Sie sie ersetzen?";
	public final static String MESSAGE_REPLACE_FILE_TITLE = "Speichern unter bestätigen";

	/**
	 * Constructor
	 * 
	 * @param qE
	 *            The ExperimentEditor object this menu bar is added to
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
				currentFile = null;
				questionEditor.setTitle(questionEditor.TITLE);
				questionEditor.newTree();
			}
		});

		JMenuItem loadMenuItem = new JMenuItem(MENU_FILE_OPEN);
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(currentFile == null ? new File(".") : currentFile);
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					currentFile = fc.getSelectedFile();
					QuestionTreeNode newRoot;
					try {
						newRoot = QuestionTreeXMLHandler.loadXMLTree(currentFile.getAbsolutePath());
						questionEditor.loadTree(newRoot);
						questionEditor.setTitle(ExperimentEditor.TITLE + " - "
								+ currentFile.getAbsolutePath());
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(questionEditor, MESSAGE_FILE_NOT_FOUND,
								MESSAGE_FILE_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					}
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
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		fileMenu.add(saveAsMenuItem);
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(currentFile);
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						int n = JOptionPane.showConfirmDialog(null, file.getName() + MESSAGE_REPLACE_FILE,
								MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							currentFile = file;
							QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
									file.getAbsolutePath());
							questionEditor.setTitle(ExperimentEditor.TITLE + " - "
									+ currentFile.getAbsolutePath());
						}
					} else {
						currentFile = file;
						QuestionTreeXMLHandler.saveXMLTree(questionEditor.getTree().getRoot(),
								file.getAbsolutePath());
						questionEditor.setTitle(ExperimentEditor.TITLE + " - "
								+ currentFile.getAbsolutePath());
					}
				}
			}
		});

		fileMenu.addSeparator();

		JMenu exportMenu = new JMenu("Exportieren");
		fileMenu.add(exportMenu);

		JMenuItem exportHTMLMenuItem = new JMenuItem("HTML");
		exportMenu.add(exportHTMLMenuItem);
		exportHTMLMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(currentFile);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						int n = JOptionPane.showConfirmDialog(null, file.getName() + MESSAGE_REPLACE_FILE,
								MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							file.mkdir();
							saveAsHTML(file, questionEditor.getTree().getRoot());
						}
					} else {
						file.mkdir();
						saveAsHTML(file, questionEditor.getTree().getRoot());
					}
				}
			}
		});

		fileMenu.addSeparator();

		JMenuItem closeMenuItem = new JMenuItem(MENU_FILE_QUIT);
		fileMenu.add(closeMenuItem);
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	private void saveAsHTML(File file, QuestionTreeNode node) {
		String nodeName = node.getName();
		String nodeValue = node.getValue();
		if (nodeName != null && nodeValue != null) {
			writeFile(file.getAbsolutePath(),nodeName, nodeValue);
		}
		if(node.isCategory()) {
			file = getFreeFile(new File(file.getAbsolutePath() + System.getProperty("file.separator") + nodeName));
			file.mkdir();
			System.out.println(file.getAbsolutePath());
		}
		for(int i=0; i<node.getChildCount(); i++) {
			saveAsHTML(file, (QuestionTreeNode) node.getChildAt(i));
		}
	}

	private void writeFile(String path, String name, String content) {
		FileWriter fw;
		BufferedWriter bw;
		String newline = System.getProperty("line.separator");
		try {
			File f = getFreeFile(new File(path + System.getProperty("file.separator") +  name + ".html"));
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
		    bw.write("<HTML>"+newline); 
			bw.write("<HEAD>"+newline); 
		    bw.write("<TITLE>"+newline); 
		    bw.write(name+newline); 
		    bw.write("</TITLE>"+newline); 
		    bw.write("</HEAD>"+newline); 
		    bw.write("<BODY>"+newline); 
		    bw.write(content+newline); 
		    bw.write("</BODY>"+newline);
		    bw.write("</HTML>"+newline); 
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private File getFreeFile(File f) {
		while(f.exists()) {
			f = new File(f.getAbsoluteFile() + "_");
		}
		return f;
	}
}
