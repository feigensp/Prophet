package questionEditor;

/**
 * this class is contains the main for the editor to create and edit questions
 * furthermore it creates the view and some listeners
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CategorySettingsDialog.SettingsDialog;
import QuestionTree.QuestionTree;
import QuestionTree.QuestionTreeEvent;
import QuestionTree.QuestionTreeListener;
import QuestionTree.QuestionTreeNode;

@SuppressWarnings("serial")
public class editorView extends JFrame {
	private QuestionTree tree;
	private EditViewPanel editViewPanel;
	private SettingsDialog settingsDialog;

	private QuestionTreeNode selected;

	private JPanel contentPane;

	/**
	 * the main method to launch the application
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					editorView frame = new editorView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * the constructor, sets some basic settings and start the method to create
	 * the view
	 */
	public editorView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 431);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		
		JMenuItem newMenuItem = new JMenuItem("Neu");
		fileMenu.add(newMenuItem);
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				contentPane.removeAll();
				contentPane.updateUI();
				build(null);
			}
		});
		
		JMenuItem loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					QuestionTreeNode newRoot = XMLTreeHandler.loadXMLTree("test.xml");

					contentPane.removeAll();
					contentPane.updateUI();
					build(newRoot);
				}
			}
		});

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				XMLTreeHandler.writeXMLTree(tree.getRoot(), "test");
			}
		});

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);

		JMenu editMenu = new JMenu("Bearbeiten");
		menuBar.add(editMenu);

		JMenuItem tableMenuItem = new JMenuItem("Tabelle einf\u00FCgen");
		editMenu.add(tableMenuItem);
		
		build(null);
	}
	
	public void removeEditorElements() {
		if (editViewPanel!=null) {
			contentPane.remove(editViewPanel);
			editViewPanel=null;
		}
		if (settingsDialog!=null) {
			contentPane.remove(settingsDialog);
			settingsDialog=null;
		}
		contentPane.updateUI();
	}
	
	public void build(QuestionTreeNode root) {
		selected=null;
		
		tree = new QuestionTree(root);
		tree.setPreferredSize(new Dimension(175, 10));
		tree.addQuestionTreeListener(new QuestionTreeListener() {
			public void questionTreeEventOccured(QuestionTreeEvent e) {
				removeEditorElements();
				selected=e.getNode();
				if (e.getNode().isCategory()) {
					settingsDialog = new SettingsDialog(selected);
					contentPane.add(settingsDialog);
				} else if (e.getNode().isQuestion()) {	
					editViewPanel = new EditViewPanel(selected); 
					contentPane.add(editViewPanel);
				}
			}			
		});		
		contentPane.add(tree, BorderLayout.WEST);

		editViewPanel = new EditViewPanel(null);
		contentPane.add(editViewPanel, BorderLayout.CENTER);
	}

}
