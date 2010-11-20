package experimentEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import util.QuestionTreeNode;
import util.XMLTreeHandler;

@SuppressWarnings("serial")
public class ExperimentEditorMenuBar extends JMenuBar {
	private ExperimentEditor questionEditor;
	
	public ExperimentEditorMenuBar(ExperimentEditor qE) {		
		questionEditor=qE;
		JMenu fileMenu = new JMenu("Datei");
		add(fileMenu);
		
		JMenuItem newMenuItem = new JMenuItem("Neu");
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
				QuestionTreeNode newRoot = XMLTreeHandler.loadXMLTree("test.xml");
//				JFileChooser fc = new JFileChooser();
//				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//					//File file = fc.getSelectedFile();
//
					questionEditor.loadTree(newRoot);
//				}
			}
		});

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				XMLTreeHandler.saveXMLTree(questionEditor.getTree().getRoot(), "test.xml");
			}
		});

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO: Fenster schlieﬂen
			}
		});
	}
}
