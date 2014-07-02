package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.ModifiedRSyntaxTextArea;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * A panel for taking notes for every node in the tree.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
@SuppressWarnings("serial")
public class NoteEditorPanel extends ExperimentEditorTab {

    private Map<QTreeNode, RTextScrollPane> scrollPanes;
    private Map<QTreeNode, RSyntaxTextArea> editAreas = new HashMap<>();
    private QTreeNode selected;
    public final static String KEY_NOTES = "notes";

    /**
     * Constructor
     */
    public NoteEditorPanel() {
        setLayout(new BorderLayout());
        this.setOpaque(false);
        scrollPanes = new HashMap<>();
    }

    /**
     * loads the notes for a selected node into the tab, called by EditorTabbedPane
     */
    @Override
    public void load(final QTreeNode selected) {
        this.selected = selected;
        this.removeAll();
        this.updateUI();
        if (selected != null) {
            RTextScrollPane scrollPane = scrollPanes.get(this.selected);
            if (scrollPane == null) {
                RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
                try {
                    doc.insertString(0, selected.getAttribute(KEY_NOTES).getValue(), null);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
                editAreas.put(selected, editArea);
                editArea.setLineWrap(true);

                scrollPane = new RTextScrollPane(editArea);
                scrollPanes.put(selected, scrollPane);
            }
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    /**
     * saves any changes, called by EditorTabbedPane
     */
    @Override
    public void save() {
        if (selected != null) {
            RSyntaxTextArea editArea = editAreas.get(selected);

            if (editArea != null) {
                selected.getAttribute(KEY_NOTES).setValue(editArea.getText());
            }
        }
    }
}
