package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.miniEditors.MacroEditor;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.*;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.xml.QTreeXMLHandler;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;
import static javax.swing.JFileChooser.APPROVE_OPTION;

/**
 * The menu bar of the ExperimentViewer. Separated to enhance readability.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditorMenuBar extends JMenuBar {

    private File currentFile;
    private QTreeModel qTreeModel;
    private ExperimentEditorTabbedPane tabbedPane;
    private JFrame owner;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem nameCheckMenuItem;
    private JMenuItem exportCSVMenuItem;
    private JMenu exportMenu;
    private FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Experiment XML", "xml");

    private class XMLToCSVActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            JFileChooser saveFileChooser = new JFileChooser();
            saveFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Comma Separated Values", "csv"));
            saveFileChooser.setFileFilter(saveFileChooser.getChoosableFileFilters()[1]);

            if (dirChooser.showOpenDialog(owner) == APPROVE_OPTION) {
                File answerDir = dirChooser.getSelectedFile();

                if (saveFileChooser.showSaveDialog(owner) == APPROVE_OPTION) {
                    File saveFile = saveFileChooser.getSelectedFile();

                    if (saveFile.exists() && !confirmOverwrite(saveFile)) {
                        return;
                    }

                    new Thread(() -> QTreeCSVHandler.exportCSV(answerDir, saveFile)).start();
                }
            }
        }
    }

    private ActionListener searchActionListener = event -> {
        Component current = tabbedPane.getSelectedComponent();

        if (current instanceof ContentEditorPanel) {
            ((ContentEditorPanel) current).search();
        }
    };

    private ActionListener closeActionListener = event -> {
        WindowEvent closeEvent = new WindowEvent(owner, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
    };

    private ActionListener newActionListener = event -> {
        currentFile = null;
        qTreeModel.setRoot(new QTreeNode(null, EXPERIMENT, "Experiment"));
    };

    private ActionListener loadActionListener = event -> {
        JFileChooser fileChooser = new JFileChooser(currentFile == null ? new File(".") : currentFile);
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showOpenDialog(owner) != APPROVE_OPTION) {
            return;
        }
        currentFile = fileChooser.getSelectedFile();

        QTreeNode treeRoot = QTreeXMLHandler.loadExperimentXML(currentFile);

        if (treeRoot == null) {
            JOptionPane.showMessageDialog(owner, getLocalized("MESSAGE_NO_VALID_EXPERIMENT_FILE"));
            return;
        }

        qTreeModel.setRoot(treeRoot);
        owner.setTitle(ExperimentEditor.class.getSimpleName() + " - " + currentFile.getAbsolutePath());
    };

    private ActionListener saveActionListener = event -> {

        if (currentFile != null) {
            tabbedPane.save();

            try {
                QTreeXMLHandler.saveExperimentXML(qTreeModel.getRoot(), currentFile);
                confirmSave();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(owner, getLocalized("MESSAGE_SAVE_ERROR"), getLocalized("MESSAGE_ERROR"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            saveAsMenuItem.doClick();
        }
    };

    private ActionListener saveAsActionListener = event -> {
        JFileChooser fileChooser = new JFileChooser(currentFile == null ? new File(".") : currentFile);
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showSaveDialog(owner) != APPROVE_OPTION) {
            return;
        }

        File chosenFile = fileChooser.getSelectedFile();
        String path = chosenFile.getPath();
        String suffix = ".xml";

        if (!path.endsWith(suffix)) {
            chosenFile = new File(path + suffix);
        }

        if (chosenFile.exists() && !confirmOverwrite(chosenFile)) {
            return;
        }

        currentFile = chosenFile;

        try {
            QTreeXMLHandler.saveExperimentXML(qTreeModel.getRoot(), currentFile);
            confirmSave();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, getLocalized("MESSAGE_SAVE_ERROR"), getLocalized("MESSAGE_ERROR"),
                    JOptionPane.ERROR_MESSAGE);
        }

        owner.setTitle(ExperimentEditor.class.getSimpleName() + " - " + currentFile.getAbsolutePath());
    };

    private class ExportHTMLFileActionListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            JFileChooser fc = new JFileChooser(currentFile);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("HyperText Markup Language", "htm", "html"));
            fc.setFileFilter(fc.getChoosableFileFilters()[1]);

            if (fc.showSaveDialog(owner) == APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                if (!file.getPath().endsWith(".htm") && !file.getPath().endsWith(".html")) {
                    file = new File(file.getAbsolutePath() + ".html");
                }

                if (file.exists()) {
                    int n = JOptionPane.showConfirmDialog(owner, file.getName() + getLocalized("MESSAGE_REPLACE_FILE"),
                            getLocalized("MESSAGE_REPLACE_FILE_TITLE"), JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                QTreeHTMLHandler.saveAsHTMLFile(file, qTreeModel.getRoot());
            }
        }
    }

    private ActionListener nameCheckActionListener = event -> {
        Map<String, List<TreePath>> duplicates = QTreeHTMLHandler.checkNames(qTreeModel.getRoot());

        if (duplicates.isEmpty()) {
            JOptionPane.showMessageDialog(null, getLocalized("MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST"));
            return;
        }

        // build a html table containing the returned duplicates
        String tableRow = "tr";
        String tableHeader = "th";
        String tableData = "td";
        Element html = new Element(Tag.valueOf("html"), "");
        Element table = html.appendElement("table");
        Element header = table.appendElement(tableRow);
        Element row;

        table.attr("border", "1");
        header.appendElement(tableHeader).text(getLocalized("MULTILINEDIALOG_NAME"));
        header.appendElement(tableHeader).text(getLocalized("MESSAGE_DUPLICATE_APPEARANCE"));

        boolean first;
        for (Map.Entry<String, List<TreePath>> entry : duplicates.entrySet()) {

            first = true;
            for (TreePath tPath : entry.getValue()) {
                row = table.appendElement(tableRow);

                if (first) {
                    row.appendElement(tableData).text(entry.getKey());
                } else {
                    row.appendElement(tableData);
                }
                row.appendElement(tableData).text(tPath.toString());
                first = false;
            }
        }

        JScrollPane sPane = new JScrollPane(new JLabel(html.toString()));
        sPane.setPreferredSize(new Dimension(sPane.getPreferredSize().width + 20, 500));

        JOptionPane.showMessageDialog(null, sPane, getLocalized("MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST"),
                JOptionPane.INFORMATION_MESSAGE);
    };

    /**
     * Constructs a new <code>ExperimentEditorMenuBar</code>.
     *
     * @param qTree
     *         the <code>QTree</code> used in the <code>ExperimentEditor</code>
     * @param tabbedPane
     *         the <code>ExperimentEditorTabbedPane</code> in the <code>ExperimentEditor</code>
     */
    public ExperimentEditorMenuBar(QTree qTree, ExperimentEditorTabbedPane tabbedPane) {
        this.owner = (JFrame) SwingUtilities.getWindowAncestor(qTree);
        this.qTreeModel = (QTreeModel) qTree.getModel();
        this.tabbedPane = tabbedPane;

        MenuListener enableListener = new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                boolean treeExists = qTreeModel.getRoot() != null;

                saveMenuItem.setEnabled(treeExists);
                saveAsMenuItem.setEnabled(treeExists);
                nameCheckMenuItem.setEnabled(treeExists);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        };

        //DATEI

        JMenu fileMenu = new JMenu(getLocalized("MENU_FILE"));
        add(fileMenu);
        fileMenu.addMenuListener(enableListener);

        JMenuItem newMenuItem = new JMenuItem(getLocalized("MENU_FILE_NEW"));
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        fileMenu.add(newMenuItem);
        newMenuItem.addActionListener(newActionListener);

        JMenuItem loadMenuItem = new JMenuItem(getLocalized("MENU_FILE_OPEN"));
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        fileMenu.add(loadMenuItem);
        loadMenuItem.addActionListener(loadActionListener);

        saveMenuItem = new JMenuItem(getLocalized("MENU_FILE_SAVE"));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        fileMenu.add(saveMenuItem);
        saveMenuItem.addActionListener(saveActionListener);

        saveAsMenuItem = new JMenuItem(getLocalized("MENU_FILE_SAVE_AS"));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        fileMenu.add(saveAsMenuItem);
        saveAsMenuItem.addActionListener(saveAsActionListener);

        fileMenu.addSeparator();

        exportMenu = new JMenu(getLocalized("MENU_EXPORT"));
        fileMenu.add(exportMenu);

        JMenuItem exportHTMLFileMenuItem = new JMenuItem(getLocalized("MENU_ITEM_HTML_OF_QUESTIONS"));
        exportMenu.add(exportHTMLFileMenuItem);
        exportHTMLFileMenuItem.addActionListener(new ExportHTMLFileActionListener());

        JMenuItem xmlToCsvAllInDir = new JMenuItem(getLocalized("MENU_ITEM_XML_TO_CSV"));
        exportMenu.add(xmlToCsvAllInDir);
        xmlToCsvAllInDir.addActionListener(new XMLToCSVActionListener());

        fileMenu.addSeparator();

        JMenuItem closeMenuItem = new JMenuItem(getLocalized("MENU_FILE_QUIT"));
        fileMenu.add(closeMenuItem);

        // BEARBEITEN

        JMenu editMenu = new JMenu(getLocalized("MENU_EDIT"));
        add(editMenu);

        JMenuItem searchMenuItem = new JMenuItem(getLocalized("MENU_EDIT_FIND"));
        searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        editMenu.add(searchMenuItem);
        searchMenuItem.addActionListener(searchActionListener);

        // EXTRAS

        JMenu extrasMenu = new JMenu(getLocalized("MENU_PLAUSIBILITY_FEATURES"));
        add(extrasMenu);
        extrasMenu.addMenuListener(enableListener);

        nameCheckMenuItem = new JMenuItem(getLocalized("MENU_ITEM_CHECK_FORM_NAMES"));
        extrasMenu.add(nameCheckMenuItem);
        nameCheckMenuItem.addActionListener(nameCheckActionListener);

        JMenuItem macroEd = new JMenuItem(MacroEditor.class.getSimpleName());
        extrasMenu.add(macroEd);
        macroEd.addActionListener(event -> MacroEditor.main(null));

        closeMenuItem.addActionListener(closeActionListener);
    }

    /**
     * Display a OK dialog confirming that the current experiment was saved.
     */
    private void confirmSave() {
        JOptionPane.showMessageDialog(owner, getLocalized("MESSAGE_SAVE_SUCCESSFUL"), null, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a YES/NO dialog asking whether the user would like to overwrite an existing file.
     *
     * @param saveFile
     *         the <code>File</code> that would be overridden
     *
     * @return true iff the user clicked YES
     */
    private boolean confirmOverwrite(File saveFile) {
        return JOptionPane.showConfirmDialog(owner,
                saveFile.getName() + " " + getLocalized("MESSAGE_REPLACE_FILE"),
                getLocalized("MESSAGE_REPLACE_FILE_TITLE"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
