package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.util.ModifiedRSyntaxTextArea;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * Displays a simple editable document allowing the user to save notes for every <code>QTreeNode</code>.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class NoteEditorPanel extends ExperimentEditorTab {

    public final static String KEY_NOTES = "notes";

    /**
     * Caches for the various layout components to preserve their state when the user switches between nodes.
     */
    private Map<QTreeNode, RTextScrollPane> scrollPanes = new HashMap<>();
    private Map<QTreeNode, RSyntaxTextArea> editAreas = new HashMap<>();

    private QTreeNode selected;

    /**
     * Constructs a new empty <code>NoteEditorPanel</code>.
     */
    public NoteEditorPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    public void load(final QTreeNode selected) {
        this.selected = selected;

        removeAll();
        if (selected == null) {
            return;
        }

        RTextScrollPane scrollPane = scrollPanes.get(this.selected);
        if (scrollPane == null) {
            RSyntaxTextArea editArea;
            RSyntaxDocument doc;

            doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
            try {
                doc.insertString(0, selected.getAttribute(KEY_NOTES).getValue(), null);
            } catch (BadLocationException e) {
                System.err.println("Could not display the notes for " + selected + ".");
            }

            editArea = new ModifiedRSyntaxTextArea(doc);
            editArea.setLineWrap(true);
            editAreas.put(selected, editArea);

            scrollPane = new RTextScrollPane(editArea);
            scrollPanes.put(selected, scrollPane);
        }

        add(scrollPane, BorderLayout.CENTER);
        repaint();
    }

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
