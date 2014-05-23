package de.uni_passau.fim.infosun.prophet.experimentGUI.util.miniEditors;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;

/**
 * An editor that enables the user to create, modify and delete macros stored in the macros.xml.
 * These macros are used by the ExperimentEditor when creating the HTML content for nodes.
 */
public class MacroEditor extends JFrame {

    private static final String FILENAME = "macros.xml";

    private File xmlFile;
    private XStream xStream;
    private JTextField nameField;
    private JTextField keyField;
    private JTextField textField;
    private DefaultListModel<Macro> model;
    private JList<Macro> macroJList;

    /**
     * Simple container class for the macro information. Used for serialisation and display purposes.
     */
    @XStreamAlias("macro")
    @XStreamConverter(value = ToAttributedValueConverter.class, strings = {"text"})
    private static class Macro {

        private String name;
        private String key;
        private String text;

        /**
         * Constructs a new <code>Macro</code> containing the given data.
         *
         * @param name
         *         the name for the macro
         * @param key
         *         the key used to trigger the macro
         * @param text
         *         the text the macro produces
         */
        private Macro(String name, String key, String text) {
            this.name = name;
            this.key = key;
            this.text = text;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Constructs and shows a new <code>MacroEd</code>.
     *
     * @param args
     *         command line arguments (ignored)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MacroEditor().setVisible(true));
    }

