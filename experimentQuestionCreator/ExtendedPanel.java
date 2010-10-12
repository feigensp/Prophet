package experimentQuestionCreator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExtendedPanel extends JPanel implements QuestionElementListener{

	private int id;
	private String name;
	private JLabel headline;
	private JPanel questionHolder;
	private GridLayout gridLayout = null;
	static ArrayList<Integer> ids = new ArrayList<Integer>();
	private LinkedList<QuestionElement> elements = new LinkedList<QuestionElement>();

	public ExtendedPanel(int id, String name) {
		super();
		ids.add(id);
		this.id = id;
		this.name = name;
		initialise();
	}

	private void initialise() {
		headline = new JLabel(name);
		setLayout(new BorderLayout());
		add(headline, BorderLayout.NORTH);
		questionHolder = new JPanel();
		add(questionHolder, BorderLayout.CENTER);
		gridLayout = new GridLayout(0, 1);
		questionHolder.setLayout(gridLayout);
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

	// Methoden um neue Komponenten hinzuzufügen
	public void addComponent(String text, int selection) {
		QuestionElement qEle = new QuestionElement(text, selection);
		qEle.addQuestionElementListener(this);
		elements.add(qEle);
		questionHolder.add(qEle);
		questionHolder.updateUI();
	}
	
	public void removeComponent(QuestionElement qe) {
		elements.remove(qe);
		questionHolder.remove(qe);
		questionHolder.updateUI();
	}

	@Override
	public void questionElementClosed(QuestionElementEvent e) {
		removeComponent(e.getQe());
		
	}
}
