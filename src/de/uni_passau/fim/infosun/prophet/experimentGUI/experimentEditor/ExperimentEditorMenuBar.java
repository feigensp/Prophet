package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
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
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.miniEditors.MacroEditor;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeHTMLHandler;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeXMLHandler;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;

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

//    private class XMLToCSVActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String answersXmlFileName = "answers.xml";
//            String experimentCode;
//            QuestionTreeNode root;
//            List<Document> answerXmlDocuments;
//            File csvFile;
//
//            // Get the dir in which to search for the answer files. Subdirs will be searched, too.
//            File searchDir = currentFile.getParentFile();
//
////            JFileChooser dirChooser = new JFileChooser();
////            int dirReturnCode;
////
////            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
////            dirChooser.setMultiSelectionEnabled(false);
////            dirReturnCode = dirChooser.showOpenDialog(null);
////
////            if (dirReturnCode == JFileChooser.APPROVE_OPTION) {
////                searchDir = dirChooser.getSelectedFile();
////            } else {
////                return;
////            }
//
//            List<File> answerFiles = QuestionTreeXMLHandler.getFilesByName(searchDir, answersXmlFileName);
//
////            // Try to find experiment code in path. Look in parent dir of the first answerFile found.
////            if (answerFiles.size() > 0) {
////                String folderName = answerFiles.get(0).getParentFile().getName();
////                String[] parts = folderName.split("_");
////
////                if (parts.length > 0) {
////                    experimentCode = parts[1];
////                } else {
////                    experimentCode = "unknown";
////                }
////            } else {
////                return;
////            }
//
//            root = experimentEditor.getTreeComponent().getRoot();
//            experimentCode = root.getAttributeValue(Constants.KEY_EXPERIMENT_CODE);
//
//            answerXmlDocuments = QuestionTreeXMLHandler.getDocuments(answerFiles);
//
//            // Get the csv file in which the results are to be stored.
//            JFileChooser fileChooser = new JFileChooser(searchDir);
//            int csvReturnCode;
//
//            fileChooser.setName(UIElementNames.EXPORT_SELECT_TARGET_CSV);
//            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV-Dateien", "csv"));
//            fileChooser.setMultiSelectionEnabled(false);
//
//            csvReturnCode = fileChooser.showSaveDialog(null);
//
//            if (csvReturnCode == JFileChooser.APPROVE_OPTION) {
//                csvFile = fileChooser.getSelectedFile();
//
//                QuestionTreeXMLHandler.saveAsCSV(root, answerXmlDocuments, csvFile, experimentCode);
//            }
//        }
//    }

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

        if (fileChooser.showOpenDialog(owner) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        currentFile = fileChooser.getSelectedFile();

        QTreeNode treeRoot = QTreeXMLHandler.loadExperimentXML(currentFile);

        if (treeRoot == null) {
            JOptionPane.showMessageDialog(owner, UIElementNames.MESSAGE_NO_VALID_EXPERIMENT_FILE);
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
            } catch (IOException e) {
                JOptionPane.showMessageDialog(owner, UIElementNames.MESSAGE_SAVE_ERROR,
                        UIElementNames.MESSAGE_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            saveAsMenuItem.doClick();
        }
    };

    private ActionListener saveAsActionListener = event -> {
        JFileChooser fileChooser = new JFileChooser(currentFile == null ? new File(".") : currentFile);
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showSaveDialog(owner) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File chosenFile = fileChooser.getSelectedFile();
        String path = chosenFile.getPath();
        String suffix = ".xml";

        if (!path.endsWith(suffix)) {
            chosenFile = new File(path + suffix);
        }

        if (chosenFile.exists()) {
            int option = JOptionPane.showConfirmDialog(owner, chosenFile.getName() + UIElementNames.MESSAGE_REPLACE_FILE,
                    UIElementNames.MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.NO_OPTION) {
                return;
            }
        }

        currentFile = chosenFile;

        try {
            QTreeXMLHandler.saveExperimentXML(qTreeModel.getRoot(), currentFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, UIElementNames.MESSAGE_SAVE_ERROR, UIElementNames.MESSAGE_ERROR,
                    JOptionPane.ERROR_MESSAGE);
        }

        owner.setTitle(ExperimentEditor.class.getSimpleName() + " - " + currentFile.getAbsolutePath());
    };

//    private class ExportHTMLFileActionListener implements ActionListener {
//
//        public void actionPerformed(ActionEvent arg0) {
//            JFileChooser fc = new JFileChooser(currentFile);
//
//            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//                File file = fc.getSelectedFile();
//
//                if (file.exists()) {
//                    int n = JOptionPane.showConfirmDialog(null, file.getName() + UIElementNames.MESSAGE_REPLACE_FILE,
//                            UIElementNames.MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);
//                    if (n == JOptionPane.NO_OPTION) {
//                        return;
//                    }
//                }
//                QuestionTreeHTMLHandler.saveAsHTMLFile(file, experimentEditor.getTreeComponent().getRoot());
//            }
//        }
//    }

//    private class ExportCSVActionListener implements ActionListener {
//
//        public void actionPerformed(ActionEvent arg0) {
//            try {
//                QuestionTreeNode experimentNode = experimentEditor.getTreeComponent().getRoot();
//
//                ArrayList<Pair<QuestionTreeNode, ArrayList<Pair<String, String>>>> formInfos =
//                        QuestionTreeHTMLHandler.getForms(experimentNode);
//
//                ArrayList<QuestionTreeNode> answerNodes = new ArrayList<>();
//                String experimentCode = experimentNode.getAttributeValue(Constants.KEY_EXPERIMENT_CODE);
//
//                // Antwortdateien ermitteln
//                String path = currentFile.getCanonicalPath();
//                int index = path.lastIndexOf(System.getProperty("file.separator"));
//                path = index != -1 ? path.substring(0, index) : path;
//                File f = new File(path);
//
//                getAnswerFiles(f, answerNodes, experimentCode, true);
//
//                // csv Datei erstellen
//                QuestionTreeXMLHandler.saveAsCSVFile(formInfos, answerNodes, experimentCode, path);
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(null, UIElementNames.MESSAGE_PATH_NOT_FOUND);
//            }
//        }
//    }

    private ActionListener nameCheckActionListener = event -> {
        Map<String, List<TreePath>> duplicates = QTreeHTMLHandler.checkNames(qTreeModel.getRoot());

        if (duplicates.isEmpty()) {
            JOptionPane.showMessageDialog(null, UIElementNames.MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST);
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
        header.appendElement(tableHeader).text(UIElementNames.MULTILINEDIALOG_NAME);
        header.appendElement(tableHeader).text(UIElementNames.MESSAGE_DUPLICATE_APPEARANCE);

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

        JOptionPane.showMessageDialog(null,sPane,
                UIElementNames.MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST, JOptionPane.INFORMATION_MESSAGE);
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

//              if (treeExists) {
//                  exportCSVMenuItem.setEnabled(currentFile != null);
//              }

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

        JMenu fileMenu = new JMenu(UIElementNames.MENU_FILE);
        add(fileMenu);
        fileMenu.addMenuListener(enableListener);

        JMenuItem newMenuItem = new JMenuItem(UIElementNames.MENU_FILE_NEW);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        fileMenu.add(newMenuItem);
        newMenuItem.addActionListener(newActionListener);

        JMenuItem loadMenuItem = new JMenuItem(UIElementNames.MENU_FILE_OPEN);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        fileMenu.add(loadMenuItem);
        loadMenuItem.addActionListener(loadActionListener);

        saveMenuItem = new JMenuItem(UIElementNames.MENU_FILE_SAVE);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        fileMenu.add(saveMenuItem);
        saveMenuItem.addActionListener(saveActionListener);

        saveAsMenuItem = new JMenuItem(UIElementNames.MENU_FILE_SAVE_AS);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        fileMenu.add(saveAsMenuItem);
        saveAsMenuItem.addActionListener(saveAsActionListener);

        fileMenu.addSeparator();

        exportMenu = new JMenu(UIElementNames.MENU_EXPORT);
        fileMenu.add(exportMenu);

//        JMenuItem exportHTMLFileMenuItem = new JMenuItem(UIElementNames.MENU_ITEM_HTML_OF_QUESTIONS);
//        exportMenu.add(exportHTMLFileMenuItem);
//        exportHTMLFileMenuItem.addActionListener(new ExportHTMLFileActionListener());
//
//        exportCSVMenuItem = new JMenuItem(UIElementNames.MENU_ITEM_CSV_OF_ANSWERS);
//        exportMenu.add(exportCSVMenuItem);
//        exportCSVMenuItem.addActionListener(new ExportCSVActionListener());
//
//        JMenuItem xmlToCsvAllInDir = new JMenuItem(UIElementNames.MENU_ITEM_XML_TO_CSV);
//        exportMenu.add(xmlToCsvAllInDir);
//        xmlToCsvAllInDir.addActionListener(new XMLToCSVActionListener());

        fileMenu.addSeparator();

        JMenuItem closeMenuItem = new JMenuItem(UIElementNames.MENU_FILE_QUIT);
        fileMenu.add(closeMenuItem);

        // BEARBEITEN

        JMenu editMenu = new JMenu(UIElementNames.MENU_EDIT);
        add(editMenu);

        JMenuItem searchMenuItem = new JMenuItem(UIElementNames.MENU_EDIT_FIND);
        searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        editMenu.add(searchMenuItem);
        searchMenuItem.addActionListener(searchActionListener);

        // EXTRAS

        JMenu extrasMenu = new JMenu(UIElementNames.MENU_PLAUSIBILITY_FEATURES);
        add(extrasMenu);
        extrasMenu.addMenuListener(enableListener);

        nameCheckMenuItem = new JMenuItem(UIElementNames.MENU_ITEM_CHECK_FORM_NAMES);
        extrasMenu.add(nameCheckMenuItem);
        nameCheckMenuItem.addActionListener(nameCheckActionListener);

        JMenuItem macroEd = new JMenuItem(MacroEditor.class.getSimpleName());
        extrasMenu.add(macroEd);
        macroEd.addActionListener(event -> MacroEditor.main(null));

        closeMenuItem.addActionListener(closeActionListener);
    }

//    /**
//     * Search for answer.xml files in the directory of file and its subdirectories.
//     * Loads the Data from this files and saves it to the answerNodes-ArrayList.
//     * Select directories after name (if the correct experiment code is used)
//     *
//     * @param file
//     *         file or directory in which is searched
//     * @param answerNodes
//     *         storage for the data from the answer.xml files
//     * @param experimentCode
//     *         used experiment code
//     * @param search
//     *         if false the answer.xml files of this directory are not used - but the files of the subdirectories are
//     *         still used
//     */
//    private void getAnswerFiles(File file, ArrayList<QuestionTreeNode> answerNodes, String experimentCode,
//            boolean search) {
//        File[] directoryFiles = file.listFiles();
//        for (File currentFile : directoryFiles) {
//            if (currentFile.isDirectory()) {
//                if (currentFile.getName().startsWith(experimentCode + "_")) {
//                    getAnswerFiles(currentFile, answerNodes, experimentCode, true);
//                } else {
//                    getAnswerFiles(currentFile, answerNodes, experimentCode, false);
//                }
//            } else if (search && currentFile.getName().equals("answers.xml")) {
//                try {
//                    QuestionTreeNode node = QuestionTreeXMLHandler.loadAnswerXMLTree(currentFile.getPath());
//                    if (node != null) {
//                        answerNodes.add(node);
//                    }
//                } catch (FileNotFoundException e) {
//                    JOptionPane.showMessageDialog(null,
//                            UIElementNames.MESSAGE_FILE_NOT_FOUND + ": " + currentFile.getAbsolutePath());
//                }
//            }
//        }
//    }
}
