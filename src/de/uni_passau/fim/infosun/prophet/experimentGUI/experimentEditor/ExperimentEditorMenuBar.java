package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.ContentEditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeXMLHandler;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeHTMLHandler;

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

        enableMenuItems();
    };

    private ActionListener loadActionListener = event -> {
        JFileChooser fileChooser = new JFileChooser(currentFile == null ? new File(".") : currentFile);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Experiment XML", "*.xml"));

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
        owner.setTitle(ExperimentEditor.TITLE + " - " + currentFile.getAbsolutePath());
        enableMenuItems();
    };
//    private class LoadActionListener implements ActionListener {
//
//        public void actionPerformed(ActionEvent arg0) {
//            JFileChooser fc = new JFileChooser(currentFile == null ? new File(".") : currentFile);
//            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                currentFile = fc.getSelectedFile();
//                try {
//                    QuestionTreeNode myTree = QuestionTreeXMLHandler.loadXMLTree(currentFile.getAbsolutePath());
//                    if (myTree != null) {
//                        experimentEditor.loadTree(myTree);
//                        experimentEditor.setTitle(ExperimentEditor.TITLE + " - " + currentFile.getAbsolutePath());
//
//                        enableMenuItems();
//                    } else {
//                        JOptionPane
//                                .showMessageDialog(experimentEditor, UIElementNames.MESSAGE_NO_VALID_EXPERIMENT_FILE);
//                    }
//                } catch (FileNotFoundException e) {
//                    JOptionPane.showMessageDialog(experimentEditor, UIElementNames.MESSAGE_FILE_NOT_FOUND,
//                            UIElementNames.MESSAGE_ERROR, JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//    }

    private ActionListener saveActionListener = event -> {

        if (currentFile != null) {
            tabbedPane.save();

            try {
                QTreeXMLHandler.saveExperimentXML((QTreeNode) qTreeModel.getRoot(), currentFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(owner, UIElementNames.MESSAGE_SAVE_ERROR,
                        UIElementNames.MESSAGE_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            saveAsMenuItem.doClick();
        }
    };

//
//    private class SaveActionListener implements ActionListener {
//
//        public void actionPerformed(ActionEvent arg0) {
//            if (currentFile != null) {
//                experimentEditor.getTabbedPane().save();
//                QuestionTreeXMLHandler
//                        .saveXMLTree(experimentEditor.getTreeComponent().getRoot(), currentFile.getAbsolutePath());
//            } else {
//                saveAsMenuItem.doClick();
//            }
//        }
//    }

    private ActionListener saveAsActionListener = event -> {
      JFileChooser fileChooser = new JFileChooser(currentFile == null ? new File(".") : currentFile);

        if (fileChooser.showSaveDialog(owner) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File chosenFile = fileChooser.getSelectedFile();

        if (chosenFile.exists()) {
            int option = JOptionPane.showConfirmDialog(owner, chosenFile.getName() + UIElementNames.MESSAGE_REPLACE_FILE,
                    UIElementNames.MESSAGE_REPLACE_FILE_TITLE, JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.NO_OPTION) {
                return;
            }
        }

        currentFile = chosenFile;

        try {
            QTreeXMLHandler.saveExperimentXML((QTreeNode) qTreeModel.getRoot(), currentFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, UIElementNames.MESSAGE_SAVE_ERROR, UIElementNames.MESSAGE_ERROR,
                    JOptionPane.ERROR_MESSAGE);
        }

        owner.setTitle(ExperimentEditor.TITLE + " - " + currentFile.getAbsolutePath());
        enableMenuItems();
    };

//    private class SaveAsActionListener implements ActionListener {
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
//
//                currentFile = file;
//
//                QuestionTreeNode rootNode = experimentEditor.getTreeComponent().getRoot();
//                QuestionTreeXMLHandler.saveXMLTree(rootNode, file.getAbsolutePath());
//
//                experimentEditor.setTitle(ExperimentEditor.TITLE + " - " + currentFile.getAbsolutePath());
//                enableMenuItems();
//            }
//        }
//    }

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
        String globalOutput = "";

        // get infos
        QTreeNode root = (QTreeNode) qTreeModel.getRoot();
        List<Pair<QTreeNode, List<Pair<String, String>>>> formInfos = QuestionTreeHTMLHandler.getForms(root);

        for (Pair<QTreeNode, List<Pair<String, String>>> nodeInfo : formInfos) {
            String output = "<b>" + nodeInfo.getKey().getName() + ":</b><br>";
            List<Pair<String, String>> forms = nodeInfo.getValue();
            boolean add = false;
            for (int i = 0; i < forms.size() - 1; i++) {
                Pair<String, String> currentForm = forms.get(i);
                int appearances = 1;
                // anzahl der vorkommen des derzeitigen
                // formularelementes berechnen
                for (int j = i + 1; j < forms.size(); j++) {
                    if (currentForm.getKey() != null && currentForm.getKey().equals(forms.get(j).getKey())) {
                        if ((currentForm.getValue() != null && currentForm.getValue().equals(forms.get(j).getValue()))
                                || currentForm.getValue() == null && forms.get(j).getValue() == null) {
                            appearances++;
                            forms.remove(forms.get(j));
                            j--;
                        }
                    }
                }
                if (appearances > 1) {
                    output += "Name = " + currentForm.getKey() + (currentForm.getValue() != null ? "; Value = "
                            + currentForm.getValue() : "; " + UIElementNames.MESSAGE_DUPLICATE_NO_VALUE) + "; "
                            + UIElementNames.MESSAGE_DUPLICATE_APPEARANCE + " = " + appearances + "<br>";
                    add = true;
                }
            }
            globalOutput += add ? output + "<br>" : "";
        }

        String msg;
        if (globalOutput.length() > 0) {
            msg = String.format("<html>%s<br><br>%s</html>", UIElementNames.MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST,
                    globalOutput);
        } else {
            msg = UIElementNames.MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST;
        }

        JOptionPane.showMessageDialog(null, msg);
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

        //DATEI

        JMenu fileMenu = new JMenu(UIElementNames.MENU_FILE);
        add(fileMenu);

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

        nameCheckMenuItem = new JMenuItem(UIElementNames.MENU_ITEM_CHECK_FORM_NAMES);
        extrasMenu.add(nameCheckMenuItem);
        nameCheckMenuItem.addActionListener(nameCheckActionListener);

        closeMenuItem.addActionListener(closeActionListener);

        enableMenuItems();
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

    /**
     * Activates or deactivates the menu items depending on the current state of the <code>QTree</code>.
     */
    private void enableMenuItems() {
        boolean treeExists = qTreeModel.getRoot() != null;

//        if (treeExists) {
//            exportCSVMenuItem.setEnabled(currentFile != null);
//        }

        saveMenuItem.setEnabled(treeExists);
        saveAsMenuItem.setEnabled(treeExists);
        nameCheckMenuItem.setEnabled(treeExists);
    }
}
