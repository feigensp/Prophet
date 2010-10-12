package experimentQuestionCreator;

import java.awt.AWTEvent;
import java.awt.FlowLayout;
import java.awt.event.AWTEventListener;
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

public class QuestionElement extends JPanel implements AWTEventListener {
	
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

	public QuestionElement(String text, int selection) {
		super();
		setLayout(new FlowLayout());
		createContent(text, selection);
		createMenu();
		add(contentPanel);
		add(menuPanel);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
//	public void processEvent(AWTEvent e) {
//		if(e.getID() == MouseEvent.MOUSE_CLICKED) {
//			System.out.println("im processEvent");
//			fireEvent();
//		}
//		super.processEvent(e);
//	}
	
	public void addQuestionElementListener(QuestionElementListener listener) {
		if(questionElementListeners == null)
			questionElementListeners = new Vector<QuestionElementListener>();
		questionElementListeners.addElement(listener);
	}
	public void removeQuestionelementListener(QuestionElementListener listener) {
		if(questionElementListeners != null) 
			questionElementListeners.removeElement(listener);
	}
	private void fireEvent() {
		if(questionElementListeners == null)
			return;
		QuestionElementEvent event = new QuestionElementEvent(this, QuestionElementEvent.QELECLOSED, this);
		for (Enumeration<QuestionElementListener> e = questionElementListeners.elements(); e.hasMoreElements(); )
			((QuestionElementListener)e.nextElement()).questionElementClosed(event);
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

	@Override
	public void eventDispatched(AWTEvent arg0) {
		if (arg0 instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) arg0;
			if(me.getSource() == close) {
				System.out.println("test");				
			}
		}
	}
}
