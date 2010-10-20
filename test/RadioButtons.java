package test;
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


public class RadioButtons extends JPanel{
	
	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL = false;
	
	private String[] captions;
	private boolean alignment;
	
	/**
	 * Konstruktor welcher aus einem StringArray mehrere RadioButtons erstellt,
	 * welche dem Panel hinzugefügt werden.
	 * Desweiteren kann die Ausrichtung angegeben werden.
	 * 
	 * @param names Die einzelnen Strings werden zu neuen RadioButtons
	 * @param alignment Horizontal wenn true, Vertikal wenn false
	 */
	public RadioButtons(String[] captions, boolean alignment) {
		this.captions = captions;
		this.alignment = alignment;
		ButtonGroup btnGroup = new ButtonGroup();
		
		if(alignment == HORIZONTAL) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		} else {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));			
		}
		for(int i = 0; i<captions.length; i++) {
			JRadioButton radioBtn = new JRadioButton(captions[i]); 
			this.add(radioBtn);
			btnGroup.add(radioBtn);
			radioBtn.setAlignmentX(LEFT_ALIGNMENT);
			radioBtn.setAlignmentY(TOP_ALIGNMENT);
		}
	}
}
