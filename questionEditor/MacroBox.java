package questionEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextPane;

public class MacroBox extends JComboBox implements ActionListener {

	public static final String TABLEROWDARK5RADIO = "Tabellenzeile (8 Radio - dunkel)";
	public static final String TABLEROWLIGHT5RADIO = "Tabellenzeile (8 Radio - hell)";

	JTextPane textPane;

	public MacroBox(JTextPane textPane) {
		super();

		this.textPane = textPane;

		this.addItem("Makroauswahl");
		this.addItem(TABLEROWDARK5RADIO);
		this.addItem(TABLEROWLIGHT5RADIO);
		//Shortcuts an das Textfeld setzen
		
		this.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String selectedText = textPane.getSelectedText() == null? "" : textPane.getSelectedText();
		String replacement = "";
		switch (this.getSelectedIndex()) {
		case 1:
			textPane.replaceSelection("yay");
			this.setSelectedIndex(0);
			break;
		case 2:
			break;
		}
	}
}
