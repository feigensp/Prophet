/**
 * Main Klasse für das Programm zur Erstellung neuer Fragebögen
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuestionCreator extends JFrame {

	private static JFileChooser fc = new JFileChooser();

	private JList overviewList;
	private DefaultListModel listModel;
	private JTextField newTextField;
	private JButton removeButton;
	private JPanel creatorOverviewPanel;
	private CardLayout cardLayout;
	private ArrayList<ExtendedPanel> questions;
	private JButton newButton;
	private JTextArea creatorMenuTextArea;
	private JComboBox creatorMenuComboBox;
	private JButton creatorMenuAdd;
	private JButton saveButton;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem closeMenuItem;
	private JMenuItem newMenuItem;

	/**
	 * Startet die Applikation
	 * 
	 * @param args
	 *            werden nciht genutzt
	 */
	public static void main(String[] args) {
		// Look and Feel setzen
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionCreator frame = new QuestionCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * GUI-Initialisierung aufrufen und Listener etc. setzen
	 */
	public QuestionCreator() {
		initGUI();
		createListener();
		// "Startfrage" generieren
		addQuestion(0, 0, "Frage1");
		overviewList.setSelectedIndex(0);
	}

	/**
	 * Erstellt die Listener für die einzelnen Objekte
	 */
	private void createListener() {
		// Listener für das Neu-Starten
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restart();
				addQuestion(0, 0, "Frage1");
				overviewList.setSelectedIndex(0);
			}
		});
		// Listener für Änderungen in der JList
		overviewList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					if (overviewList.getSelectedIndex() == -1) {
						removeButton.setEnabled(false);
					} else {
						removeButton.setEnabled(true);
						cardLayout.show(creatorOverviewPanel, ""
								+ questions
										.get(overviewList.getSelectedIndex())
										.getId());
					}
				}
			}
		});
		// Eine neue Frage erstellen
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String text = newTextField.getText();
				int index = overviewList.getSelectedIndex() == -1 ? listModel
						.size() : overviewList.getSelectedIndex() + 1;
				addQuestion(ExtendedPanel.nextFreeId(), index, text);
				newTextField.selectAll();
				newTextField.requestFocusInWindow();
			}
		});
		newTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = newTextField.getText();
					int index = overviewList.getSelectedIndex() == -1 ? listModel
							.size() : overviewList.getSelectedIndex() + 1;
					addQuestion(ExtendedPanel.nextFreeId(), index, text);
					newTextField.selectAll();
					newTextField.requestFocusInWindow();
				}
			}
		});
		// Eine bestehende Frage löschen
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = overviewList.getSelectedIndex();
				removeQuestion(questions.get(index).getId(), index);
				overviewList.requestFocusInWindow();
			}
		});
		// Neues Frageelement hinzufügen
		creatorMenuAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int selection = creatorMenuComboBox.getSelectedIndex();
				String text = creatorMenuTextArea.getText();
				questions.get(overviewList.getSelectedIndex()).addComponent(
						text, selection);
				creatorMenuTextArea.setText("");
				creatorMenuTextArea.grabFocus();
			}
		});
		creatorMenuTextArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					int selection = creatorMenuComboBox.getSelectedIndex();
					String text = creatorMenuTextArea.getText();
					questions.get(overviewList.getSelectedIndex())
							.addComponent(text, selection);
					creatorMenuTextArea.setText("");
					creatorMenuTextArea.grabFocus();
				}
			}
		});
		// Fragebogen als XML Dokument exportieren
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// save(file.getAbsolutePath());
					XMLHandler.writeXMLTree(createTree(), file.getName());
				}
			}
		});
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// save(file.getAbsolutePath());
					XMLHandler.writeXMLTree(createTree(), file.getName());
				}
			}
		});
		// Fragebogen aus XML importieren
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// fc = new JFileChooser();
				// int returnVal = fc.showSaveDialog(null);
				// if (returnVal == JFileChooser.APPROVE_OPTION) {
				// File file = fc.getSelectedFile();
				// XMLHandler.loadXMLTree(file.getName());
				// }
				TreeNode root = XMLHandler.loadXMLTree("test.xml");
				loadTree(root);
			}
		});
		// Titeländerungslistener
		overviewList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					String str = JOptionPane
							.showInputDialog("Titel eingeben: ");
					if (str != null && !str.equals("") && !str.contains(" ")) {
						int index = overviewList.getSelectedIndex();
						overviewList.getSelectedIndex();
						listModel.set(index, str);
						questions.get(index).setHeadline(str);
					}
				}
			}
		});
		// Programm beenden
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	/**
	 * setzt alle relevanten Variablen zurück um einen neuen "Fragebogen"
	 * beginnen zu können
	 */
	private void restart() {
		listModel.clear();
		questions.clear();
		ExtendedPanel.removeIds();
	}

	/**
	 * Exportiert Fragebogen in Baumstrukturform
	 */
	private TreeNode createTree() {
		// Wurzel erstellen
		TreeNode root = new TreeNode();
		for (int i = 0; i < questions.size(); i++) {
			// Fragen hinzufügen - dazu Attributliste erstellen
			Vector<ElementAttribute> questionAttributes = new Vector<ElementAttribute>();
			questionAttributes.add(new ElementAttribute<String>("text",
					((String) listModel.get(i))));
			TreeNode question = new TreeNode(root, questionAttributes);
			root.addChild(question);
			// Elemente der Fragen hinzufügen
			LinkedList<QuestionElement> elements = questions.get(i)
					.getElements();
			for (QuestionElement ele : elements) {
				// Vector für die Attribute der Komponenten
				Vector<ElementAttribute> componentAttributes = new Vector<ElementAttribute>();
				componentAttributes.add(new ElementAttribute<String>("text",
						ele.getText()));
				if (!ele.getAnswer().equals("")) {
					componentAttributes.add(new ElementAttribute<String>(
							"answer", ele.getAnswer()));
				}
				componentAttributes.add(new ElementAttribute<Integer>("model",
						ele.getSelection()));
				componentAttributes.add(new ElementAttribute<Integer>("x",
						(int) ele.getElementSize().getWidth()));
				componentAttributes.add(new ElementAttribute<Integer>("y",
						(int) ele.getElementSize().getHeight()));
				TreeNode element = new TreeNode(question, componentAttributes);
				question.addChild(element);
			}
		}
		return root;
	}

	/**
	 * Importiert Fragebogen aus Baumstrukturform
	 * 
	 * @param root
	 *            Wurzelelement des Baumes
	 */
	private void loadTree(TreeNode root) {
		// Alles Leeren
		restart();

		Vector<TreeNode> treeQuestions = root.getChildren();
		int i = 0;
		// Seiten hinzufügen
		for (TreeNode treeQuestion : treeQuestions) {
			// Attribute der Fragen
			Vector<ElementAttribute> questionAttributes = treeQuestion
					.getAttributes();
			String questionText = "default";
			for (ElementAttribute questionAttribute : questionAttributes) {
				// Überschrifts-Attribut
				if (questionAttribute.getName().equals("text")) {
					questionText = questionAttribute.getContent().toString();
				}
			}
			addQuestion(ExtendedPanel.nextFreeId(), listModel.size(),
					questionText);
			// Komponenten hinzufügen
			Vector<TreeNode> components = treeQuestion.getChildren();
			for (TreeNode treeComponent : components) {
				// Komponent-Attribute
				Vector<ElementAttribute> componentAttributes = treeComponent
						.getAttributes();
				String componentText = "default";
				String componentAnswer = "";
				int componentModel = 0;
				int componentWidth = 75;
				int componentHeight = 25;
				for (ElementAttribute componentAttribute : componentAttributes) {
					// Model-Attribute
					if (componentAttribute.getName().equals("model")) {
						componentModel = Integer.parseInt(componentAttribute
								.getContent().toString());
					} else
					// Text-Attribut
					if (componentAttribute.getName().equals("text")) {
						componentText = componentAttribute.getContent()
								.toString();
					} else
					// Antwort-Attribut
					if (componentAttribute.getName().equals("answer")) {
						componentAnswer = componentAttribute.getContent()
								.toString();
					} else
					// Breite-Attribut
					if (componentAttribute.getName().equals("x")) {
						componentWidth = Integer.parseInt(componentAttribute
								.getContent().toString());
					} else
					// Höhe-Attribut
					if (componentAttribute.getName().equals("y")) {
						componentHeight = Integer.parseInt(componentAttribute
								.getContent().toString());
					}
				}
				if (!componentAnswer.equals("")) {
					switch (componentModel) {
					case QuestionElement.TEXTFIELD:
						break;
					case QuestionElement.COMBOBOX:
						break;
					case QuestionElement.CHECKBOX:
						break;
					case QuestionElement.RADIOBUTTON:
						break;
					}
				} //else {
					questions.get(i).addComponent(componentText,
							componentModel, true,
							new Dimension(componentWidth, componentHeight),
							true);
				//}
			}
			i++;
		}
		overviewList.setSelectedIndex(0);
	}

	/**
	 * Methode um eine neue Frage hinzuzufügen (in der Frage-Liste hinten
	 * drangehongen) Fügt element in der Frageliste (questions), der Liste
	 * (listModel) und dem cardLayout ein
	 * 
	 * @param cardIndex
	 *            index im Cardlayout (nicht veränderbar)
	 * @param name
	 *            Darstellungsname in der JList sowie erste Überschrift
	 */
	private void addQuestion(int cardIndex, int listIndex, String name) {
		ExtendedPanel p = new ExtendedPanel(cardIndex, name);
		questions.add(listIndex, p);
		creatorOverviewPanel.add(p, "" + cardIndex);
		// Panel in das CardLayout einfügen
		cardLayout.addLayoutComponent(p, "" + cardIndex);
		// In die Liste einfügen - und markieren
		listModel.insertElementAt(name, listIndex);
		overviewList.ensureIndexIsVisible(listIndex);
		overviewList.setSelectedIndex(listIndex);
		newTextField.setText("");
	}

	/**
	 * Methode um eine Frage zu löschen
	 * 
	 * @param listIndex
	 *            Index den das Element in der Frage-Liste hat
	 */
	private void removeQuestion(int cardIndex, int listIndex) {
		// Id entfernen
		questions.get(listIndex).removeId();
		// vom CardLayout löschen
		cardLayout.removeLayoutComponent(questions.get(listIndex));
		// aus der ExtendedPanel-Liste löschen
		questions.remove(listIndex);
		// Aus der Liste die angezeigt wird löschen
		overviewList.setSelectedIndex(listIndex - 1);
		overviewList.ensureIndexIsVisible(listIndex - 1);
		listModel.remove(listIndex);
	}

	/**
	 * GUI-Elemente initialisieren und Layout aufbauen
	 */
	private void initGUI() {
		questions = new ArrayList<ExtendedPanel>();
		listModel = new DefaultListModel();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 458);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);

		newMenuItem = new JMenuItem("Neu");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		fileMenu.add(newMenuItem);

		openMenuItem = new JMenuItem("\u00D6ffnen");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		fileMenu.add(openMenuItem);

		saveMenuItem = new JMenuItem("Speichern");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		fileMenu.add(saveMenuItem);

		closeMenuItem = new JMenuItem("Beenden");
		closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				InputEvent.ALT_MASK));
		fileMenu.add(closeMenuItem);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		// Linke Seite
		JPanel overviewPanel = new JPanel();
		contentPane.add(overviewPanel, BorderLayout.WEST);
		overviewPanel.setPreferredSize(new Dimension(175, 10));
		overviewPanel.setMinimumSize(new Dimension(175, 10));
		overviewPanel.setLayout(new BorderLayout(0, 0));

		overviewList = new JList();
		overviewPanel.add(overviewList, BorderLayout.CENTER);
		overviewList.setModel(listModel);
		overviewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel overviewSouth = new JPanel();
		overviewSouth.setMinimumSize(new Dimension(10, 110));
		overviewSouth.setPreferredSize(new Dimension(10, 110));
		overviewPanel.add(overviewSouth, BorderLayout.SOUTH);
		overviewSouth.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel overviewTextPanel = new JPanel();
		overviewSouth.add(overviewTextPanel);

		newTextField = new JTextField();
		newTextField.setColumns(10);
		// newTextField.setDocument(new TextFieldDoc());
		overviewTextPanel.add(newTextField);

		JPanel overviewActionPanel = new JPanel();
		overviewSouth.add(overviewActionPanel);

		newButton = new JButton("Neu");
		overviewActionPanel.add(newButton);

		removeButton = new JButton("Entfernen");
		removeButton.setEnabled(false);
		overviewActionPanel.add(removeButton);

		JPanel exportPanel = new JPanel();
		overviewSouth.add(exportPanel);

		saveButton = new JButton("Speichern");
		exportPanel.add(saveButton);

		// rechte Seite
		JPanel creatorPanel = new JPanel();
		contentPane.add(creatorPanel, BorderLayout.CENTER);
		creatorPanel.setLayout(new BorderLayout(0, 0));

		creatorOverviewPanel = new JPanel();
		creatorPanel.add(creatorOverviewPanel, BorderLayout.CENTER);
		cardLayout = new CardLayout();
		creatorOverviewPanel.setLayout(cardLayout);

		JPanel creatorMenuPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) creatorMenuPanel.getLayout();
		flowLayout.setVgap(10);
		creatorPanel.add(creatorMenuPanel, BorderLayout.SOUTH);
		creatorMenuPanel.setMinimumSize(new Dimension(10, 110));
		creatorMenuPanel.setPreferredSize(new Dimension(10, 110));

		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setPreferredSize(new Dimension(250, 85));
		textAreaPanel.setMinimumSize(new Dimension(250, 85));
		textAreaPanel.setLayout(new BorderLayout());
		creatorMenuTextArea = new JTextArea();
		textAreaPanel.add(new JScrollPane(creatorMenuTextArea),
				BorderLayout.CENTER);
		creatorMenuPanel.add(textAreaPanel);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(100, 60));
		panel.setMinimumSize(new Dimension(100, 60));
		creatorMenuPanel.add(panel);
		panel.setLayout(new GridLayout(2, 1, 0, 10));

		creatorMenuComboBox = new JComboBox();
		creatorMenuComboBox.setModel(new DefaultComboBoxModel(new String[] {
				"JLabel", "JTextField", "JTextArea", "JComboBox", "JCheckBox",
				"JRadioButton" }));
		panel.add(creatorMenuComboBox);

		creatorMenuAdd = new JButton("Hinzuf\u00FCgen");
		panel.add(creatorMenuAdd);
	}
}
