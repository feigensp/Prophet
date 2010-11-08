/**
 * Erstellt ein Panel, welchem die einzelnen Wörter der caption als einzelne JLabels hinzugeüfgt werden.
 */

package questionView;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordWrapLabel extends JPanel {

	String caption;

	/**
	 * Standartkonstruktor, welcher ein Panel mit einem Text (JLabels) erstellt
	 * 
	 * @param s
	 *            Text welcher dargestellt werden soll
	 */
	public WordWrapLabel(String s) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		//this.add(new JLabel("<html>" + s + "</html>"), BorderLayout.CENTER);
		/**
		 * langer Text:
		 * Ich bin nun ein ganz langer Text. Geboren wurde ich im Jahre  87 vor christus. Mein Vater war das Alte Testament und meine Mutter war ein Buch Von den elementen. Als Pythagoras mich das erste mal sah, wunderte er sich, dass er noch lebte und noch nicht tot war. Mich selber irritierte das nciht so sehr wie ihn, denn ich wusste das nicht alles sinn ergeben muss. Dennoch wunderte es auch mich ein wenig. Ein paar Sekunden starb er wieder und wurde nie mehr gesehen.
		 */
		setText(s);
	}

	/**
	 * Setzt den Text (JLabel's) des Panels neu
	 * 
	 * @param s
	 *            Text, welcher gesetzt werden soll
	 */
	public void setText(String s) {
		this.removeAll();
		caption = s;
		String[] captionElements = s.split(" ");
		for (String labelCaption : captionElements) {
			add(new JLabel(labelCaption + " "));
		}

//		ArrayList<JLabel> labels = new ArrayList<JLabel>();
//		double panelWidth = this.getSize().getWidth();
//		JLabel label = new JLabel("");
//		String part1 = s;
//		String part2 = "";
//		if()
//			do {
//				
//				//Solange durchführen, wie LabelText zu groß, oder der 2. Teil nicht leer 
//			} while (panelWidth < label.getGraphics().getFontMetrics()
//					.getStringBounds(part1, 0, part1.length(), getGraphics())
//					.getWidth()
//					|| !part2.equals(""));
//			int whitespacePos = s.lastIndexOf(" ");
	}

	/**
	 * Liefert den gesamten Text des Panels zurück
	 * 
	 * @return Text der einzelnen JLabels
	 */
	public String getText() {
		return caption;
	}

}
