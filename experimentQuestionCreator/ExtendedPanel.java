/**
 * Extended Panel dient zur Handhabungshilfe im CardLayout
 * Neben dem eigentlichen Panel enthält es auch eine ID-Verwaltung für das Layout
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ExtendedPanel extends JPanel implements QuestionElementListener {

	private int id; // ID dieses ExtendedPanel
	private String name; // Überschrift für Frage die das Panel repräsentiert

	private JLabel headline;
	// Panel welches alle Komponenten der Frage beinhalten wird
	private JPanel questionHolder;
	private BoxLayout boxLayout;

	// statische ArrayList, welche die Ids aller ExtendedPanels beherbergt
	static ArrayList<Integer> ids = new ArrayList<Integer>();

	// Liste mit allen QuestionElementen auf diesem Panel
	private LinkedList<QuestionElement> elements = new LinkedList<QuestionElement>();

	private static final Font headlineFont = new Font("monospaced", Font.BOLD,
			14);

	/**
	 * Konstruktor der das ExtendedPanel initialisiert
	 * 
	 * @param id
	 *            Die Id, welche das Panel im CardLayout haben wird
	 * @param name
	 *            Überschrift des Panel
	 */
	public ExtendedPanel(int id, String name) {
		super();

		ids.add(id);
		this.id = id;
		this.name = name;

		initialise();
	}

	/**
	 * Initialisiert das Start-Layout
	 */
	private void initialise() {
		setLayout(new BorderLayout());

		JPanel headlinePanel = new JPanel();
		headlinePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		headline = new JLabel(name);
		headline.setFont(headlineFont);
		headlinePanel.add(headline);
		add(headlinePanel, BorderLayout.NORTH);

		questionHolder = new JPanel();
		add(new JScrollPane(questionHolder), BorderLayout.CENTER);
		boxLayout = new BoxLayout(questionHolder, BoxLayout.Y_AXIS);
		questionHolder.setLayout(boxLayout);
	}

	/**
	 * Fügt eine neue Komponente mit Standartgröße hinzu
	 * 
	 * @param text
	 *            Inhalt/Beschriftung
	 * @param selection
	 *            Komponententyp
	 */
	public void addComponent(String text, int selection) {
		QuestionElement qEle = new QuestionElement(text, selection);
		qEle.addQuestionElementListener(this);
		elements.add(qEle);
		qEle.setAlignmentX(Component.LEFT_ALIGNMENT);
		questionHolder.add(qEle);
		questionHolder.updateUI();
	}

	/**
	 * Fügt eine neue Komponente mit spezieller Größe hinzu
	 * 
	 * @param text
	 *            Inhalt/Beschriftung
	 * @param selection
	 *            Komponententyp
	 * @param size
	 *            Größe
	 */
	public void addComponent(String text, int selection, Dimension size) {
		QuestionElement qEle = new QuestionElement(text, selection, size);
		qEle.addQuestionElementListener(this);
		elements.add(qEle);
		qEle.setAlignmentX(Component.LEFT_ALIGNMENT);
		questionHolder.add(qEle);
		questionHolder.updateUI();
	}

	/**
	 * Löscht eine Komponente
	 * 
	 * @param qe
	 *            Komponente, welche entfernt werden soll
	 */
	public void removeComponent(QuestionElement qe) {
		elements.remove(qe);
		questionHolder.remove(qe);
		questionHolder.updateUI();
	}

	/**
	 * Ordnet die Elemente nach der Reihenfolge in der Liste an und aktualisiert
	 * damit die Sicht
	 */
	public void updateView() {
		for (QuestionElement ele : elements) {
			questionHolder.remove(ele);
		}
		for (QuestionElement ele : elements) {
			questionHolder.add(ele);
		}
		questionHolder.updateUI();
	}

	/**
	 * Gibt die nächste freie Id zurück
	 * 
	 * @return Id, welche frei ist
	 */
	public static int nextFreeId() {
		int i = 0;
		while (ids.contains(i)) {
			i++;
		}
		return i;
	}

	/**
	 * Löscht eine Id
	 */
	public void removeId() {
		ids.remove((Integer) id);
	}

	/**
	 * Löscht alle Ids
	 */
	public static void removeIds() {
		ids.clear();
	}

	/**
	 * Gibt die Id dieses ExtendedPanel zurück
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gibt die einzelnen Komponenten dieser Frage zurück
	 * 
	 * @return LinkedList, welche die Fragekomponenten enthält
	 */
	public LinkedList<QuestionElement> getElements() {
		return elements;
	}

	/**
	 * Setzt eine neue Überschrift
	 */
	public void setName(String name) {
		this.name = name;
		headline.setText(name);
	}
	
	/**
	 * Listener für die QuestionElements
	 */
	@Override
	public void questionElementClose(QuestionElementEvent e) {
		removeComponent(e.getQuestionElement());
	}

	@Override
	public void questionElementUp(QuestionElementEvent e) {
		int i = elements.indexOf(e.getQuestionElement());
		if (i - 1 >= 0) {
			elements.remove(i);
			elements.add(i - 1, e.getQuestionElement());
			updateView();
		}
	}

	@Override
	public void questionElementDown(QuestionElementEvent e) {
		int i = elements.indexOf(e.getQuestionElement());
		if (i + 1 < elements.size()) {
			elements.remove(i);
			elements.add(i + 1, e.getQuestionElement());
			updateView();
		}
	}
}
