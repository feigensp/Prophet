package experimentQuestionCreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ExtendedPanel extends JPanel implements QuestionElementListener {

	private int id;
	private String name;
	private JLabel headline;
	private JPanel questionHolder;
	private BoxLayout boxLayout;
	static ArrayList<Integer> ids = new ArrayList<Integer>();
	private LinkedList<QuestionElement> elements = new LinkedList<QuestionElement>();
	private Font headlineFont = new Font("monospaced", Font.BOLD, 14);

	public ExtendedPanel(int id, String name) {
		super();
		ids.add(id);
		this.id = id;
		this.name = name;
		initialise();
	}

	private void initialise() {
		JPanel headlinePanel = new JPanel();
		headlinePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		headline = new JLabel(name);
		headline.setFont(headlineFont);
		headline.setToolTipText("Klicken um Titel zu ändern");
		headlinePanel.add(headline);
		setLayout(new BorderLayout());
		add(headlinePanel, BorderLayout.NORTH);
		questionHolder = new JPanel();
		add(new JScrollPane(questionHolder), BorderLayout.CENTER);
		boxLayout = new BoxLayout(questionHolder, BoxLayout.Y_AXIS);
		questionHolder.setLayout(boxLayout);
	}

	public static int nextFreeId() {
		int i = 0;
		while (ids.contains(i)) {
			i++;
		}
		return i;
	}

	public void removeId() {
		ids.remove((Integer) id);
	}

	public int getId() {
		return id;
	}

	public LinkedList<QuestionElement> getElements() {
		return elements;
	}

	// Methoden um neue Komponenten hinzuzufügen
	public void addComponent(String text, int selection) {
		QuestionElement qEle = new QuestionElement(text, selection);
		qEle.addQuestionElementListener(this);
		elements.add(qEle);
		qEle.setAlignmentX(Component.LEFT_ALIGNMENT);
		questionHolder.add(qEle);
		questionHolder.updateUI();
	}

	public void removeComponent(QuestionElement qe) {
		elements.remove(qe);
		questionHolder.remove(qe);
		questionHolder.updateUI();
	}
	
	public void updateView() {
		for(QuestionElement ele : elements) {
			questionHolder.remove(ele);
		}
		for(QuestionElement ele : elements) {
			questionHolder.add(ele);
		}
		questionHolder.updateUI();
	}
	
	public void setName(String name) {
		this.name = name;
		headline.setText(name);
	}

	@Override
	public void questionElementClose(QuestionElementEvent e) {
		removeComponent(e.getQuestionElement());

	}

	@Override
	public void questionElementUp(QuestionElementEvent e) {
		int i = elements.indexOf(e.getQuestionElement());
		if(i-1 >= 0) {
			elements.remove(i);
			elements.add(i-1, e.getQuestionElement());
			updateView();
		}
	}

	@Override
	public void questionElementDown(QuestionElementEvent e) {
		int i = elements.indexOf(e.getQuestionElement());
		if(i+1 < elements.size()) {
			elements.remove(i);
			elements.add(i+1, e.getQuestionElement());
			updateView();
		}
	}
}
