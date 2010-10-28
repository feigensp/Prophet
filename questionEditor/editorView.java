package questionEditor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class editorView extends JFrame {

	private JTextField pathSelectionTextField;
	private JButton pathSelectionButton;
	private JButton boldButton;
	private JButton tableButton;
	private EditArea textPane;

	/**
	 * Launch the application.
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
	 * Create the frame.
	 */
	public editorView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 431);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);

		JMenuItem loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		fileMenu.add(saveMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);

		JMenu editMenu = new JMenu("Bearbeiten");
		menuBar.add(editMenu);

		JMenuItem tableMenuItem = new JMenuItem("Tabelle einf\u00FCgen");
		editMenu.add(tableMenuItem);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel optionPanel = new JPanel();
		contentPane.add(optionPanel, BorderLayout.WEST);
		optionPanel.setLayout(new BorderLayout(0, 0));

		JPanel pathSelectionPanel = new JPanel();
		optionPanel.add(pathSelectionPanel, BorderLayout.SOUTH);

		pathSelectionTextField = new JTextField();
		pathSelectionPanel.add(pathSelectionTextField);
		pathSelectionTextField.setColumns(10);

		pathSelectionButton = new JButton("Durchsuchen");
		pathSelectionPanel.add(pathSelectionButton);

		textPane = new EditArea();

		PopupTree tree = new PopupTree(new DefaultMutableTreeNode("Übersicht"), textPane);
		optionPanel.add(tree, BorderLayout.CENTER);

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

	private void setListener() {
		// Ordner einstellen
		pathSelectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					pathSelectionTextField.setText(file.getAbsolutePath());
				}
			}
		});
		// toolbar buttons
		boldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setTag("b");
			}
		});
	}

}
