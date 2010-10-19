package experimentQuestionCreator;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QuestionElement extends JPanel {

	// Vector der alle Listener beherbergt
	Vector<QuestionElementListener> questionElementListeners;

	// Konstanten, für die jeweiligen Komponenten stehen (in Enum ändern?)
	public static final int LABEL = 0;
	public static final int TEXTFIELD = 1;
	public static final int TEXTAREA = 2;
	public static final int COMBOBOX = 3;
	public static final int CHECKBOX = 4;
	public static final int RADIOBUTTON = 5;

	// Größe des Menus - Konstant, gilt für alle --> static
	private static Dimension menuSize;

	private JPanel contentPanel;// Panel für Inhalt der Komponente

	private JPanel menuPanel; // Panel für menu der Komponente
	private JCheckBox editCheckBox;

	private JPanel editPanel; // Panel für das editieren einzelner Komponenten
	private JTextArea editTextArea;
	private JComboBox editComboBox;

	private int selection;// ausgewählte Komponente
	private String text;
	// Extra antwort String, muss noch eingebaut werden
	private String answer;

	// Variablen für Drag and Drop
	private boolean dragged;
	private Point oldPos;
	private Point newPos;

	private JComponent comp;

	/**
	 * Konstruktor für das erstellen einer Komponente mit Standartgröße
	 * 
	 * @param text
	 *            Inhalt/Beschriftung der Komponente
	 * @param selection
	 *            Komponententyp
	 */
	public QuestionElement(String text, int selection) {
		super();
		this.selection = selection;
		this.text = text;
		this.answer = "";

		dragged = false;
		oldPos = new Point();

		super.setLayout(new FlowLayout(FlowLayout.LEFT));

		createMenu();
		createEditPanel();
		createContent();

		add(menuPanel);
		add(contentPanel);

		addDragAndDrop();

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Konstruktor für das Erstellen einer Komponente mir bestimmten
	 * Eigenschaften
	 * 
	 * @param text
	 *            Inhalt/Beschriftung der Komponente
	 * @param answer
	 *            Vorgegebene antwort
	 * @param selection
	 *            Komponententyp
	 * @param menu
	 *            Gibt an, ob ein Editor-Menu für diese Komponente mit erzeugt
	 *            werden soll
	 * @param size
	 *            Größe der Komponente
	 * @param border
	 *            Gibt an ob die Komponente umrandet sein soll
	 */
	public QuestionElement(String text, String answer, int selection,
			boolean menu, Dimension size, boolean border) {
		super();
		this.selection = selection;
		this.text = text;
		this.answer = answer;

		dragged = false;
		oldPos = new Point();

		super.setLayout(new FlowLayout(FlowLayout.LEFT));

		createMenu();
		createEditPanel();
		createContent(size, border);

		if (menu) {
			add(menuPanel);
		} else {
			JPanel filler = new JPanel();
			filler.setPreferredSize(menuSize);
			add(filler);
		}
		add(contentPanel);

		addDragAndDrop();

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Methode die das Menu zur Komponente erstellt
	 */
	private void createMenu() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// zurück-Button und Listener
		JButton up = new JButton("<");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireEvent(QuestionElementEvent.QELEUP);
			}
		});
		// vor-Button und Listener
		JButton down = new JButton(">");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireEvent(QuestionElementEvent.QELEDOWN);
			}
		});
		// schliessen-Label und Listener
		JLabel close = new JLabel("x");
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fireEvent(QuestionElementEvent.QELECLOSED);
			}
		});
		// CheckBox für das Aktivieren des Editiervorgangs
		editCheckBox = new JCheckBox();
		editCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (editCheckBox.isSelected()) {
					remove(contentPanel);
					if (selection == TEXTFIELD && !answer.equals("")) {
						editTextArea.setText(text + "[" + answer + "]");
					} else {
						editTextArea.setText(text);
					}
					editComboBox.setSelectedIndex(selection);
					add(editPanel);
					updateSize(editPanel);
				} else {
					remove(editPanel);
					add(contentPanel);
					updateSize(contentPanel);
				}
				updateUI();
			}
		});

		menuPanel.add(up);
		menuPanel.add(editCheckBox);
		menuPanel.add(down);
		menuPanel.add(close);

		// Menu-Größe einmalig messen
		if (menuSize == null) {
			menuSize = menuPanel.getPreferredSize();
		} else {
			menuPanel.setPreferredSize(menuSize);
			menuPanel.setMaximumSize(menuSize);
			menuPanel.setMinimumSize(menuSize);
		}
	}

	/**
	 * Methode die das Editierpanel mit seinen Funktionen erstellt
	 */
	private void createEditPanel() {
		editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		editTextArea = new JTextArea();
		editTextArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				updateSize(editPanel);
			}
		});
		editPanel.add(editTextArea, BorderLayout.CENTER);
		JPanel south = new JPanel();
		editPanel.add(south, BorderLayout.SOUTH);
		editComboBox = new JComboBox();
		editComboBox.setModel(new DefaultComboBoxModel(new String[] { "JLabel",
				"JTextField", "JTextArea", "JComboBox", "JCheckBox",
				"JRadioButton" }));
		south.add(editComboBox);
		JButton editButton = new JButton("Übernehmen");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				selection = editComboBox.getSelectedIndex();
				text = editTextArea.getText();
				// Text und Antwort trennen wenn es ein TextField ist (Antwort
				// wenn ganz hinten etwas in eckigen Klammern)
				if (selection == TEXTFIELD && text.endsWith("]")
						&& text.contains("[")) {
					answer = text.substring(text.lastIndexOf("[") + 1,
							text.length() - 1);
					text = text.substring(0, text.lastIndexOf("[") - 1);
				} else {
					answer = "";
				}
				contentPanel = null;
				createContent();
				remove(editPanel);
				add(contentPanel);
				updateSize(contentPanel);
				editCheckBox.setSelected(false);
				addDragAndDrop();
				updateUI();
			}
		});
		south.add(editButton);
		updateSize(editPanel);
	}

	/**
	 * Methode um die eigentliche Komponente zu erstellen
	 */
	private void createContent() {
		createContentHelp(true);
		updateSize(contentPanel);
	}

	/**
	 * Methode um eigentlich Komponente mit spezieller Größe zu erstellen
	 * 
	 * @param size
	 *            Größe der Komponente
	 * @param border
	 *            Gibt an ob die Komponente umrandet sein soll
	 */
	private void createContent(Dimension size, boolean border) {
		createContentHelp(border);
		// Größe der Komponente setzen
		contentPanel.setPreferredSize(size);
		contentPanel.setMinimumSize(size);
		contentPanel.setMaximumSize(size);
		updateSize(contentPanel);
	}

	/**
	 * Hilfsfunktion zur CreateContent-Methode, da diese überladen ist und der
	 * Code übersichtlicher gehalten werden soll
	 * 
	 * @param border
	 *            gibt an ob die Komponente umrandet sein soll
	 */
	private void createContentHelp(boolean border) {
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		if (border)
			contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Text splitten für Componenten wie JRadioButton
		String[] texte = text.split("\n");

		// Komponente erstellen
		switch (selection) {
		case LABEL:
			comp = new JLabel(text);
			contentPanel.add(comp, BorderLayout.CENTER);
			break;
		case TEXTFIELD:
			comp = new JTextField(text, 10);
			contentPanel.add(comp, BorderLayout.CENTER);
			break;
		case TEXTAREA:
			comp = new JTextArea(text);
			contentPanel.add(new JScrollPane(comp), BorderLayout.CENTER);
			break;
		case COMBOBOX:
			comp = new JComboBox(texte);
			((JComboBox) comp).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					answer = "" + ((JComboBox) comp).getSelectedIndex();
				}
			});
			if (!answer.equals("")) {
				((JComboBox) comp).setSelectedIndex(Integer.parseInt(answer));
			}
			contentPanel.add(comp, BorderLayout.CENTER);
			break;
		case CHECKBOX:
			comp = new JPanel();
			comp.setLayout(new GridLayout(texte.length, 1));
			// Integer-Array mit vorgegeben Antworten versuchen zu erstellen
			ArrayList<Integer> selectedBoxes = new ArrayList<Integer>();
			if (!answer.equals("")) {
				String[] selectedAnswers = answer.split(",");
				for (int i = 0; i < selectedAnswers.length; i++) {
					try {
						selectedBoxes.add(Integer.parseInt(selectedAnswers[i]));
					} catch (NumberFormatException e) {
					}
				}
			}
			for (int i = 0; i < texte.length; i++) {
				JCheckBox chkbox = new JCheckBox(texte[i]);
				comp.add(chkbox);
				// Antwort setzen wenn vorgegeben
				if (selectedBoxes.contains(i)) {
					chkbox.setSelected(true);
				}
				// Listener hinzufügen, der die Antwort aktualisiert
				chkbox.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						answer = "";
						Component[] checkComp = comp.getComponents();
						Object check = null;
						for (int i = 0; i < checkComp.length; i++) {
							check = checkComp[i];
							if (check instanceof JCheckBox) {
								if (((JCheckBox) check).isSelected()) {
									answer += i + ",";
								}
							}
						}
					}
				});
			}
			contentPanel.add(comp, BorderLayout.CENTER);
			break;
		case RADIOBUTTON:
			comp = new JPanel();
			ButtonGroup btngroup = new ButtonGroup();
			comp.setLayout(new GridLayout(texte.length, 1));
			// Radiobuttons erstellen und hinzufügen
			for (int i = 0; i < texte.length; i++) {
				JRadioButton radiobtn = new JRadioButton(texte[i]);
				comp.add(radiobtn);
				btngroup.add(radiobtn);
				// Antwort setzen wenn vorgegeben
				if (!answer.equals("")) {
					if (i == Integer.parseInt(answer)) {
						radiobtn.setSelected(true);
					}
				}
				// Listener hinzufügen, der die Antwort aktualisiert
				radiobtn.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						Component[] radioComp = comp.getComponents();
						Object radio = null;
						for (int i = 0; i < radioComp.length; i++) {
							radio = radioComp[i];
							if (radio instanceof JRadioButton) {
								if (((JRadioButton) radio).isSelected()) {
									answer = "" + i;
								}
							}
						}
					}
				});
			}
			contentPanel.add(comp, BorderLayout.CENTER);
			break;
		default:
			break;
		}
	}

	/**
	 * Versucht die Textfeld-Komponente zurückzugeben
	 * 
	 * @return JTextfield, oder null wenn nicht möglich
	 */
	public JTextField getTextField() {
		if (selection == TEXTFIELD) {
			return (JTextField) comp;
		} else {
			return null;
		}
	}

	/**
	 * Versucht die TextArea-Komponente zurückzugeben
	 * 
	 * @return JTextArea, oder null wenn nicht möglich
	 */
	public JTextArea getTextArea() {
		if (selection == TEXTAREA) {
			return (JTextArea) comp;
		} else {
			return null;
		}
	}

	/**
	 * Versucht die ComboBox-Komponente zurückzugeben
	 * 
	 * @return JComboBox, oder null wenn nicht möglich
	 */
	public JComboBox getComboBox() {
		if (selection == COMBOBOX) {
			return (JComboBox) comp;
		} else {
			return null;
		}
	}

	/**
	 * Versucht das Panel mit den Checkbox-Komponenten zurückzugeben
	 * 
	 * @return JPanel, oder null wenn nicht möglich
	 */
	public JPanel getCheckBoxPanel() {
		if (selection == CHECKBOX) {
			return (JPanel) comp;
		} else {
			return null;
		}
	}

	/**
	 * Versucht das Panel mit den RadioButton-Komponenten zurückzugeben
	 * 
	 * @return JPanel, oder null wenn nicht möglich
	 */
	public JPanel getRadioButtonPanel() {
		if (selection == RADIOBUTTON) {
			return (JPanel) comp;
		} else {
			return null;
		}
	}

	/**
	 * Liefert den Komponententyp als Integer-Zurück
	 * 
	 * @return (0=JLabel; 1=JTextField; 2=JTextArea; 3=JComboBox; 4=JCheckBox;
	 *         5=JRadioButton)
	 */
	public int getSelection() {
		return selection;
	}

	/**
	 * Liefert den Inhalt/Beschriftung der Komponente zurück
	 * 
	 * @return Inhalt/Beschriftung der Komponente
	 */
	public String getText() {
		return text;
	}

	/**
	 * Liefert die Vorgegebene Antwort für diese Komponente zurück
	 * 
	 * @return Antwort als String
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Liefert Größe der Komponente (ohne Menu) zurück
	 * 
	 * @return
	 */
	public Dimension getElementSize() {
		return contentPanel.getPreferredSize();
	}

	/**
	 * Erstellt Listener für das Drag and Drop des contentPanels
	 */
	private void addDragAndDrop() {
		contentPanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				dragged = true;
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
			}

		});
		contentPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (dragged) {
					Dimension oldSize = contentPanel.getPreferredSize();
					Point newPos = me.getPoint();
					Dimension change = new Dimension(
							(int) (newPos.getX() - oldPos.getX()),
							(int) (newPos.getY() - oldPos.getY()));
					Dimension newSize = new Dimension(
							(int) (oldSize.getWidth() + change.getWidth()),
							(int) (oldSize.getHeight() + change.getHeight()));
					contentPanel.setPreferredSize(newSize);
					contentPanel.setMaximumSize(newSize);
					contentPanel.setMinimumSize(newSize);
					updateSize(change);
					dragged = false;
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent me) {
				oldPos = me.getPoint();
			}
		});
	}

	/**
	 * Aktualisiert die Größe der Komponente
	 * 
	 * @param change
	 *            Dimension um die sich die Größe ändern soll
	 */
	private void updateSize(Dimension change) {
		Dimension oldD = getPreferredSize();
		double x = (oldD.getWidth() + change.getWidth()) < menuSize.getWidth() ? menuSize.getWidth() : (oldD.getWidth() + change.getWidth());
		double y = (oldD.getHeight() + change.getHeight()) < menuSize.getHeight() ? menuSize.getHeight() : (oldD.getHeight() + change.getHeight());
		System.out.println("x: " + x + " - y: " + y);
		Dimension newD = new Dimension((int)x, (int)y);
		setPreferredSize(newD);
		setMaximumSize(newD);
		setMinimumSize(newD);
		updateUI();
	}

	/**
	 * Aktualisiert die Größe der Komponente auf seine Standartgröße
	 * 
	 * @param selection
	 */
	private void updateSize(JPanel selection) {
		// Größe der Komponente herausfinden
		Dimension componentSize = selection.getPreferredSize();
		Dimension absoluteSize = new Dimension((int) (componentSize.getWidth()
				+ menuSize.getWidth() + 100), (int) (Math.max(
				componentSize.getHeight(), menuSize.getHeight()) + 25));
		// Größe setzen
		setPreferredSize(absoluteSize);
		setMinimumSize(absoluteSize);
		setMaximumSize(absoluteSize);
	}

	/**
	 * Methode für das Hinzufügen eines QuestionElementListener
	 * 
	 * @param listener
	 */
	public void addQuestionElementListener(QuestionElementListener listener) {
		if (questionElementListeners == null)
			questionElementListeners = new Vector<QuestionElementListener>();
		questionElementListeners.addElement(listener);
	}

	/**
	 * Methode für das Entfernen eines QuestionElement Listener
	 * 
	 * @param listener
	 */
	public void removeQuestionelementListener(QuestionElementListener listener) {
		if (questionElementListeners != null)
			questionElementListeners.removeElement(listener);
	}

	/**
	 * Methode die das Event startet
	 * 
	 * @param eventType
	 *            Event, welches gestartet werden soll
	 */
	private void fireEvent(int eventType) {
		if (questionElementListeners == null)
			return;
		QuestionElementEvent event;
		// Event Erstellen
		switch (eventType) {
		case QuestionElementEvent.QELECLOSED:
			event = new QuestionElementEvent(this,
					QuestionElementEvent.QELECLOSED, this);
			break;
		case QuestionElementEvent.QELEUP:
			event = new QuestionElementEvent(this, QuestionElementEvent.QELEUP,
					this);
			break;
		case QuestionElementEvent.QELEDOWN:
			event = new QuestionElementEvent(this, QuestionElementEvent.QELEUP,
					this);
			break;
		default:
			return;
		}
		// Listener
		for (Enumeration<QuestionElementListener> e = questionElementListeners
				.elements(); e.hasMoreElements();) {
			switch (eventType) {
			case QuestionElementEvent.QELECLOSED:
				((QuestionElementListener) e.nextElement())
						.questionElementClose(event);
				break;
			case QuestionElementEvent.QELEUP:
				((QuestionElementListener) e.nextElement())
						.questionElementUp(event);
				break;
			case QuestionElementEvent.QELEDOWN:
				((QuestionElementListener) e.nextElement())
						.questionElementDown(event);
				break;
			default:
				return;
			}
		}
	}
}
