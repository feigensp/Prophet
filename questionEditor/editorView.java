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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import QuestionTree.QuestionNode;
import QuestionTree.QuestionTree;
import QuestionTree.QuestionTreeEvent;
import QuestionTree.QuestionTreeListener;
import QuestionTree.QuestionTreeNode;

public class editorView extends JFrame {
	private JButton boldButton;
	private JButton tableButton;
	private EditArea textPane;
	private JTextPane viewPane;
	private JMenuItem saveMenuItem;
	private JMenuItem newMenuItem;
	private JMenuItem loadMenuItem;
	private QuestionTree tree;
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
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		build();
	}
	
	public void build() {
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

		viewPane = new JTextPane();
		viewPane.setEditorKit(new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									protected void submitData(String data) {
										System.out.println("Test");
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});
		
		textPane = new EditArea();
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			
			public void myChange() {
				if (selected!=null) {
					if (selected.isQuestion()) {
						((QuestionNode)selected).setContent(textPane.getText());
						viewPane.setText(EditorData.HTMLSTART + textPane.getText()
								+ EditorData.HTMLEND);
					}
				}
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				myChange();
			}
		});
		
		tree = new QuestionTree();
		tree.addQuestionTreeListener(new QuestionTreeListener() {
			public void questionTreeEventOccured(QuestionTreeEvent e) {
				selected=null;
				if (e.getID()==QuestionTreeEvent.CATEGORY_OPENED) {
					textPane.setEditable(false);
					textPane.setText("");
					viewPane.setText("");
					textPane.setText("Category");
				} else if (e.getID()==QuestionTreeEvent.QUESTION_OPENED) {
					QuestionNode myNode = (QuestionNode)e.getNode();
					textPane.setText(myNode.getContent().replaceAll("\r\n", "\n"));
					viewPane.setText(EditorData.HTMLSTART + textPane.getText()+EditorData.HTMLEND);
					textPane.setEditable(true);
				}
				selected=e.getNode();
			}			
		});
		
		optionPanel.add(tree, BorderLayout.CENTER);

		JPanel editViewPanel = new JPanel();
		contentPane.add(editViewPanel, BorderLayout.CENTER);
		editViewPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane editViewTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		editViewPanel.add(editViewTabbedPane);

		JPanel editPanel = new JPanel();
		editViewTabbedPane.addTab("Editor", null, editPanel, null);
		editPanel.setLayout(new BorderLayout(0, 0));
		editPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);

		JPanel viewPanel = new JPanel();
		editViewTabbedPane.addTab("Betrachter", null, viewPanel, null);
		viewPanel.setLayout(new BorderLayout(0, 0));

		viewPane.setEditable(false);
		viewPanel.add(new JScrollPane(viewPane), BorderLayout.CENTER);

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
				XMLTreeHandler.writeXMLTree(tree.getRoot(), "test");
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
				QuestionTreeNode newRoot = null;

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// save(file.getAbsolutePath());
					newRoot = XMLTreeHandler.loadXMLTree("test.xml");
					tree.setRoot(newRoot);
				}

//				if (newRoot == null) {
//					System.out.println("Datei nicht gefunden...");
//				} else {
//					contentPane.removeAll();
//					build();
//				}
			}
		});
		// textPane
		textPane.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					textPane.setText(textPane.getText() + "<br>");
				}
			}
		});
	}

}
