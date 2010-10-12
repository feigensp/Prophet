package experimentQuestionCreator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QuestionCreator extends JFrame {

	private JList overviewList;
	private DefaultListModel listModel;
	private JTextField newTextField;
	private JButton removeButton;
	private JPanel creatorOverviewPanel;
	private CardLayout cardLayout;
	private ArrayList<ExtendedPanel> questions;
	private JButton newButton;
	private JPanel contentPane;
	private JPanel overviewPanel;
	private JPanel overviewSouth;
	private JPanel overviewTextPanel;
	private JPanel overviewActionPanel;
	private JPanel creatorPanel;
	private JPanel creatorMenuPanel;
	private JTextArea creatorMenuTextArea;
	private JPanel panel;
	private JComboBox creatorMenuComboBox;
	private JButton creatorMenuAdd;
	private JPanel exportPanel;
	private JButton exportButton;
	private JPanel textAreaPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

		// "Startfrage" generieren
		addQuestion(0, 0, "Frage 1");
		overviewList.setSelectedIndex(0);

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
				// Einzigartigen Namen erzwingen (auch wenn nicht wirklich
				// nötig)
				if (text.equals("") || listModel.contains(text)) {
					Toolkit.getDefaultToolkit().beep();
					newTextField.requestFocusInWindow();
					newTextField.selectAll();
				} else {
					// ansonsten hinter selektiertem Element einfügen, oder am
					// Ende
					int index = overviewList.getSelectedIndex() == -1 ? listModel
							.size() : overviewList.getSelectedIndex() + 1;
					addQuestion(ExtendedPanel.nextFreeId(), index, text);
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
			}
		});

		// Fragebogen als XML Dokument exportieren
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document questionnaire = null;
				try {
					//Dokument erstellen
					questionnaire = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder().newDocument();
					//Wurzelknoten
					Element root = questionnaire.createElement("questionnaire");
					questionnaire.appendChild(root);
					// Alle Fragen hinzufügen
					for (int i = 0; i < questions.size(); i++) {
						Element question = questionnaire
								.createElement(((String) listModel.get(i)).replace(" ", ""));
						root.appendChild(question);
						//Elemente der Fragen hinzufügen
						LinkedList<QuestionElement> elements = questions.get(i)
								.getElements();
						for (QuestionElement ele : elements) {
							Element component = questionnaire
									.createElement("component");
							question.appendChild(component);
							component.setAttribute("model",
									ele.getSelectionString());
							component.setAttribute("text", ele.getText().replace("\n", "<br>"));
						}
					}
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				}
				try {
					if (questionnaire != null) {
						TransformerFactory
								.newInstance()
								.newTransformer()
								.transform(new DOMSource(questionnaire),
										new StreamResult("Fragebogen.xml"));
					}
				} catch (TransformerConfigurationException e1) {
					e1.printStackTrace();
				} catch (TransformerException e1) {
					e1.printStackTrace();
				} catch (TransformerFactoryConfigurationError e1) {
					e1.printStackTrace();
				}
			}
		});
		
		newTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				newTextField.setText(newTextField.getText().replace(" ", ""));
				
			}
			public void keyReleased(KeyEvent e) {
				//if(e.getKeyChar() == ' ') {
					newTextField.setText(newTextField.getText().replace(" ", ""));
				//}
			}
		});
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

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		// Linke Seite
		overviewPanel = new JPanel();
		contentPane.add(overviewPanel, BorderLayout.WEST);
		overviewPanel.setPreferredSize(new Dimension(175, 10));
		overviewPanel.setMinimumSize(new Dimension(175, 10));
		overviewPanel.setLayout(new BorderLayout(0, 0));

		overviewList = new JList();
		overviewPanel.add(overviewList, BorderLayout.CENTER);
		overviewList.setModel(listModel);
		overviewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		overviewSouth = new JPanel();
		overviewPanel.add(overviewSouth, BorderLayout.SOUTH);
		overviewSouth.setLayout(new GridLayout(3, 1, 0, 0));

		overviewTextPanel = new JPanel();
		overviewSouth.add(overviewTextPanel);

		newTextField = new JTextField();
		newTextField.setColumns(10);
		newTextField.setDocument(new TextFieldDoc());
		overviewTextPanel.add(newTextField);

		overviewActionPanel = new JPanel();
		overviewSouth.add(overviewActionPanel);

		newButton = new JButton("Neu");
		overviewActionPanel.add(newButton);

		removeButton = new JButton("Entfernen");
		removeButton.setEnabled(false);
		overviewActionPanel.add(removeButton);

		exportPanel = new JPanel();
		overviewSouth.add(exportPanel);

		exportButton = new JButton("Export");
		exportPanel.add(exportButton);

		// rechte Seite
		creatorPanel = new JPanel();
		contentPane.add(creatorPanel, BorderLayout.CENTER);
		creatorPanel.setLayout(new BorderLayout(0, 0));

		creatorOverviewPanel = new JPanel();
		creatorPanel.add(creatorOverviewPanel, BorderLayout.CENTER);
		cardLayout = new CardLayout();
		creatorOverviewPanel.setLayout(cardLayout);
		
		creatorMenuPanel = new JPanel();
		creatorPanel.add(creatorMenuPanel, BorderLayout.SOUTH);
		creatorMenuPanel.setMinimumSize(new Dimension(10, 90));
		creatorMenuPanel.setPreferredSize(new Dimension(10, 90));
		
		textAreaPanel = new JPanel();
		textAreaPanel.setPreferredSize(new Dimension(250, 60));
		textAreaPanel.setMinimumSize(new Dimension(250, 60));
		textAreaPanel.setLayout(new BorderLayout());
		creatorMenuTextArea = new JTextArea();
		textAreaPanel.add(new JScrollPane(creatorMenuTextArea), BorderLayout.CENTER);
		creatorMenuPanel.add(textAreaPanel);

		panel = new JPanel();
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
