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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;

public class editorView extends JFrame {
	private JButton boldButton;
	private JButton tableButton;
	private EditArea textPane;
	private JMenuItem saveMenuItem;
	private JMenuItem newMenuItem;
	private JMenuItem loadMenuItem;
	private PopupTree tree;
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
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		build();
	}

	/**
	 * creates the view and starts the method which adds the listener
	 */
	private void build() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);

		newMenuItem = new JMenuItem("Neu");
		fileMenu.add(newMenuItem);

		loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);

		saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);

		JMenu editMenu = new JMenu("Bearbeiten");
		menuBar.add(editMenu);

		JMenuItem tableMenuItem = new JMenuItem("Tabelle einf\u00FCgen");
		editMenu.add(tableMenuItem);

		JPanel optionPanel = new JPanel();
		optionPanel.setPreferredSize(new Dimension(175, 10));
		contentPane.add(optionPanel, BorderLayout.WEST);
		optionPanel.setLayout(new BorderLayout(0, 0));

		textPane = new EditArea();

		JTextPane viewPane = new JTextPane();
		viewPane.setEditorKit(new HTMLEditorKit());
		tree = new PopupTree(new DefaultMutableTreeNode("Übersicht"), textPane,
				viewPane);
		JScrollPane treeScrollPane = new JScrollPane(tree);
		optionPanel.add(treeScrollPane, BorderLayout.CENTER);

		JPanel editViewPanel = new JPanel();
		contentPane.add(editViewPanel, BorderLayout.CENTER);
		editViewPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane editViewTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		editViewPanel.add(editViewTabbedPane);

		JPanel editPanel = new JPanel();
		editViewTabbedPane.addTab("Editor", null, editPanel, null);
		editPanel.setLayout(new BorderLayout(0, 0));
		editPanel.add(textPane, BorderLayout.CENTER);

		JPanel viewPanel = new JPanel();
		editViewTabbedPane.addTab("Betrachter", null, viewPanel, null);
		viewPanel.setLayout(new BorderLayout(0, 0));

		viewPane.setEditable(false);
		viewPanel.add(viewPane, BorderLayout.CENTER);

		JToolBar toolBar = new JToolBar();
		editViewPanel.add(toolBar, BorderLayout.NORTH);

		boldButton = new JButton("<b>");
		toolBar.add(boldButton);
		tableButton = new JButton("<table>");
		toolBar.add(tableButton);
		MacroBox macroBox = new MacroBox(textPane);
		toolBar.add(macroBox);

		setListener();
	}

	/**
	 * method which sets the listener
	 */
	private void setListener() {
		// toolbar buttons
		boldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setTag("b");
			}
		});
		// save
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EditorData.extractHTMLContent();
				XMLTreeHandler.writeXMLTree(EditorData.getDataRoot(), "test");
			}
		});
		// new
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				EditorData.reset();
				contentPane.removeAll();
				build();
			}
		});
		// load
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DataTreeNode newRoot = null;

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// save(file.getAbsolutePath());
					newRoot = XMLTreeHandler.loadXMLTree("test.xml");
				}

				if (newRoot == null) {
					System.out.println("Datei nicht gefunden...");
				} else {
					EditorData.reset();
					contentPane.removeAll();
					build();
					EditorData.setDataRoot(newRoot);
					tree.rootUpdated();
				}
			}
		});
	}

}
