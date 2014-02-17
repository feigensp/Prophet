package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;

/**
 * Box for the ContentEditorToolBar, adding HTML tags to change font styles
 * @author Andreas Hasselberg
 * @author Markus K�ppen
 *
 */
@SuppressWarnings("serial")
public class FontStyleBox extends JComboBox implements ActionListener{

	private RSyntaxTextArea editArea;
	private ArrayList<Pair<String, String>> fontFace;

	public FontStyleBox(RSyntaxTextArea editArea) {
		super();
		this.editArea = editArea;
		fontFace = new ArrayList<Pair<String, String>>();
		fontFace.add(new Pair<String, String>(UIElementNames.FONT_FACE_BOLD, "b"));
		fontFace.add(new Pair<String, String>(UIElementNames.FONT_FACE_ITALIC, "i"));
		fontFace.add(new Pair<String, String>(UIElementNames.FONT_FACE_UNDERLINE, "u"));

		this.addItem(UIElementNames.MENU_TAB_EDITOR_FONT_FACE);
		for(int i=0; i<fontFace.size(); i++) {
			this.addItem(fontFace.get(i).getKey());
		}
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		if (this.getSelectedIndex() != 0) {
			String tag = fontFace.get(this.getSelectedIndex()-1).getValue();
			String text = editArea.getSelectedText();
			text = text==null ? "" : text;
			editArea.replaceSelection("<"+tag+">"+text+"</"+tag+">");
			this.setSelectedIndex(0);
		}
	}

}
