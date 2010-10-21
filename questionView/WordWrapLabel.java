/**
 * Erstellt ein Panel, welchem die einzelnen Wörter der caption als einzelne JLabels hinzugeüfgt werden.
 */

package questionView;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordWrapLabel extends JPanel {

	String caption;

	/**
	 * Standartkonstruktor, welcher ein Panel mit einem Text (JLabels) erstellt
	 * @param s Text welcher dargestellt werden soll
	 */
	public WordWrapLabel(String s) {
		super();
		//this.setLayout(new BorderLayout());
		setText(s);
	}

	/**
	 * Setzt den Text (JLabel's) des Panels neu
	 * @param s Text, welcher gesetzt werden soll
	 */
	public void setText(String s) {
		this.removeAll();
		caption = s;
		String[] captionElements = s.split(" ");
		for (String labelCaption : captionElements) {
			add(new JLabel(labelCaption));
		}
	}

	/**
	 * Liefert den gesamten Text des Panels zurück
	 * @return Text der einzelnen JLabels
	 */
	public String getText() {
		return caption;
	}

}
