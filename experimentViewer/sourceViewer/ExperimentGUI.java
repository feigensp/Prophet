/**
 * Diese Klasse stellt eine Oberfläche bereit, in welcher sich Fragen aus XML Dateien angeschaut werden können.
 * Die Antworten, welche gegeben werden, werden als XML-Dokumente exportiert.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentViewer.sourceViewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import test.Watch;
import test.experimentQuestionCreator.ElementAttribute;
import test.experimentQuestionCreator.ExtendedPanel;
import test.experimentQuestionCreator.MyTreeNode;
import test.experimentQuestionCreator.QuestionElement;
import test.experimentQuestionCreator.XMLHandler;

public class ExperimentGUI extends JFrame {

	// Pfad zu den Anfangsfragen
	private String startPath;

	private String codewort;
	private int lastSelection;

	private JButton startButton;
	private DefaultListModel listModel;
	private Vector<ExtendedPanel> questionPanels;
	private Vector<Watch> timelines;
	private JPanel questionCollectionPanel;
	private CardLayout questionCardLayout;
	private CardLayout timeCardLayout;
	private JList list;
	private JButton backButton;
	private JButton forwardButton;
	private JPanel timelinePanel;
	private Watch time;
	private JButton exportButton;

	/**
	 * Launch the application.
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
					ExperimentGUI frame = new ExperimentGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Wandelt die eingegeben Daten aller Fragen in eine Baumform um
	 * 
	 * @return Wurzelelement des erstellten Baumes
	 */
	public MyTreeNode exportData() {
		// Baum mit Informationen aufbauen
		MyTreeNode root = new MyTreeNode();
		String s;

		for (int i = 0; i < questionPanels.size(); i++) {
			// Fragen hinzufügen - dazu Attributliste erstellen
			Vector<ElementAttribute> questionAttributes = new Vector<ElementAttribute>();
			questionAttributes.add(new ElementAttribute<String>("text",
					((String) listModel.get(i))));

			MyTreeNode question = new MyTreeNode(root, questionAttributes);
			root.addChild(question);
			// Elemente der Fragen hinzufügen
			LinkedList<QuestionElement> elements = questionPanels.get(i)
					.getElements();
			for (QuestionElement ele : elements) {
				// Vector für die Attribute der Komponenten
				Vector<ElementAttribute> componentAttributes = new Vector<ElementAttribute>();
				int sel = ele.getSelection();
				switch (sel) {
				case QuestionElement.TEXTFIELD:
					componentAttributes.add(new ElementAttribute<String>(
							"model", "" + QuestionElement.TEXTFIELD));
					componentAttributes.add(new ElementAttribute<String>(
							"answer", ele.getTextField().getText()));
					break;
				case QuestionElement.TEXTAREA:
					componentAttributes.add(new ElementAttribute<String>(
							"model", "" + QuestionElement.TEXTAREA));
					componentAttributes.add(new ElementAttribute<String>(
							"answer", ele.getTextArea().getText()));
					break;
				case QuestionElement.COMBOBOX:
					componentAttributes.add(new ElementAttribute<String>(
							"model", "" + QuestionElement.COMBOBOX));
					componentAttributes.add(new ElementAttribute<String>(
							"answer", ele.getComboBox().getSelectedItem()
									.toString()));
					break;
				case QuestionElement.CHECKBOX:
					componentAttributes.add(new ElementAttribute<String>(
							"model", "" + QuestionElement.CHECKBOX));
					Component[] checkComp = ele.getCheckBoxPanel()
							.getComponents();
					Object check = null;
					s = "";
					for (int j = 0; j < checkComp.length; j++) {
						check = checkComp[j];
						if (check instanceof JCheckBox) {
							if (((JCheckBox) check).isSelected()) {
								s += (((JCheckBox) check).getText());
							}
						}
					}
					componentAttributes.add(new ElementAttribute<String>(
							"answer", s));
					break;
				case QuestionElement.RADIOBUTTON:
					componentAttributes.add(new ElementAttribute<String>(
							"model", "" + QuestionElement.RADIOBUTTON));
					Component[] radioComp = ele.getRadioButtonPanel()
							.getComponents();
					Object radio = null;
					for (int j = 0; j < radioComp.length; j++) {
						radio = radioComp[j];
						if (radio instanceof JRadioButton) {
							if (((JRadioButton) radio).isSelected()) {
								componentAttributes
										.add(new ElementAttribute<String>(
												"answer",
												((JRadioButton) radio)
														.getText()));
							}
						}
					}
					break;
				}
				if (componentAttributes.size() > 0) {
					MyTreeNode element = new MyTreeNode(question,
							componentAttributes);
					question.addChild(element);
				}
			}
		}
		return root;
	}

	/**
	 * Listener zu den Objekten hinzufügen
	 */
	public void setListener() {
		// Codeworteingabe --> Fragen werden gestartet
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				loadList();
				// Codeworteingabe entfernen
				questionCollectionPanel.removeAll();
				int i = 0;
				for (JPanel question : questionPanels) {
					questionCollectionPanel.add(question, "" + i);
					timelines.add(new Watch("Aufgabenzeit: "));
					timelines.get(i).start();
					timelines.get(i).pause();
					timelinePanel.add(timelines.get(i), "" + i);
					i++;
				}
				time.start();
				timelines.get(0).resume();

				exportButton.setEnabled(true);
				list.setSelectedIndex(0);
			}
		});

		// Fragen wechseln
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					int i = list.getSelectedIndex();
					questionCardLayout.show(questionCollectionPanel, "" + i);
					// Stoppuhr pausieren, nächste starten
					timeCardLayout.show(timelinePanel, "" + i);
					timelines.get(lastSelection).pause();
					lastSelection = i;
					timelines.get(i).resume();

					if (listModel.size() >= 1) {
						forwardButton.setEnabled(true);
					}
				}
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				forwardButton.setEnabled(true);
				int i = list.getSelectedIndex();
				if (i > 0) {
					list.setSelectedIndex(i - 1);
					if (i == 1) {
						backButton.setEnabled(false);
					}
				}
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				backButton.setEnabled(true);
				int i = list.getSelectedIndex();
				if (i < listModel.size() - 1) {
					list.setSelectedIndex(i + 1);
					if (i == listModel.size() - 2) {
						forwardButton.setEnabled(false);
					}
				}
			}
		});

		// Daten exportieren
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				XMLHandler.writeXMLTree(exportData(), "answers.xml");
			}
		});
	}

	/**
	 * Lädt alle Fragen einer XML-Datei und fügt diese in die Liste ein
	 */
	public void loadList() {
		MyTreeNode root = XMLHandler.loadXMLTree("test.xml");
		// Seiten erstellen

		Vector<MyTreeNode> treeQuestions = root.getChildren();
		int i = 0;
		// Seiten hinzufügen
		for (MyTreeNode treeQuestion : treeQuestions) {
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
			listModel.addElement(questionText);
			ExtendedPanel questionPanel = new ExtendedPanel(i, questionText);
			questionPanel.setOpaque(true);
			// Komponenten hinzufügen
			Vector<MyTreeNode> components = treeQuestion.getChildren();
			for (MyTreeNode treeComponent : components) {
				// Komponent-Attribute
				Vector<ElementAttribute> componentAttributes = treeComponent
						.getAttributes();
				String componentText = "default";
				String componentAnswer = "";
				int componentModel = 0;
				int componentWidth = 75;
				int componentHeight = 25;
				for (ElementAttribute componentAttribute : componentAttributes) {
					// Model Attribut
					if (componentAttribute.getName().equals("model")) {
						componentModel = Integer.parseInt(componentAttribute
								.getContent().toString());
					}
					// Text Attribut
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
				questionPanel.addComponent(componentText, componentAnswer,
						componentModel, false, new Dimension(componentWidth,
								componentHeight), false);
			}
			// Frage zur Sammlung hinzufügen
			questionPanels.add(questionPanel);
			i++;
		}
	}

	/**
	 * GUI erstellen und Variablen initialisieren
	 */
	public ExperimentGUI() {
		listModel = new DefaultListModel();
		questionPanels = new Vector<ExtendedPanel>();
		timelines = new Vector<Watch>();
		lastSelection = 0;

		JPanel contentPane;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 743, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(10, 50));
		menuPanel.setMinimumSize(new Dimension(10, 50));
		contentPane.add(menuPanel, BorderLayout.SOUTH);
		menuPanel.setLayout(new BorderLayout(0, 0));

		JPanel backPanel = new JPanel();
		menuPanel.add(backPanel, BorderLayout.WEST);
		backPanel.setLayout(new BorderLayout(0, 0));

		backButton = new JButton("Zur\u00FCck");
		backButton.setEnabled(false);
		backPanel.add(backButton, BorderLayout.CENTER);

		JPanel forwardPanel = new JPanel();
		menuPanel.add(forwardPanel, BorderLayout.EAST);
		forwardPanel.setLayout(new BorderLayout(0, 0));

		forwardButton = new JButton("Vorw\u00E4rts");
		forwardButton.setEnabled(false);
		forwardPanel.add(forwardButton, BorderLayout.CENTER);

		JPanel centerMenuPanel = new JPanel();
		menuPanel.add(centerMenuPanel, BorderLayout.CENTER);

		timelinePanel = new JPanel();
		timeCardLayout = new CardLayout();
		timelinePanel.setLayout(timeCardLayout);
		timelinePanel.setPreferredSize(new Dimension(125, 35));
		centerMenuPanel.add(timelinePanel);

		exportButton = new JButton("Antworten Sichern");
		exportButton.setEnabled(false);
		centerMenuPanel.add(exportButton);

		JPanel timePanel = new JPanel();
		timePanel.setLayout(new CardLayout());
		timePanel.setPreferredSize(new Dimension(125, 35));
		time = new Watch("Gesamtzeit");
		timePanel.add(time, "gesamtzeit");
		centerMenuPanel.add(timePanel);

		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane centerSplit = new JSplitPane();
		centerPanel.add(centerSplit);

		JPanel overviewPanel = new JPanel();
		overviewPanel.setPreferredSize(new Dimension(125, 10));
		overviewPanel.setMinimumSize(new Dimension(125, 10));
		centerSplit.setLeftComponent(overviewPanel);
		overviewPanel.setLayout(new BorderLayout(0, 0));

		list = new JList();
		list.setModel(listModel);
		overviewPanel.add(list, BorderLayout.CENTER);

		questionCollectionPanel = new JPanel();
		centerSplit.setRightComponent(questionCollectionPanel);
		questionCardLayout = new CardLayout();
		questionCollectionPanel.setLayout(questionCardLayout);

		JPanel codePanel = new JPanel();
		questionCollectionPanel.add(codePanel, "start");
		codePanel.setLayout(new BorderLayout(0, 0));

		JPanel codeNorth = new JPanel();
		codePanel.add(codeNorth, BorderLayout.NORTH);

		JLabel codeHeadlineLabel = new JLabel("Codeworteingabe");
		codeHeadlineLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		codeNorth.add(codeHeadlineLabel);

		JPanel codeCenter = new JPanel();
		codePanel.add(codeCenter, BorderLayout.CENTER);
		codeCenter.setLayout(new BoxLayout(codeCenter, BoxLayout.Y_AXIS));

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut_1);

		JLabel codeInstructionLabel = new JLabel(
				"<html>Bitte geben sie ihr codewort ein, es wird wie folgt gebildet:<br>...</html>");
		codeInstructionLabel.setHorizontalTextPosition(SwingConstants.LEADING);
		codeCenter.add(codeInstructionLabel);

		Component verticalStrut = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut);

		JLabel lblbeispiel = new JLabel("<html>Beispiel:<br>...</html>");
		codeCenter.add(lblbeispiel);

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut_2);

		JLabel codeLabel = new JLabel("Codewort:");
		codeCenter.add(codeLabel);

		startButton = new JButton("Start");
		codeCenter.add(startButton);

		// Listener setzen
		setListener();
	}
}
