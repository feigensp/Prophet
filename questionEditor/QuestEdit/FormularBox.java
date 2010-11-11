package questionEditor.QuestEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import questionEditor.EditArea;
import util.StringTupel;

public class FormularBox extends JComboBox implements ActionListener {

	private EditArea editArea;
	private ArrayList<String> forms;

	public FormularBox(EditArea editArea) {
		super();
		this.editArea = editArea;
		forms = new ArrayList<String>();
		forms.add("Textfeld"); // index 1
		forms.add("TextArea"); // index 2
		forms.add("Liste"); // index 3
		forms.add("Combobox"); // index 4
		forms.add("Radiobutton"); // index 5
		forms.add("Checkboxen"); //index 6

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
		case 1: // texfield
			String textFieldName = JOptionPane.showInputDialog(this,
					"Name des Textfeldes:", "Textfeld", 1);
			if (textFieldName != null) {
				editArea.replaceSelection("<input type=\"text\" name=\""
						+ textFieldName + "\">");
			}
			break;
		case 2: // textarea
			String textAreaName = JOptionPane.showInputDialog(this,
					"Name des Textareas:", "Textarea", 1);
			if (textAreaName != null) {
				editArea.replaceSelection("<textarea name=\"" + textAreaName
						+ "\" cols=\"50\" rows=\"10\">");
			}
			break;
		case 3: // liste
			StringTupel listInfos = MultilineDialogs
					.showMultilineInputDialog("Listeninformationen");
			if (listInfos != null) {
				String[] listEntrys = listInfos.getValue().split(
						System.getProperty("line.separator"));
				String list = "";
				for (int i = 0; i < listEntrys.length; i++) {
					list += System.getProperty("line.separator")
							+ "<option value=\"" + listEntrys[i] + "\">"
							+ listEntrys[i] + "</option>";
				}
				editArea.replaceSelection("<select name=\""
						+ listInfos.getKey() + "\" size=\"3\" multiple>" + list
						+ System.getProperty("line.separator") + "</select>");
			}
			break;
		case 4: // Combobox
			StringTupel comboInfos = MultilineDialogs
					.showMultilineInputDialog("Listeninformationen");
			if (comboInfos != null) {
				String[] comboEntrys = comboInfos.getValue().split(
						System.getProperty("line.separator"));
				String combos = "";
				for (int i = 0; i < comboEntrys.length; i++) {
					combos += System.getProperty("line.separator")
							+ "<option value=\"" + comboEntrys[i] + "\">"
							+ comboEntrys[i] + "</option>";
				}
				editArea.replaceSelection("<select name=\""
						+ comboInfos.getKey() + "\">" + combos
						+ System.getProperty("line.separator") + "</select>");
			}
			break;
		case 5: // RadioButton
			StringTupel radioInfos = MultilineDialogs
					.showMultilineInputDialog("Listeninformationen");
			if (radioInfos != null) {
				String[] radioEntrys = radioInfos.getValue().split(
						System.getProperty("line.separator"));
				String radios = "";
				for (int i = 0; i < radioEntrys.length; i++) {
					radios += "<input type=\"radio\" name=\""
							+ radioInfos.getKey() + "\" value=\""
							+ radioEntrys[i] + "\">" + radioEntrys[i] + "<br>"
							+ System.getProperty("line.separator");
				}
				editArea.replaceSelection(radios);
			}
			break;
		case 6: // CheckBox
			StringTupel checkInfos = MultilineDialogs
					.showMultilineInputDialog("Listeninformationen");
			if (checkInfos != null) {
				String[] checkEntrys = checkInfos.getValue().split(
						System.getProperty("line.separator"));
				String checks = "";
				for (int i = 0; i < checkEntrys.length; i++) {
					checks += "<input type=\"checkbox\" name=\""
							+ checkInfos.getKey() + "\" value=\""
							+ checkEntrys[i] + "\">" + checkEntrys[i] + "<br>"
							+ System.getProperty("line.separator");
				}
				editArea.replaceSelection(checks);
			}
			break;
		}
		this.setSelectedIndex(0);
	}

}