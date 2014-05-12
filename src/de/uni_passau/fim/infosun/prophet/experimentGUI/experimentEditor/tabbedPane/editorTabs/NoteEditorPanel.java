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
 * @author Markus K�ppen
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
    public void activate(final QTreeNode s) {
        selected = s;
        this.removeAll();
        this.updateUI();
        if (s != null) {
            RTextScrollPane scrollPane = scrollPanes.get(selected);
            if (scrollPane == null) {
                RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
                try {
                    doc.insertString(0, s.getAttribute(KEY_NOTES).getValue(), null);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
                editAreas.put(s, editArea);
                editArea.setLineWrap(true);

                scrollPane = new RTextScrollPane(editArea);
                scrollPanes.put(s, scrollPane);
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
