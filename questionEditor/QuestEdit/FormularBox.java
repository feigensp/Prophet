package questionEditor.QuestEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import questionEditor.EditArea;

public class FormularBox extends JComboBox implements ActionListener {

	private EditArea editArea;
	private ArrayList<String> forms;

	public FormularBox(EditArea editArea) {
		super();
		this.editArea = editArea;
		forms = new ArrayList<String>();
		forms.add("Textfeld"); // index 1

		this.addItem("Formulare");
		for (int i = 0; i < forms.size(); i++) {
			this.addItem(forms.get(i));
		}
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		switch (this.getSelectedIndex()) {
		case 0:
			return;
		case 1:
			String name = JOptionPane.showInputDialog(this,
					"Name des Textfeldes:", "Textfeld", 1);
			if (name != null)
				editArea.replaceSelection("<input type=\"text\" name=\"" + name
						+ "\">");
			break;
		}
		this.setSelectedIndex(0);
	}

}