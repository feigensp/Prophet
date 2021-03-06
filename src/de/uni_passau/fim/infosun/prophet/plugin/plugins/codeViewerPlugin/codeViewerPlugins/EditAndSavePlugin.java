package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.CheckBoxSetting;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.InputEvent.SHIFT_MASK;

/**
 * A <code>Plugin</code> that enables editing the contents of the <code>CodeViewer</code>s <code>EditorPanel</code>s.
 * Menu items and keyboard shortcuts to save the currently active panel or all panels will be added to the
 * <code>CodeViewer</code>. The edited versions of the files will be saved in a directory under the
 * <code>CodeViewer</code>s save directory.
 */
public class EditAndSavePlugin implements Plugin {

    private static final String KEY = "editable";
    private static final String DIR_NAME = "savedFiles";

    private File saveDir;
    private EditorTabbedPane tabbedPane;
    private Set<EditorPanel> changed;
    
    private boolean enabled;

    /**
     * Constructs a new <code>EditAndSavePlugin</code>.
     */
    public EditAndSavePlugin() {
        changed = new HashSet<>();
    }

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new CheckBoxSetting(attribute, getClass().getSimpleName());
        setting.setCaption(getLocalized("EDIT_AND_SAVE_EDITABLE_CODE"));

        return setting;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        Attribute attr = viewer.getAttribute();
        enabled = attr.containsSubAttribute(KEY) && Boolean.parseBoolean(attr.getSubAttribute(KEY).getValue());

        if (!enabled) {
            return;
        }

        tabbedPane = viewer.getTabbedPane();
        saveDir = new File(viewer.getSaveDir(), DIR_NAME);

        if (!saveDir.mkdirs()) {
            System.err.println("Could not create the directory to save files in.");
        }

        JMenuItem saveMenuItem = new JMenuItem(getLocalized("EDIT_AND_SAVE_SAVE"));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK));
        saveMenuItem.addActionListener(event -> saveActiveEditorPanel());
        viewer.addMenuItemToFileMenu(saveMenuItem);

        JMenuItem saveAllMenuItem = new JMenuItem(getLocalized("EDIT_AND_SAVE_SAVE_ALL"));
        saveAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK | SHIFT_MASK));
        saveAllMenuItem.addActionListener(event -> saveAllEditorPanels());
        viewer.addMenuItemToFileMenu(saveAllMenuItem);
    }

    @Override
    public void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel) {

        if (!enabled) {
            return;
        }

        RSyntaxTextArea textArea = editorPanel.getTextArea();
        File savedFile = getSaveFile(editorPanel);

        if (savedFile.exists()) {
            Document doc = textArea.getDocument();
            DocumentListener[] listeners = removeListeners((RSyntaxDocument) doc);

            try {
                doc.remove(0, doc.getLength());
            } catch (BadLocationException e) {
                System.err.println("Could not clear the document.");
                System.err.println(e.getMessage());
            }

            try {
                doc.insertString(0, readFile(savedFile), null);
            } catch (BadLocationException e) {
                System.err.println("Could not insert the contents of the saveFile.");
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println("Could not read the saveFile.");
                System.err.println(e.getMessage());
            }

            textArea.setCaretPosition(0);
            addListeners((RSyntaxDocument) doc, listeners);
        }

        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            private void changeOccurred() {
                changed.add(editorPanel);
            }

            @Override
            public void changedUpdate(DocumentEvent event) {

            }

            @Override
            public void insertUpdate(DocumentEvent event) {
                changeOccurred();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                changeOccurred();
            }
        });
    }

    @Override
    public void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel) {

        if (!enabled) {
            return;
        }

        if (changed.contains(editorPanel)) {
            String msg = getLocalized("EDIT_AND_SAVE_DIALOG_SAVE_CHANGES") + "?";
            String title = getLocalized("EDIT_AND_SAVE_SAVE") + "?";

            if (JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saveEditorPanel(editorPanel);
            }

            changed.remove(editorPanel);
        }
    }

    @Override
    public void onClose(CodeViewer codeViewer) {

        if (!enabled) {
            return;
        }

        if (!changed.isEmpty()) {
            String msg = getLocalized("EDIT_AND_SAVE_DIALOG_SAVE_CHANGES") + "?";
            String title = getLocalized("EDIT_AND_SAVE_SAVE") + "?";

            if (JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                changed.forEach(this::saveEditorPanel);
            }
        }

        saveDir = null;
        tabbedPane = null;
        changed.clear();
    }

    /**
     * Saves the <code>EditorPanel</code> that is currently selected in the <code>tabbedPane</code>.
     */
    private void saveActiveEditorPanel() {
        Component activeComp = tabbedPane.getSelectedComponent();

        if (activeComp != null && activeComp instanceof EditorPanel) {
            saveEditorPanel((EditorPanel) activeComp);
        }
    }

    /**
     * Saves all <code>EditorPanel</code>s that are currently open in the <code>tabbedPane</code>.
     */
    private void saveAllEditorPanels() {

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component myComp = tabbedPane.getComponentAt(i);

            if (myComp instanceof EditorPanel) {
                saveEditorPanel((EditorPanel) myComp);
            }
        }
    }

    /**
     * Saves the contents of the given <code>editorPanel</code> to a <code>File</code> (of the original name) in the
     * directory <code>saveDir</code>.
     *
     * @param editorPanel
     *         the <code>EditorPanel</code> whose contents are to be saved
     */
    private void saveEditorPanel(EditorPanel editorPanel) {
        File file = getSaveFile(editorPanel);

        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write(editorPanel.getTextArea().getText());
            changed.remove(editorPanel);
        } catch (IOException e) {
            System.err.println("Could not save an EditorPanels text.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the <code>File</code> in which the contents of the given <code>EditorPanel</code> should be saved
     * if they are changed.
     *
     * @param editorPanel
     *         the <code>EditorPanel</code> whose <code>savedFile</code> is to be returned
     *
     * @return the <code>savedFile</code> for the <code>editorPanel</code>
     */
    private File getSaveFile(EditorPanel editorPanel) {
        return new File(saveDir, editorPanel.getFile().getName());
    }

    /**
     * Removes all <code>DocumentListener</code>s from the given <code>RSyntaxDocument</code> and returns them.
     *
     * @param doc
     *         the <code>RSyntaxDocument</code> to remove listeners from
     *
     * @return the removed listeners
     */
    private DocumentListener[] removeListeners(RSyntaxDocument doc) {
        DocumentListener[] listeners = doc.getDocumentListeners();

        for (DocumentListener listener : listeners) {
            doc.removeDocumentListener(listener);
        }

        return listeners;
    }

    /**
     * Adds all given <code>DocumentListener</code>s to the <code>RSyntaxDocument</code>.
     *
     * @param doc
     *         the <code>RSyntaxDocument</code> to add the <code>DocumentListener</code>s to
     * @param listeners
     *         the <code>DocumentListener</code>s to add
     */
    private void addListeners(RSyntaxDocument doc, DocumentListener[] listeners) {

        for (DocumentListener listener : listeners) {
            doc.addDocumentListener(listener);
        }
    }

    /**
     * Returns the contents of the given <code>File</code> as a <code>String</code>. This assumes UTF-8 encoding.
     *
     * @param f
     *         the <code>File</code> to read
     *
     * @return the contents of the <code>File</code>
     *
     * @throws IOException
     *         if there is an <code>IOException</code> reading the <code>File</code>
     */
    private String readFile(File f) throws IOException {
        return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8).intern();
    }
}
