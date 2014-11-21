package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar
        .ContentEditorToolBar;
import de.uni_passau.fim.infosun.prophet.util.ModifiedRSyntaxTextArea;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.searchBar.SearchBar;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * Enables the user to edit the HTML content of a <code>QTreeNode</code>.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ContentEditorPanel extends ExperimentEditorTab {

    /**
     * Caches for the various layout components to preserve their state when the user switches between nodes.
     */
    private Map<QTreeNode, JPanel> editPanels = new HashMap<>();
    private Map<QTreeNode, RSyntaxTextArea> editAreas = new HashMap<>();
    private Map<QTreeNode, SearchBar> searchBars = new HashMap<>();

    private QTreeNode selected;

    /**
     * Constructs a new empty <code>ContentEditorPanel</code>.
     */
    public ContentEditorPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    public void load(QTreeNode selected) {
        this.selected = selected;

        removeAll();
        if (selected == null) {
            return;
        }

        JPanel editPanel = editPanels.get(selected);
        if (editPanel == null) {
            SearchBar searchBar;
            RSyntaxDocument doc;
            RSyntaxTextArea editArea;
            RTextScrollPane scrollPane;
            ContentEditorToolBar toolBar;

            editPanel = new JPanel();
            editPanel.setLayout(new BorderLayout());

            doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_HTML);
            try {
                doc.insertString(0, selected.getHtml(), null);
            } catch (BadLocationException e) {
                System.err.println("Could not display the HTML content of " + selected + ".");
            }

            editArea = new ModifiedRSyntaxTextArea(doc);
            editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
            editArea.setLineWrap(true);
            editArea.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent ke) {
                    if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        editArea.replaceSelection("<br>");
                    }
                }
            });
            editAreas.put(selected, editArea);

            toolBar = new ContentEditorToolBar(editArea);
            editPanel.add(toolBar, BorderLayout.NORTH);

            scrollPane = new RTextScrollPane(editArea);
            editPanel.add(scrollPane, BorderLayout.CENTER);

            searchBar = new SearchBar(editArea);
            searchBar.setVisible(false);
            searchBars.put(selected, searchBar);
            editPanel.add(searchBar, BorderLayout.SOUTH);

            ExperimentEditorTabbedPane.recursiveSetOpaque(editPanel);
            editPanels.put(selected, editPanel);
        }

        add(editPanel, BorderLayout.CENTER);
        repaint();
    }

    @Override
    public void save() {
        if (selected != null) {
            RSyntaxTextArea editArea = editAreas.get(selected);
            if (editArea != null) {
                selected.setHtml(editArea.getText());
            }
        }
    }

    /**
     * Shows a <code>SearchBar</code> in this <code>ContentEditorPanel</code> enabling the user to search through
     * the document.
     */
    public void search() {
        SearchBar searchBar = searchBars.get(selected);

        if (searchBar != null) {
            searchBar.setVisible(true);
            searchBar.grabFocus();
        }
    }
}
