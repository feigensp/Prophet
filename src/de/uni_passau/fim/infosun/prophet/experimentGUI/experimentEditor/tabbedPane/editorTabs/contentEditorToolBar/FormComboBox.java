package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * <code>JComboBox</code> for the <code>ContentEditorToolBar</code> to enable adding HTML forms.
 *
 * @author Georg Seibt
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

        forms.add(UIElementNames.get("HTML_TEXT_FIELD"));  // index 1
        forms.add(UIElementNames.get("HTML_TEXT_AREA"));   // index 2
        forms.add(UIElementNames.get("HTML_LIST"));        // index 3
        forms.add(UIElementNames.get("HTML_COMBO_BOX"));   // index 4
        forms.add(UIElementNames.get("HTML_RADIO_BUTTON"));// index 5
        forms.add(UIElementNames.get("HTML_CHECK_BOX"));   // index 6
        forms.add(UIElementNames.get("HTML_TABLE"));       // index 7

        addItem(UIElementNames.get("MENU_TAB_EDITOR_FORMS"));
        forms.forEach(this::addItem);

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

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
                insertList(event);
                break;
            case 4: // ComboBox
                insertComboBox(event);
                break;
            case 5: // RadioButton
                insertRadioButton(event);
                break;
            case 6: // CheckBox
                insertCheckBox(event);
                break;
            case 7: // Table
                insertTable(event);
                break;
        }

        setSelectedIndex(0);
    }

    /**
     * Inserts a HTML Table replacing the current selection in the <code>textArea</code>.
     *
     * @param event
     *         the <code>ActionEvent</code> that caused the insertion
     */
    private void insertTable(ActionEvent event) {
        Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
        Pair<String, String[][]> tableInfo = TableDialog.showTableDialog(parent,
                UIElementNames.get("DIALOG_DEFINE_TABLE_INFORMATION"));

        if (tableInfo == null) {
            return;
        }

        String tableFormat = "<table name=\"%s\">%n%s</table>";
        String rowFormat = "  <tr>%n%s  </tr>%n";
        String tableHeaderFormat = "    <th>%s</th>%n";
        String tableDataFormat = "    <td>%s</td>%n";
        String formatString;

        StringBuilder rowBuilder = new StringBuilder();
        StringBuilder tableBuilder = new StringBuilder();

        String[][] table = tableInfo.getValue();

        for (int i = 0; i < table.length; i++) {
            rowBuilder.delete(0, rowBuilder.length());

            if (i == 0) {
                formatString = tableHeaderFormat;
            } else {
                formatString = tableDataFormat;
            }

            for (String s : table[i]) {
                rowBuilder.append(String.format(formatString, s));
            }
            tableBuilder.append(String.format(rowFormat, rowBuilder.toString()));
        }

        textArea.replaceSelection(String.format(tableFormat, tableInfo.getKey(), tableBuilder.toString()));
    }

    /**
     * Inserts a HTML CheckBox replacing the current selection in the <code>textArea</code>.
     *
     * @param event
     *         the <code>ActionEvent</code> that caused the insertion
     */
    private void insertCheckBox(ActionEvent event) {
        String formatString;
        Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
        Pair<String, List<String>> checkInfo = MultilineDialog
                .showMultiDialog(parent, UIElementNames.get("DIALOG_DEFINE_LIST_INFORMATION"));

        if (checkInfo == null) {
            return;
        }

        StringBuilder checks = new StringBuilder();

        formatString = "<input type=\"checkbox\" name=\"%s\" value=\"%s\">%s<br>%n";
        for (String checkEntry : checkInfo.getValue()) {
            checks.append(String.format(formatString, checkInfo.getKey(), checkEntry, checkEntry));
        }

        textArea.replaceSelection(checks.toString());
    }

    /**
     * Inserts HTML RadioButton items replacing the current selection in the <code>textArea</code>.
     *
     * @param event
     *         the <code>ActionEvent</code> that caused the insertion
     */
    private void insertRadioButton(ActionEvent event) {
        String formatString;
        Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
        Pair<String, List<String>> radioInfo = MultilineDialog
                .showMultiDialog(parent, UIElementNames.get("DIALOG_DEFINE_LIST_INFORMATION"));

        if (radioInfo == null) {
            return;
        }

        StringBuilder radios = new StringBuilder();

        formatString = "<input type=\"radio\" name=\"%s\" value=\"%s\">%s<br>%n";
        for (String radioEntry : radioInfo.getValue()) {
            radios.append(String.format(formatString, radioInfo.getKey(), radioEntry, radioEntry));
        }

        textArea.replaceSelection(radios.toString());
    }

    /**
     * Inserts a HTML ComboBox replacing the current selection in the <code>textArea</code>.
     *
     * @param event
     *         the <code>ActionEvent</code> that caused the insertion
     */
    private void insertComboBox(ActionEvent event) {
        String formatString;
        Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
        Pair<String, List<String>> comboInfo = MultilineDialog
                .showMultiDialog(parent, UIElementNames.get("DIALOG_DEFINE_LIST_INFORMATION"));

        if (comboInfo == null) {
            return;
        }

        StringBuilder combos = new StringBuilder();

        for (String comboEntry : comboInfo.getValue()) {
            combos.append(String.format("%n<option value=\"%s\">%s</option>", comboEntry, comboEntry));
        }

        formatString = "<select name=\"%s\">%s%n</select>";
        textArea.replaceSelection(String.format(formatString, comboInfo.getKey(), combos.toString()));
    }

    /**
     * Inserts HTML List items replacing the current selection in the <code>textArea</code>.
     *
     * @param event
     *         the <code>ActionEvent</code> that caused the insertion
     */
    private void insertList(ActionEvent event) {
        String formatString;
        Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
        Pair<String, List<String>> listInfo = MultilineDialog
                .showMultiDialog(parent, UIElementNames.get("DIALOG_DEFINE_LIST_INFORMATION"));

        if (listInfo == null) {
            return;
        }

        StringBuilder list = new StringBuilder();

        for (String listEntry : listInfo.getValue()) {
            list.append(String.format("%n<option value=\"%s\">%s</option>", listEntry, listEntry));
        }

        formatString = "<select name=\"%s\" size=\"3\" multiple>%s%n</select>";
        textArea.replaceSelection(String.format(formatString, listInfo.getKey(), list.toString()));
    }

    /**
     * Inserts a HTML TextArea replacing the current selection in the <code>textArea</code>.
     */
    private void insertTextArea() {
        String formatString;
        String textAreaName = JOptionPane
                .showInputDialog(this, UIElementNames.get("DIALOG_DEFINE_TEXT_AREA") + ":", UIElementNames.get("HTML_TEXT_AREA"),
                        JOptionPane.QUESTION_MESSAGE);

        if (textAreaName == null) {
            return;
        }

        formatString = "<textarea name=\"%s\" cols=\"50\" rows=\"10\"></textarea>";
        textArea.replaceSelection(String.format(formatString, textAreaName));
    }

    /**
     * Inserts a HTML TextField replacing the current selection in the <code>textArea</code>.
     */
    private void insertTextField() {
        String formatString;
        String textFieldName = JOptionPane
                .showInputDialog(null, UIElementNames.get("DIALOG_DEFINE_TEXT_FIELD") + ":", UIElementNames.get("HTML_TEXT_FIELD"),
                        JOptionPane.QUESTION_MESSAGE);

        if (textFieldName == null) {
            return;
        }

        formatString = "<input type=\"text\" name=\"%s\">";
        textArea.replaceSelection(String.format(formatString, textFieldName));
    }
}
