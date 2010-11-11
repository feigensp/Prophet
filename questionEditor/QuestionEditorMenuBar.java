package questionEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import util.QuestionTreeNode;
import util.XMLTreeHandler;

@SuppressWarnings("serial")
public class QuestionEditorMenuBar extends JMenuBar {
	private QuestionEditor questionEditor;
	
	public QuestionEditorMenuBar(QuestionEditor qE) {		
		questionEditor=qE;
		JMenu fileMenu = new JMenu("Datei");
		add(fileMenu);
		
		JMenuItem newMenuItem = new JMenuItem("Neu");
		fileMenu.add(newMenuItem);
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				questionEditor.build(null);
			}
		});
		
		JMenuItem loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					//File file = fc.getSelectedFile();
					QuestionTreeNode newRoot = XMLTreeHandler.loadXMLTree("test.xml");

					questionEditor.build(newRoot);
				}
			}
		});

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				XMLTreeHandler.writeXMLTree(questionEditor.getTree().getRoot(), "test");
			}
		});

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);

		JMenu editMenu = new JMenu("Bearbeiten");
		add(editMenu);

		JMenuItem tableMenuItem = new JMenuItem("Tabelle einf\u00FCgen");
		editMenu.add(tableMenuItem);
	}
}
