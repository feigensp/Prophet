package experimentQuestionCreator;

import java.awt.AWTEvent;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QuestionElement extends JPanel {

	Vector<QuestionElementListener> questionElementListeners;

	public static final int LABEL = 0;
	public static final int TEXTFIELD = 1;
	public static final int TEXTAREA = 2;
	public static final int COMBOBOX = 3;
	public static final int CHECKBOX = 4;
	public static final int RADIOBUTTON = 5;

	private JPanel menuPanel;
	private JPanel contentPanel;
	private JLabel close;
	private int selection;
	private String text;

	public QuestionElement(String text, int selection) {
		super();
		this.selection = selection;
		this.text = text;
		setLayout(new GridLayout(1, 2, 10, 0));
		createContent(text, selection);
		createMenu();
		add(contentPanel);
		add(menuPanel);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	public void addQuestionElementListener(QuestionElementListener listener) {
		if (questionElementListeners == null)
			questionElementListeners = new Vector<QuestionElementListener>();
		questionElementListeners.addElement(listener);
	}

	public void removeQuestionelementListener(QuestionElementListener listener) {
		if (questionElementListeners != null)
			questionElementListeners.removeElement(listener);
	}

	private void fireEvent() {
		if (questionElementListeners == null)
			return;
		QuestionElementEvent event = new QuestionElementEvent(this,
				QuestionElementEvent.QELECLOSED, this);
		for (Enumeration<QuestionElementListener> e = questionElementListeners
				.elements(); e.hasMoreElements();)
			((QuestionElementListener) e.nextElement())
					.questionElementClosed(event);
	}

	private void createMenu() {
		menuPanel = new JPanel();
		JButton up = new JButton("nach oben");
		JButton down = new JButton("nach unten");
		close = new JLabel("x");
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fireEvent();
			}
		});
		menuPanel.add(up);
		menuPanel.add(down);
		menuPanel.add(close);
	}

	private void createContent(String text, int selection) {
		contentPanel = new JPanel();
		String[] texte = text.split("\n");
		switch (selection) {
		case LABEL:
			contentPanel.add(new JLabel(text));
			break;
		case TEXTFIELD:
			contentPanel.add(new JTextField(text));
			break;
		case TEXTAREA:
			contentPanel.add(new JTextArea(text));
			break;
		case COMBOBOX:
			contentPanel.add(new JComboBox(texte));
			break;
		case CHECKBOX:
			JPanel chkPanel = new JPanel();
			chkPanel.setLayout(new GridLayout(texte.length, 1));
			ButtonGroup chkGroup = new ButtonGroup();
			for (int i = 0; i < texte.length; i++) {
				JCheckBox chkbox = new JCheckBox(texte[i]);
				chkPanel.add(chkbox);
				chkGroup.add(chkbox);
			}
			contentPanel.add(chkPanel);
			break;
		case RADIOBUTTON:
			JPanel radioPanel = new JPanel();
			radioPanel.setLayout(new GridLayout(texte.length, 1));
			ButtonGroup radioGroup = new ButtonGroup();
			for (int i = 0; i < texte.length; i++) {
				JRadioButton radiobtn = new JRadioButton(texte[i]);
				radioPanel.add(radiobtn);
				radioGroup.add(radiobtn);
			}
			contentPanel.add(radioPanel);
			break;
		default:
			break;
		}
	}

	public int getSelection() {
		return selection;
	}

	public String getSelectionString() {
		switch (selection) {
		case LABEL:
			return "label";
		case TEXTFIELD:
			return "textfield";
		case TEXTAREA:
			return "textarea";
		case COMBOBOX:
			return "combobox";
		case CHECKBOX:
			return "checkbox";
		case RADIOBUTTON:
			return "radiobutton";
		default:
			return "";
		}
	}

	public String getText() {
		return text;
	}
}
