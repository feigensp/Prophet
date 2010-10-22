package questionView;

/**
 * Erstellt ein Panel mit RadioButtons.
 * Diese Klasse stellt Methoden bereit um z.B. auf die einzelnen Buttons zugreifen zu können.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RadioButtons extends JPanel {

	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL = false;

	private JRadioButton[] buttons;

	/**
	 * Konstruktor welcher aus einem StringArray mehrere RadioButtons erstellt,
	 * welche dem Panel hinzugefügt werden. Desweiteren kann die Ausrichtung
	 * angegeben werden.
	 * 
	 * @param names
	 *            Die einzelnen Strings werden zu neuen RadioButtons
	 * @param alignment
	 *            Horizontal wenn true, Vertikal wenn false
	 */
	public RadioButtons(String[] captions, boolean alignment) {
		ButtonGroup btnGroup = new ButtonGroup();
		buttons = new JRadioButton[captions.length];

		if (alignment == HORIZONTAL) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		} else {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
		for (int i = 0; i < captions.length; i++) {
			buttons[i] = new JRadioButton(captions[i]);
			this.add(buttons[i]);
			btnGroup.add(buttons[i]);
			buttons[i].setAlignmentX(LEFT_ALIGNMENT);
			buttons[i].setAlignmentY(TOP_ALIGNMENT);
		}
	}
	
	public RadioButtons(String[] captions) {
		ButtonGroup btnGroup = new ButtonGroup();
		buttons = new JRadioButton[captions.length];
		
		for (int i = 0; i < captions.length; i++) {
			buttons[i] = new JRadioButton(captions[i]);
			btnGroup.add(buttons[i]);
		}		
	}

	/**
	 * Liefert den Index zurück, welcher RadioButton markiert ist
	 * 
	 * @return Index oder -1 wenn keiner markiert ist.
	 */
	public int getSelectedIndex() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isSelected()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Liefert den Text vom markierten RadioButton zurück
	 * 
	 * @return Beschriftung des RadioButtons oder Leerstring wenn keiner
	 *         markiert ist
	 */
	public String getSelectedCaption() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isSelected()) {
				buttons[i].getText();
			}
		}
		return "";
	}
	
	public JRadioButton[] getRadioButtons() {
		return buttons;
	}
}