    /**
     * Constructs a new <code>MacroEd</code>.
     */
    public MacroEditor() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(400, 300));
        setTitle("MacroEditor");
        setJMenuBar(createMenuBar());
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    saveXMLFile();
                } catch (IOException e) {
                    System.err.println("Could not save the macros.xml file. " + e);
                    Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
                    JOptionPane.showMessageDialog(parent, UIElementNames.MESSAGE_SAVE_ERROR);
                }
            }
        });

        Container content = getContentPane();

        content.setLayout(new BorderLayout());
        content.add(createListView(), BorderLayout.CENTER);
        content.add(createEditPanel(), BorderLayout.SOUTH);

        createXStream();
        loadXMLFile();

        pack();
    }

    /**
     * Creates the <code>macroJList</code>.
     *
     * @return the <code>macroJList</code>
     */
    private JList<Macro> createListView() {
        macroJList = new JList<>();

        model = new DefaultListModel<>();

        macroJList.setModel(model);
        macroJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        macroJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        macroJList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        macroJList.addListSelectionListener(event -> {
            Macro selected = macroJList.getSelectedValue();

            if (selected != null) {
                nameField.setText(selected.name);
                keyField.setText(selected.key);
                textField.setText(selected.text);
            } else {
                nameField.setText(null);
                keyField.setText(null);
                textField.setText(null);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem(UIElementNames.MACRO_EDITOR_DELETE_MACRO);
        popupMenu.add(removeItem);
        removeItem.addActionListener(event -> model.remove(macroJList.getSelectedIndex()));

        macroJList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    show(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    show(e);
                }
            }

            private void show(MouseEvent e) {
                int closestIndex = macroJList.locationToIndex(e.getPoint());
                boolean clickedCell = macroJList.getCellBounds(closestIndex, closestIndex).contains(e.getPoint());

                if (clickedCell) {
                    macroJList.setSelectedIndex(closestIndex);
                    popupMenu.show(macroJList, e.getX(), e.getY());
                }
            }
        });

        return macroJList;
    }

    /**
     * Creates the panel containing the <code>JTextField</code> instances used to edit macros.
     *
     * @return the edit panel
     */
    private JPanel createEditPanel() {
        JPanel panel = new JPanel();
        JPanel nameKeyPanel = new JPanel();
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel keyLabel = new JLabel("Key:");
        keyField = new JTextField();
        JPanel textPanel = new JPanel();
        JLabel textLabel = new JLabel("Text:");
        textField = new JTextField();

        ActionListener newMacroListener = event -> {
            String name = nameField.getText();
            String key = keyField.getText();
            String text = textField.getText();

            if (macroValidate(name, key, text)) {
                Macro macro = macroJList.getSelectedValue();

                if (macro == null) {
                    macro = new Macro(name, key, text);
                    model.addElement(macro);
                    nameField.setText(null);
                    keyField.setText(null);
                    textField.setText(null);
                    nameField.requestFocus();
                } else {
                    macro.name = nameField.getText();
                    macro.key = keyField.getText();
                    macro.text = textField.getText();
                    model.set(model.indexOf(macro), macro);
                }
            } else {
                Component focusOwner = getFocusOwner();

                if (focusOwner.equals(textField)) {
                    nameField.requestFocus();
                } else {
                    focusOwner.transferFocus();
                }
            }
        };

        nameField.addActionListener(newMacroListener);
        keyField.addActionListener(newMacroListener);
        textField.addActionListener(newMacroListener);

        nameKeyPanel.setLayout(new BoxLayout(nameKeyPanel, BoxLayout.LINE_AXIS));
        nameKeyPanel.add(nameLabel);
        nameKeyPanel.add(Box.createHorizontalStrut(5));
        nameKeyPanel.add(nameField);
        nameKeyPanel.add(Box.createHorizontalStrut(5));
        nameKeyPanel.add(keyLabel);
        nameKeyPanel.add(Box.createHorizontalStrut(5));
        nameKeyPanel.add(keyField);

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
        textPanel.add(textLabel);
        textPanel.add(Box.createHorizontalStrut(5));
        textPanel.add(textField);

        panel.setLayout(new GridLayout(2, 1, 0, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(nameKeyPanel);
        panel.add(textPanel);

        return panel;
    }

    /**
     * Creates the <code>JMenuBar</code> of the <code>MacroEd</code>.
     *
     * @return the <code>JMenuBar</code>
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(UIElementNames.MENU_FILE);
        JMenuItem saveMenuItem = new JMenuItem(UIElementNames.MENU_FILE_SAVE);
        JMenuItem closeMenuItem = new JMenuItem(UIElementNames.MENU_FILE_QUIT);

        saveMenuItem.addActionListener(event -> {
            try {
                saveXMLFile();
            } catch (IOException e) {
                System.err.println("Could not save the macros.xml file. " + e);
                Component parent = SwingUtilities.getWindowAncestor(((Component) event.getSource()));
                JOptionPane.showMessageDialog(parent, UIElementNames.MESSAGE_SAVE_ERROR);
            }
        });

        menuBar.add(menu);
        menu.add(saveMenuItem);
        menu.add(closeMenuItem);

        return menuBar;
    }

    /**
     * Creates the <code>XStream</code> used for saving and loading the macros.xml.
     */
    private void createXStream() {
        xStream = new XStream();
        xStream.processAnnotations(Macro.class);
        xStream.alias("root", List.class);
    }

    /**
     * Saves the current macros to the macros.xml file.
     */
    private void saveXMLFile() throws IOException {
        xStream.toXML(Collections.list(model.elements()), new FileWriter(xmlFile));
    }

    /**
     * Loads the macros.xml file.
     */
    private void loadXMLFile() {
        xmlFile = new File(FILENAME);

        if (xmlFile.exists()) {
            Collection<Macro> macros = (Collection<Macro>) xStream.fromXML(xmlFile);
            macros.forEach(model::addElement);
        }
    }

    /**
     * Validates whether the given <code>name</code>, <code>key</code>, and <code>text</code> produce a valid macro.
     *
     * @param name
     *         the name for the macro
     * @param key
     *         the key used to trigger the macro
     * @param text
     *         the text the macro produces
     *
     * @return true iff the parameters are valid
     */
    private boolean macroValidate(String name, String key, String text) {
        boolean nameValid = !name.trim().isEmpty();
        boolean keyValid = key.trim().length() == 1;
        boolean textValid = !text.isEmpty();

        return nameValid && keyValid && textValid;
    }
}
