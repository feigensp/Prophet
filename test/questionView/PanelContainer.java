/**
 * Diese Klasse stellt ein Panel dar, in welcher Elemente nach dem BoxLayout angeordnet werden (horizontal).
 * Zudem kann eine Tabelle hinzugefügt werden, dies geschieht indem ein Panel mit FormLayout hinzugefügt wird.
 * Es werden spezielle "add-Methoden" bereitgestellt um Elemente Formatiert hinzuzufügen
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test.questionView;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelContainer extends JPanel {

	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL = false;

	private ArrayList<JComponent> components;

	/**
	 * Standartkonstruktor, es wird ein leeres Panel mit horizontalem BoxLayout
	 * erstellt.
	 */
	public PanelContainer() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setAlignmentY(TOP_ALIGNMENT);
//		this.setPreferredSize(new Dimension(300, 300));
//		this.setMinimumSize(new Dimension(300, 300));
//		this.setMaximumSize(new Dimension(300, 300));
		components = new ArrayList<JComponent>();
	}

	/**
	 * Löscht eine Komponente an einem Index Ist dieser zu groß wird false
	 * zurückgegeben
	 * 
	 * @param index
	 *            Index der Komponente die gelöscht werden soll
	 * @return true wenn Löschung vorgenommen, false wenn nicht
	 */
	public boolean removeComponent(int index) {
		if (index < components.size()) {
			components.remove(index);
			this.remove(index);
			updateUI();
			return true;
		}
		return false;
	}

	/**
	 * Gibt die Anzahl der Komponenten im PanelContainer zurück
	 * 
	 * @return Anzahl der Komponenten
	 */
	public int getComponentCount() {
		return components.size();
	}

	/**
	 * Fügt eine Komponente an der Position i ein
	 * 
	 * @param comp
	 *            Komponente die eingefügt werden soll
	 * @param i
	 *            Einfügeposition (indexbasiert)
	 */
	public void addComponent(JComponent comp, int i) {
		components.add(i, comp);
	}

	/**
	 * Fügt eine Komponente am Ende an
	 * 
	 * @param comp
	 *            Komponente die eingefügt werden soll
	 */
	public void addComponent(JComponent comp) {
		components.add(comp);
	}
}
