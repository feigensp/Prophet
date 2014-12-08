package de.uni_passau.fim.infosun.prophet.experimentEditor.macroEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import org.xml.sax.SAXException;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * An editor that enables the user to create, modify and delete macros stored in the macros.xml.
 * These macros are used by the ExperimentEditor when creating the HTML content for nodes.
 */
public class MacroEditor extends JFrame {

    private static final String xmlProlog = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>%n");
    private static final String XML_FILENAME = "macros.xml";
    private static final String XSD_FILENAME = "macros.xsd";
    private static File xmlFile;
    private static Validator validator;
    private static XStream xStream;

    static {
        xmlFile = new File(XML_FILENAME);

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(MacroEditor.class.getResource(XSD_FILENAME));
            validator = schema.newValidator();
        } catch (SAXException e) {
            System.err.println("Could not create a validator for ");
        }

        xStream = new XStream();
        xStream.processAnnotations(Macro.class);
        xStream.alias("root", List.class);
    }

    private JTextField nameField;
    private JTextField keyField;
    private JTextField textField;
    private DefaultListModel<Macro> model;
    private JList<Macro> macroJList;
    private Set<String> keys;

    /**
     * Simple container class for the macro information. Used for serialisation and display purposes.
     */
    @XStreamAlias("macro")
    @XStreamConverter(value = ToAttributedValueConverter.class, strings = {"text"})
    public static class Macro {

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

        /**
         * Returns the macros name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the macros key (a 1 character String).
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Returns the macros text.
         *
         * @return the text
         */
        public String getText() {
            return text;
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
        setTitle(getClass().getSimpleName());
        setJMenuBar(createMenuBar());
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    saveXMLFile();
                } catch (IOException e) {
                    System.err.println("Could not save the macros.xml file. " + e);
                    Component parent = SwingUtilities.getWindowAncestor((Component) event.getSource());
                    JOptionPane.showMessageDialog(parent, getLocalized("MESSAGE_SAVE_ERROR"));
                }
            }
        });

        Container content = getContentPane();

        content.setLayout(new BorderLayout());
        content.add(createListView(), BorderLayout.CENTER);
        content.add(createEditPanel(), BorderLayout.SOUTH);

        loadMacros().forEach(model::addElement);
        keys = new HashSet<>();

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
        JMenuItem removeItem = new JMenuItem(getLocalized("MACRO_EDITOR_DELETE_MACRO"));
        popupMenu.add(removeItem);
        removeItem.addActionListener(event -> model.remove(macroJList.getSelectedIndex()));

        macroJList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && macroJList.getModel().getSize() != 0) {
                    show(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && macroJList.getModel().getSize() != 0) {
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
        JButton addButton = new JButton("Add");

        ActionListener newMacroListener = event -> {
            String name = nameField.getText();
            String key = keyField.getText();
            String text = textField.getText();

            if (macroValidate(name, key, text)) {
                Macro macro = macroJList.getSelectedValue();

                if (macro == null) {
                    macro = new Macro(name, key, text);
                    model.addElement(macro);
                    keys.add(key.toUpperCase());
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
            } else if (!event.getSource().equals(addButton)) {
                Component focusOwner = getFocusOwner();
                boolean switchFocus = true;

                if (focusOwner.equals(nameField)) {
                    switchFocus = !nameField.getText().trim().isEmpty();
                } else if (focusOwner.equals(keyField)) {
                    switchFocus = !keyField.getText().trim().isEmpty();
                } else if (focusOwner.equals(textField)) {
                    switchFocus = !textField.getText().trim().isEmpty();
                }

                if (switchFocus) {
                    if (focusOwner.equals(textField)) {
                        nameField.requestFocus();
                    } else {
                        focusOwner.transferFocus();
                    }
                }
            }
        };

        nameField.addActionListener(newMacroListener);
        keyField.addActionListener(newMacroListener);
        textField.addActionListener(newMacroListener);
        addButton.addActionListener(newMacroListener);

        textLabel.setPreferredSize(nameLabel.getPreferredSize());

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
        textPanel.add(Box.createHorizontalStrut(5));
        textPanel.add(addButton);

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
        JMenu menu = new JMenu(getLocalized("MENU_FILE"));
        JMenuItem saveMenuItem = new JMenuItem(getLocalized("MENU_FILE_SAVE"));
        JMenuItem closeMenuItem = new JMenuItem(getLocalized("MENU_FILE_QUIT"));

        saveMenuItem.addActionListener(event -> {
            try {
                saveXMLFile();
            } catch (IOException e) {
                System.err.println("Could not save the macros.xml file. " + e);
                Component parent = SwingUtilities.getWindowAncestor((Component) event.getSource());
                JOptionPane.showMessageDialog(parent, getLocalized("MESSAGE_SAVE_ERROR"));
            }
        });

        closeMenuItem.addActionListener(event -> processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        menuBar.add(menu);
        menu.add(saveMenuItem);
        menu.add(closeMenuItem);

        return menuBar;
    }

    /**
     * Saves the current macros to the macros.xml file.
     *
     * @throws IOException
     *         if there is an error writing to the <code>xmlFile</code>
     */
    private void saveXMLFile() throws IOException {
        List<Macro> list = Collections.list(model.elements());
        CharsetEncoder utf8encoder = StandardCharsets.UTF_8.newEncoder();

        if (!list.isEmpty()) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(xmlFile), utf8encoder)) {
                writer.write(xmlProlog);
                xStream.toXML(list, writer);
            }
        }
    }

    /**
     * Loads the <code>Macro</code>s contained in the 'macros.xml' file. If there is an error deserializing the
     * file or if the file does not conform to 'macros.xsd' an empty <code>List</code> will be returned.
     *
     * @return the loaded <code>Macro</code>s
     */
    @SuppressWarnings({"unchecked"}) // the macros.xsd ensures that the XML can be deserialised to a Collection<Macro>
    public static List<Macro> loadMacros() {
        List<Macro> macros = new LinkedList<>();
        CharsetDecoder utf8decoder;

        if (!xmlFile.exists()) {
            return macros;
        }

        try {
            utf8decoder = StandardCharsets.UTF_8.newDecoder();
            try (Reader reader = new InputStreamReader(new FileInputStream(xmlFile), utf8decoder)) {
                validator.validate(new StreamSource(reader));
            }

            utf8decoder = StandardCharsets.UTF_8.newDecoder();
            try (Reader reader = new InputStreamReader(new FileInputStream(xmlFile), utf8decoder)) {
                macros.addAll((Collection<Macro>) xStream.fromXML(reader));
            } catch (XStreamException | ClassCastException e) {
                System.err.println(xmlFile.getName() + " could not be deserialised. " + e.getMessage());
            }
        } catch (SAXException | IOException e) {
            System.err.println(xmlFile.getName() + " did not pass validation. " + e.getMessage());
        }

        return macros;
    }

    /**
     * Validates whether the given <code>name</code>, <code>key</code>, and <code>text</code> produce a macro.
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
        boolean keyValid = key.trim().length() == 1 && !keys.contains(key.toUpperCase());
        boolean textValid = !text.isEmpty();

        return nameValid && keyValid && textValid;
    }
}
