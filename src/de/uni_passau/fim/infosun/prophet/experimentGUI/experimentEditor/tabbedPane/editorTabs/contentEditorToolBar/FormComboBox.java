package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * <code>JComboBox</code> for the <code>ContentEditorToolBar</code> to enable adding HTML forms.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class FormComboBox extends JComboBox<String> implements ActionListener {

    private RSyntaxTextArea textArea;

    /**
     * Constructs a new <code>FormComboBox</code> that adds its forms to the given <code>RSyntaxTextArea</code>.
     *
     * @param textArea
     *         the <code>RSyntaxTextArea</code> this <code>FormComboBox</code> affects
     */
    public FormComboBox(RSyntaxTextArea textArea) {
        List<String> forms = new ArrayList<>();

        this.textArea = textArea;

        forms.add(UIElementNames.HTML_TEXT_FIELD);  // index 1
        forms.add(UIElementNames.HTML_TEXT_AREA);   // index 2
        forms.add(UIElementNames.HTML_LIST);        // index 3
        forms.add(UIElementNames.HTML_COMBO_BOX);   // index 4
        forms.add(UIElementNames.HTML_RADIO_BUTTON);// index 5
        forms.add(UIElementNames.HTML_CHECK_BOX);   // index 6

        addItem(UIElementNames.MENU_TAB_EDITOR_FORMS);
        forms.forEach(this::addItem);

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        switch (getSelectedIndex()) {
            case 0:
                return;
            case 1: // TextField
                insertTextField();
                break;
            case 2: // TextArea
                insertTextArea();
                break;
            case 3: // List
                insertList();
                break;
            case 4: // ComboBox
                insertComboBox();
                break;
            case 5: // RadioButton
                insertRadioButton();
                break;
            case 6: // CheckBox
                insertCheckBox();
                break;
        }

        setSelectedIndex(0);
    }

    /**
     * Inserts a HTML CheckBox replacing the current selection in the <code>textArea</code>.
     */
    private void insertCheckBox() {
        String formatString;
        String lineSep = System.getProperty("line.separator");
        Pair<String, String> checkInfo =
                MultilineDialogs.showMultilineInputDialog(UIElementNames.DIALOG_DEFINE_LIST_INFORMATION);

        if (checkInfo == null) {
            return;
        }

        String[] checkEntries = checkInfo.getValue().split(lineSep);
        StringBuilder checks = new StringBuilder("");

        formatString = "<input type=\"checkbox\" name=\"%s\" id=\"%s\" value=\"%s\">%s<br>%n";
        for (String checkEntry : checkEntries) {
            checks.append(String.format(formatString, checkInfo.getKey(), checkInfo.getKey(), checkEntry, checkEntry));
        }

        textArea.replaceSelection(checks.toString());
    }

    /**
     * Inserts HTML RadioButton items replacing the current selection in the <code>textArea</code>.
     */
    private void insertRadioButton() {
        String formatString;
        String lineSep = System.getProperty("line.separator");
        Pair<String, String> radioInfo =
                MultilineDialogs.showMultilineInputDialog(UIElementNames.DIALOG_DEFINE_LIST_INFORMATION);

        if (radioInfo == null) {
            return;
        }

        String[] radioEntries = radioInfo.getValue().split(lineSep);
        StringBuilder radios = new StringBuilder("");

        formatString = "<input type=\"radio\" name=\"%s\" id=\"%s\" value=\"%s\">%s<br>%n";
        for (String radioEntry : radioEntries) {
            radios.append(String.format(formatString, radioInfo.getKey(), radioInfo.getKey(), radioEntry, radioEntry));
        }

        textArea.replaceSelection(radios.toString());
    }

    /**
     * Inserts a HTML ComboBox replacing the current selection in the <code>textArea</code>.
     */
    private void insertComboBox() {
        String formatString;
        String lineSep = System.getProperty("line.separator");
        Pair<String, String> comboInfo =
                MultilineDialogs.showMultilineInputDialog(UIElementNames.DIALOG_DEFINE_LIST_INFORMATION);

        if (comboInfo == null) {
            return;
        }

        String[] comboEntries = comboInfo.getValue().split(lineSep);
        StringBuilder combos = new StringBuilder("");

        for (String comboEntry : comboEntries) {
            combos.append(String.format("%n<option value=\"%s\">%s</option>", comboEntry, comboEntry));
        }

        formatString = "<select name=\"%s\" id=\"%s\">%s%n</select>";
        textArea.replaceSelection(
                String.format(formatString, comboInfo.getKey(), comboInfo.getKey(), combos.toString()));
    }

    /**
     * Inserts HTML List items replacing the current selection in the <code>textArea</code>.
     */
    private void insertList() {
        String formatString;
        String lineSep = System.getProperty("line.separator");
        Pair<String, String> listInfo =
                MultilineDialogs.showMultilineInputDialog(UIElementNames.DIALOG_DEFINE_LIST_INFORMATION);

        if (listInfo == null) {
            return;
        }

        String[] listEntries = listInfo.getValue().split(lineSep);
        StringBuilder list = new StringBuilder("");

        for (String listEntry : listEntries) {
            list.append(String.format("%n<option value=\"%s\">%s</option>", listEntry, listEntry));
        }

        formatString = "<select name=\"%s\" id=\"%s\" size=\"3\" multiple>%s%n</select>";
        textArea.replaceSelection(String.format(formatString, listInfo.getKey(), listInfo.getKey(), list.toString()));
    }

    /**
     * Inserts a HTML TextArea replacing the current selection in the <code>textArea</code>.
     */
    private void insertTextArea() {
        String formatString;
        String textAreaName = JOptionPane
                .showInputDialog(this, UIElementNames.DIALOG_DEFINE_TEXT_AREA + ":", UIElementNames.HTML_TEXT_AREA,
                        JOptionPane.QUESTION_MESSAGE);

        if (textAreaName == null) {
            return;
        }

        formatString = "<textarea name=\"%s\" id=\"%s\" cols=\"50\" rows=\"10\"></textarea>";
        textArea.replaceSelection(String.format(formatString, textAreaName, textAreaName));
    }

    /**
     * Inserts a HTML TextField replacing the current selection in the <code>textArea</code>.
     */
    private void insertTextField() {
        String formatString;
        String textFieldName = JOptionPane
                .showInputDialog(null, UIElementNames.DIALOG_DEFINE_TEXT_FIELD + ":", UIElementNames.HTML_TEXT_FIELD,
                        JOptionPane.QUESTION_MESSAGE);

        if (textFieldName == null) {
            return;
        }

        formatString = "<input type=\"text\" name=\"%s\" id=\"%s\">";
        textArea.replaceSelection(String.format(formatString, textFieldName, textFieldName));
    }
}
