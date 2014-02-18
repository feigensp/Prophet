package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsCheckBox;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class EditAndSavePlugin implements CodeViewerPluginInterface {

    public final static String KEY = "editable";
    private boolean editable;
    private EditorTabbedPane tabbedPane;
    private File saveDir;
    HashMap<EditorPanel, Boolean> isChanged;

    @Override
    public SettingsComponentDescription getSettingsComponentDescription() {
        return new SettingsComponentDescription(SettingsCheckBox.class, KEY,
                UIElementNames.EDIT_AND_SAVE_EDITABLE_CODE);
    }

    @Override
    public void init(QuestionTreeNode selected) {
        editable = Boolean.parseBoolean(selected.getAttributeValue(KEY));
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
        if (editable) {
            tabbedPane = viewer.getTabbedPane();
            saveDir = new File(viewer.getSaveDir().getPath() + System.getProperty("file.separator") + "savedFiles");
            isChanged = new HashMap<>();
            JMenuItem saveMenuItem = new JMenuItem(UIElementNames.EDIT_AND_SAVE_SAVE);
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
            viewer.addMenuItemToFileMenu(saveMenuItem);
            saveMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    saveActiveFile();
                }
            });
            JMenuItem saveAllMenuItem = new JMenuItem(UIElementNames.EDIT_AND_SAVE_SAVE_ALL);
            saveAllMenuItem.setAccelerator(KeyStroke
                    .getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK | java.awt.Event.SHIFT_MASK));
            viewer.addMenuItemToFileMenu(saveAllMenuItem);
            saveAllMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    saveAllFiles();
                }
            });
        }
    }

    private void saveActiveFile() {
        Component activeComp = tabbedPane.getSelectedComponent();
        if (activeComp != null && activeComp instanceof EditorPanel) {
            saveEditorPanel((EditorPanel) activeComp);
        }
    }

    private void saveAllFiles() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component myComp = tabbedPane.getComponentAt(i);
            if (myComp instanceof EditorPanel) {
                saveEditorPanel((EditorPanel) myComp);
            }
        }
    }

    private void saveEditorPanel(EditorPanel editorPanel) {
        File file = new File(saveDir.getPath() + editorPanel.getFilePath());
        FileWriter fileWriter = null;
        try {
            file.getParentFile().mkdirs();
            fileWriter = new FileWriter(file);
            fileWriter.write(editorPanel.getTextArea().getText());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isChanged.put(editorPanel, false);
    }

    @Override
    public void onEditorPanelCreate(final EditorPanel editorPanel) {
        if (editable) {
            RSyntaxTextArea textArea = editorPanel.getTextArea();
            File savedFile = new File(saveDir.getPath() + editorPanel.getFilePath());
            if (savedFile.exists()) {
                Document doc = textArea.getDocument();
                DocumentListener[] listeners = removeDocumentListener((RSyntaxDocument) doc);
                try {
                    doc.remove(0, doc.getLength());
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[(int) savedFile.length()];
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(savedFile);
                    fileStream.read(buffer);
                    doc.insertString(0, new String(buffer), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textArea.setCaretPosition(0);
                readdDocumentListeners((RSyntaxDocument) doc, listeners);
            }
            textArea.setEditable(true);
            textArea.getDocument().addDocumentListener(new DocumentListener() {

                private void changeOccured() {
                    isChanged.put(editorPanel, true);
                }

                public void changedUpdate(DocumentEvent arg0) {
//					changeOccured();
                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    changeOccured();
                }

                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    changeOccured();
                }
            });
        }
    }

    private DocumentListener[] removeDocumentListener(RSyntaxDocument doc) {
        DocumentListener[] listeners = doc.getDocumentListeners();
        for (DocumentListener listener : listeners) {
            doc.removeDocumentListener(listener);
        }
        return listeners;
    }

    private void readdDocumentListeners(RSyntaxDocument doc, DocumentListener[] listeners) {
        for (DocumentListener listener : listeners) {
            doc.addDocumentListener(listener);
        }
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        if (editable) {
            Boolean changed = isChanged.get(editorPanel);
            if (changed != null && changed) {
                int n = JOptionPane.showConfirmDialog(null, UIElementNames.EDIT_AND_SAVE_DIALOG_SAVE_CHANGES + "?",
                        UIElementNames.EDIT_AND_SAVE_SAVE + "?", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    saveEditorPanel(editorPanel);
                }
            }
            isChanged.remove(editorPanel);
        }
    }

    @Override
    public void onClose() {
        if (editable) {
            boolean ask = false;
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component myComp = tabbedPane.getComponentAt(i);
                if (myComp instanceof EditorPanel) {
                    Boolean changed = isChanged.get(myComp);
                    System.out.println(changed);
                    if (changed != null && changed) {
                        ask = true;
                        break;
                    }
                }
            }
            if (ask) {
                int n = JOptionPane.showConfirmDialog(null, UIElementNames.EDIT_AND_SAVE_DIALOG_SAVE_CHANGES + "?",
                        UIElementNames.EDIT_AND_SAVE_SAVE + "?", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    saveAllFiles();
                }
            }
            isChanged = null;
        }
    }
}
