/**
 * Diese Klasse stellt ein Panel dar, in welcher Elemente nach dem BoxLayout angeordnet werden (horizontal).
 * Zudem kann eine Tabelle hinzugefügt werden, dies geschieht indem ein Panel mit FormLayout hinzugefügt wird.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelContainer extends JPanel {

	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL = false;

	private ArrayList<JComponent> components;

	public PanelContainer() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setAlignmentY(TOP_ALIGNMENT);
		components = new ArrayList<JComponent>();
	}

	public void addTable(Table t) {
		Table table = t;
		table.setAlignmentX(LEFT_ALIGNMENT);
		table.setAlignmentY(TOP_ALIGNMENT);
		this.add(table);
		components.add(table);
		updateUI();
	}

	public void addLabel(String caption) {
		JLabel label = new JLabel(caption);
		label.setAlignmentX(LEFT_ALIGNMENT);
		label.setAlignmentY(TOP_ALIGNMENT);
		this.add(label);
		components.add(label);
		updateUI();
	}

	public void addRadioButtons(String[] captions, boolean alignment) {
		RadioButtons buttons = new RadioButtons(captions, alignment);
		buttons.setAlignmentX(LEFT_ALIGNMENT);
		buttons.setAlignmentY(TOP_ALIGNMENT);
		this.add(buttons);
		components.add(buttons);
		updateUI();
	}

	public void addTextField(int columns) {
		JTextField textField = new JTextField("", columns);
		textField.setMaximumSize(textField.getPreferredSize());
		textField.setAlignmentX(LEFT_ALIGNMENT);
		textField.setAlignmentY(TOP_ALIGNMENT);
		this.add(textField);
		components.add(textField);
		updateUI();
	}

	public void addDivider(int size, boolean alignment) {
		JPanel panel = new JPanel();
		if (alignment == HORIZONTAL) {
			panel.setPreferredSize(new Dimension(size, 1));
			panel.setMinimumSize(new Dimension(size, 1));
			panel.setMaximumSize(new Dimension(size, 1));
		} else {
			panel.setPreferredSize(new Dimension(1, size));
			panel.setMinimumSize(new Dimension(1, size));
			panel.setMaximumSize(new Dimension(1, size));
		}
		this.add(panel);
		components.add(panel);
		updateUI();
	}
	
	public boolean removeComponent(int index) {
		if(index < components.size()) {
			//Element aus der KomponentenListe löschen
			components.remove(index);
			//Panel neu aufbauen
			this.removeAll();
			for(int i=0; i<components.size(); i++) {
				this.add(components.get(i));
			}
			updateUI();
			return true;
		}
		return false;
	}
}
