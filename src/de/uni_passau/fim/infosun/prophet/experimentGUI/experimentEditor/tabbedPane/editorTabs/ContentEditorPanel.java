package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar
        .ContentEditorToolBar;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.ModifiedRSyntaxTextArea;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.searchBar.SearchBar;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * The main tab for editing the content of a node within the ExperimentEditor
 *
 * @author Andreas Hasselberg
 * @author Markus K�ppen
 */
@SuppressWarnings("serial")
public class ContentEditorPanel extends ExperimentEditorTab {

    /**
     * Storage of all EditorPanels, let the user continue where he left the node/tab
     */
    private Map<QTreeNode, JPanel> editPanels = new HashMap<>();
    /**
     * Storage of all text editor areas, used to save the current work
     */
    private Map<QTreeNode, RSyntaxTextArea> editAreas = new HashMap<>();
    /**
     * Storage of all search bars, used to delegate the search operation to the correct bar
     */
    private Map<QTreeNode, SearchBar> searchBars = new HashMap<>();
    /**
     * the currently selected node
     */
    private QTreeNode selected;

    /**
     * Constructor
     */
    public ContentEditorPanel() {
        setLayout(new BorderLayout());
        this.setOpaque(false);
    }

    /**
     * Called by EditorTabbedPane to indicate a possible node change, (re)loads the panel
     */
    public void activate(QTreeNode s) {
        selected = s;
        this.removeAll();
        this.updateUI();
        if (selected != null) {
            JPanel editPanel = editPanels.get(s);
            if (editPanel == null) {
                editPanel = new JPanel();
                editPanel.setLayout(new BorderLayout());

                RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_HTML);
                try {
                    doc.insertString(0, s.getHtml(), null);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                final RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
                editAreas.put(s, editArea);

                editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
                editArea.addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent ke) {
                        if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
                            editArea.replaceSelection("<br>");
                        }
                    }
                });

                ContentEditorToolBar toolBar = new ContentEditorToolBar(editArea);
                editPanel.add(toolBar, BorderLayout.NORTH);
                RTextScrollPane scrollPane = new RTextScrollPane(editArea);
                editArea.setLineWrap(true);
                editPanel.add(scrollPane, BorderLayout.CENTER);
                SearchBar searchBar = new SearchBar(editArea);
                searchBar.setVisible(false);
                searchBars.put(selected, searchBar);

                editPanel.add(searchBar, BorderLayout.SOUTH);
                ExperimentEditorTabbedPane.recursiveSetOpaque(editPanel);
                editPanels.put(s, editPanel);
            }
            add(editPanel, BorderLayout.CENTER);
        }
    }

    /**
     * saves the current changes to the tree, called by EditorTabbedPane
     */
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
     * makes the current search bar visible.
     */
    public void search() {
        SearchBar searchBar = searchBars.get(selected);
        if (searchBar != null) {
            searchBar.setVisible(true);
            searchBar.grabFocus();
        }
    }
}
