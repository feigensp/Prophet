package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;

/**
 * Contains methods to show a dialog that requests user input for a name and values (separated by line-breaks).
 */
public class MultilineDialog {

    private MultilineDialog() {}

    private static final int DEFAULT_COLUMNS = 30;
    private static final int DEFAULT_ROWS = 10;

    /**
     * Shows a non-modal <code>MultilineDialog</code> with the given title. <code>null</code> will be returned if the
     * user cancels the input in some way.
     *
     * @param title
     *         the title for the dialog
     *
     * @return a <code>Pair</code> where the key is the name the user input and the value the lines of the
     * <code>JTextArea</code> or <code>null</code>
     */
    public static Pair<String, List<String>> showMultiDialog(String title) {
        return showMultiDialog(null, title);
    }

    /**
     * Shows a modal <code>MultilineDialog</code> with the given parent and title. <code>null</code> will be returned
     * if
     * the user cancels the input in some way.
     *
     * @param parent
     *         the parent of the dialog
     * @param title
     *         the title for the dialog
     *
     * @return a <code>Pair</code> where the key is the name the user input and the value the lines of the
     * <code>JTextArea</code> or <code>null</code>
     */
    public static Pair<String, List<String>> showMultiDialog(Component parent, String title) {
        return showMultiDialog(parent, title, DEFAULT_COLUMNS, DEFAULT_ROWS);
    }

    /**
     * Shows a modal <code>MultilineDialog</code> with the given parent and title. The <code>JTextArea</code> will
     * have the given columns and rows. <code>null</code> will be returned if the user cancels the input in some way.
     *
     * @param parent
     *         the parent of the dialog
     * @param title
     *         the title for the dialog
     * @param columns
     *         the columns for the <code>JTextArea</code>
     * @param rows
     *         the rows for the <code>JTextArea</code>
     *
     * @return a <code>Pair</code> where the key is the name the user input and the value the lines of the
     * <code>JTextArea</code> or <code>null</code>
     */
    public static Pair<String, List<String>> showMultiDialog(Component parent, String title, int columns, int rows) {
        JPanel content = new JPanel();
        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel(UIElementNames.getLocalized("MULTILINEDIALOG_NAME"));
        JTextField nameTextField = new JTextField();
        JTextArea textInputArea = new JTextArea();

        content.setLayout(new BorderLayout());
        content.add(namePanel, BorderLayout.NORTH);
        content.add(new JScrollPane(textInputArea), BorderLayout.CENTER);

        textInputArea.setColumns(columns);
        textInputArea.setRows(rows);

        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalStrut(5));
        namePanel.add(nameTextField);
        namePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        int result = JOptionPane.showConfirmDialog(parent, content, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String key = nameTextField.getText().trim();
            Stream<String> lines = Arrays.stream(textInputArea.getText().split("\\n")).map(String::trim);
            List<String> value = lines.collect(Collectors.toList());

            return Pair.of(key, value);
        } else {
            return null;
        }
    }
}
