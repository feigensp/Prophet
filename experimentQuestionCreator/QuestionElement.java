package experimentQuestionCreator;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
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

	private static Dimension menuSize; // = new Dimension(108, 33);

	// public static Dimension menuSize;

	private JPanel menuPanel;
	private JPanel contentPanel;
	private JLabel close;
	private int selection;
	private String text;

	private boolean dragged;
	private Point oldPos;

	public QuestionElement(String text, int selection) {
		super();
		this.selection = selection;
		this.text = text;
		dragged = false;
		oldPos = new Point();
		super.setLayout(new FlowLayout(FlowLayout.LEFT));
		createMenu();
		createContent(text, selection);
		add(menuPanel);
		add(contentPanel);
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
		});
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	public QuestionElement(String text, int selection, Dimension size) {
		super();
		this.selection = selection;
		this.text = text;
		dragged = false;
		oldPos = new Point();
		super.setLayout(new FlowLayout(FlowLayout.LEFT));
		createMenu();
		createContent(text, selection, size);
		add(menuPanel);
		add(contentPanel);
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
		});
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public Dimension getElementSize() {
		return contentPanel.getPreferredSize();
	}

	private void updateSize(Dimension change) {
		Dimension oldD = getPreferredSize();
		Dimension newD = new Dimension(
				(int) (oldD.getWidth() + change.getWidth()),
				(int) (oldD.getHeight() + change.getHeight()));
		setPreferredSize(newD);
		setMaximumSize(newD);
		setMinimumSize(newD);
		updateUI();
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

	private void fireEvent(int eventType) {
		if (questionElementListeners == null)
			return;
		QuestionElementEvent event;

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

	private void createMenu() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton up = new JButton("<");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireEvent(QuestionElementEvent.QELEUP);
			}
		});
		JButton down = new JButton(">");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireEvent(QuestionElementEvent.QELEDOWN);
			}
		});
		close = new JLabel("x");
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fireEvent(QuestionElementEvent.QELECLOSED);
			}
		});
		menuPanel.add(up);
		menuPanel.add(down);
		menuPanel.add(close);
		if (menuSize == null) {
			menuSize = menuPanel.getPreferredSize();
		} else {
			menuPanel.setPreferredSize(menuSize);
			menuPanel.setMaximumSize(menuSize);
			menuPanel.setMinimumSize(menuSize);
		}
	}

	private void createContent(String text, int selection) {
		contentPanel = new JPanel();
		//contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setLayout(new BorderLayout());
		String[] texte = text.split("\n");
		switch (selection) {
		case LABEL:
			JLabel label = new JLabel(text);
			contentPanel.add(label, BorderLayout.CENTER);
			break;
		case TEXTFIELD:
			contentPanel.add(new JTextField(text, 10), BorderLayout.CENTER);
			break;
		case TEXTAREA:
			contentPanel.add(new JTextArea(text), BorderLayout.CENTER);
			break;
		case COMBOBOX:
			contentPanel.add(new JComboBox(texte), BorderLayout.CENTER);
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
			contentPanel.add(chkPanel, BorderLayout.CENTER);
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
			contentPanel.add(radioPanel, BorderLayout.CENTER);
			break;
		default:
			break;
		}
		Dimension componentSize = contentPanel.getPreferredSize();
		Dimension absoluteSize = new Dimension((int) (componentSize.getWidth()
				+ menuSize.getWidth() + 100), (int) (Math.max(
				componentSize.getHeight(), menuSize.getHeight()) + 25));
		super.setPreferredSize(absoluteSize);
		super.setMinimumSize(absoluteSize);
		super.setMaximumSize(absoluteSize);
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	private void createContent(String text, int selection, Dimension size) {
		contentPanel = new JPanel();
		//contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setLayout(new BorderLayout());
		String[] texte = text.split("\n");
		switch (selection) {
		case LABEL:
			JLabel label = new JLabel(text);
			contentPanel.add(label, BorderLayout.CENTER);
			break;
		case TEXTFIELD:
			contentPanel.add(new JTextField(text, 10), BorderLayout.CENTER);
			break;
		case TEXTAREA:
			contentPanel.add(new JTextArea(text), BorderLayout.CENTER);
			break;
		case COMBOBOX:
			contentPanel.add(new JComboBox(texte), BorderLayout.CENTER);
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
			contentPanel.add(chkPanel, BorderLayout.CENTER);
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
			contentPanel.add(radioPanel, BorderLayout.CENTER);
			break;
		default:
			break;
		}
		contentPanel.setPreferredSize(size);
		contentPanel.setMinimumSize(size);
		contentPanel.setMaximumSize(size);
		Dimension absoluteSize = new Dimension((int) (size.getWidth()
				+ menuSize.getWidth() + 100), (int) (Math.max(
				size.getHeight(), menuSize.getHeight()) + 25));
		super.setPreferredSize(absoluteSize);
		super.setMinimumSize(absoluteSize);
		super.setMaximumSize(absoluteSize);
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
