package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;

/**
 * Contains methods to show a dialog that requests user input tabular data.
 */
public class TableDialog {

    private TableDialog() {}

    /**
     * Shows a non modal <code>TableDialog</code> with the given <code>title</code>.
     *
     * @param title
     *         the title for the dialog
     * @return a <code>Pair</code> where the key is the name the user input and the value the table of
     *         <code>String</code>s
     */
    public static Pair<String, String[][]> showTableDialog(String title) {
        return showTableDialog(null, title);
    }

    /**
     * Shows a modal <code>TableDialog</code> with the given <code>parent</code> and <code>title</code>.
     *
     * @param parent
     *         the parent of the dialog
     * @param title
     *         the title for the dialog
     * @return a <code>Pair</code> where the key is the name the user input and the value the table of
     *         <code>String</code>s
     */
    public static Pair<String, String[][]> showTableDialog(Component parent, String title) {
        final JTextField[][][] table = {new JTextField[0][0]};
        JPanel content = new JPanel();

        JPanel tablePanel = new JPanel();
        GroupLayout tableLayout = new GroupLayout(tablePanel);
        tablePanel.setLayout(tableLayout);

        JPanel configPanel = new JPanel();

        JLabel nameLabel = new JLabel(UIElementNames.MULTILINEDIALOG_NAME);
        JTextField nameTextField = new JTextField();

        SpinnerNumberModel colModel = new SpinnerNumberModel();
        colModel.setMinimum(1);
        colModel.setMaximum(100);
        colModel.setStepSize(1);

        SpinnerNumberModel rowModel = new SpinnerNumberModel();
        rowModel.setMinimum(1);
        rowModel.setMaximum(100);
        rowModel.setStepSize(1);

        JSpinner columns = new JSpinner(colModel);
        JSpinner rows = new JSpinner(rowModel);

        ChangeListener listener = event -> {
            int numRows = rowModel.getNumber().intValue();
            int numCols = colModel.getNumber().intValue();

            if (numRows == 0 || numCols == 0) {
                return;
            }

            JTextField[][] newTable = new JTextField[numRows][numCols];

            for (int y = 0; y < newTable.length; y++) {
                for (int x = 0; x < newTable[0].length; x++) {

                    JTextField newTextField;
                    if (y < table[0].length && x < table[0][0].length) {
                        newTextField = table[0][y][x];
                    } else {
                        newTextField = new JTextField();
                    }

                    newTextField.setColumns(10);
                    newTable[y][x] = newTextField;
                }
            }

            tablePanel.removeAll();

            GroupLayout.SequentialGroup xGroup = tableLayout.createSequentialGroup();
            GroupLayout.SequentialGroup yGroup = tableLayout.createSequentialGroup();

            for (JTextField[] row : newTable) {
                GroupLayout.ParallelGroup parallelGroup = tableLayout.createParallelGroup();

                for (int x = 0; x < newTable[0].length; x++) {
                    parallelGroup.addComponent(row[x], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE);
                }
                yGroup.addGroup(parallelGroup);
            }

            for (int x = 0; x < newTable[0].length; x++) {
                GroupLayout.ParallelGroup parallelGroup = tableLayout.createParallelGroup();

                for (JTextField[] row : newTable) {
                    parallelGroup.addComponent(row[x]);
                }
                xGroup.addGroup(parallelGroup);
            }

            tableLayout.setHorizontalGroup(xGroup);
            tableLayout.setVerticalGroup(yGroup);

            table[0] = newTable;
            tablePanel.setPreferredSize(tableLayout.preferredLayoutSize(tablePanel));

            Window window = SwingUtilities.windowForComponent(content);
            if (window != null) {
                window.pack();
            }
        };

        colModel.addChangeListener(listener);
        rowModel.addChangeListener(listener);

        colModel.setValue(3);
        rowModel.setValue(3);

        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.LINE_AXIS));
        configPanel.add(nameLabel);
        configPanel.add(Box.createHorizontalStrut(5));
        configPanel.add(nameTextField);
        configPanel.add(Box.createHorizontalStrut(10));
        configPanel.add(columns);
        configPanel.add(Box.createHorizontalStrut(5));
        configPanel.add(new JLabel("x"));
        configPanel.add(Box.createHorizontalStrut(5));
        configPanel.add(rows);
        configPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        content.setLayout(new BorderLayout());
        content.add(configPanel, BorderLayout.NORTH);
        content.add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        tablePanel.setPreferredSize(tableLayout.preferredLayoutSize(tablePanel));

        int result = JOptionPane.showConfirmDialog(parent, content, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String key = nameTextField.getText().trim();
            String[][] value = new String[table[0].length][table[0][0].length];

            for (int y = 0; y < table[0].length; y++) {
                for (int x = 0; x < table[0][0].length; x++) {
                    value[y][x] = table[0][y][x].getText();
                }
            }

            return new Pair<>(key, value);
        } else {
            return null;
        }
    }
}
