package de.uni_passau.fim.infosun.prophet.util.searchBar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTreeNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * This class adds a JTextPane to a searchbar which is created. With this
 * searchBar the user can search through the text in the JTextPane User
 *
 * @author Robert Futrell, Markus Köppen, Andreas Hasselberg
 */
public class GlobalSearchBar extends JToolBar implements ActionListener {

    public static final String CAPTION_HIDE = "X";
    public static final String CAPTION_FIND = getLocalized("GLOBAL_SEARCH_BAR_SEARCH");
    public static final String CAPTION_REGEX = getLocalized("GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION");
    public static final String CAPTION_MATCH_CASE = getLocalized("GLOBAL_SEARCH_BAR_CASE_SENSITIVE");

    public static final String ACTION_HIDE = "Hide";
    public static final String ACTION_FIND = "Global";

    private JButton hideButton;
    private JTextField searchField;
    private JButton findButton;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;

    private CodeViewer codeViewer;
    private List<SearchBarListener> listeners;

    private TreeCellRenderer oldRenderer;

    private static class Highlighter extends DefaultTreeCellRenderer {

        Set<FileTreeNode> highlightedNodes;

        public Highlighter(Set<FileTreeNode> highlightedNodes) {
            this.highlightedNodes = highlightedNodes;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof FileTreeNode && highlightedNodes.contains(value)) {
                setForeground(Color.red);
            }

            return this;
        }
    }

    public GlobalSearchBar(CodeViewer codeViewer) {
        setFloatable(false);

        this.codeViewer = codeViewer;
        this.listeners = new ArrayList<>();

        hideButton = new JButton(CAPTION_HIDE);
        hideButton.setActionCommand(ACTION_HIDE);
        hideButton.addActionListener(this);
        add(hideButton);
        addSeparator(new Dimension(5, 0));
        
        findButton = new JButton(CAPTION_FIND);
        findButton.setActionCommand(ACTION_FIND);
        findButton.addActionListener(this);
        add(findButton);
        addSeparator(new Dimension(5, 0));

        searchField = new JTextField(30);
        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    findButton.doClick();
                }
            }
        });
        add(searchField);
        addSeparator(new Dimension(5, 0));
        
        regexCB = new JCheckBox(CAPTION_REGEX);
        add(regexCB);
        addSeparator(new Dimension(5, 0));

        matchCaseCB = new JCheckBox(CAPTION_MATCH_CASE);
        add(matchCaseCB);
    }

    /**
     * Adds a <code>SearchBarListener</code> to this <code>GlobalSearchBar</code>.
     *
     * @param listener
     *         the <code>SearchBarListener</code> to add
     */
    public void addSearchBarListener(SearchBarListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a <code>SearchBarListener</code> from this <code>GlobalSearchBar</code>.
     *
     * @param listener
     *         the <code>SearchBarListener</code> to remove
     */
    public void removeSearchBarListener(SearchBarListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void grabFocus() {
        searchField.grabFocus();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();
        FileTree fileTree = codeViewer.getFileTree();

        if (command.equals(ACTION_HIDE)) {

            if (oldRenderer != null) {
                fileTree.setCellRenderer(oldRenderer);
                fileTree.repaint();
                oldRenderer = null;
            }

            setVisible(false);
            return;
        }

        String text = searchField.getText();
        if (text.isEmpty()) {
            return;
        }

        SearchContext searchContext = new SearchContext();
        searchContext.setSearchFor(text);
        searchContext.setSearchForward(true);
        searchContext.setMatchCase(matchCaseCB.isSelected());
        searchContext.setWholeWord(false);
        searchContext.setRegularExpression(regexCB.isSelected());

        FileTreeNode treeRoot = fileTree.getModel().getRoot();
        Stream<FileTreeNode> fileNodes = treeRoot.preOrder().stream().filter(FileTreeNode::isFile);
        Predicate<FileTreeNode> textFilter = node -> containsText(node, searchContext);
        Set<FileTreeNode> foundNodes = fileNodes.filter(textFilter).collect(Collectors.toSet());

        if (!foundNodes.isEmpty()) {
            if (oldRenderer != null) {
                oldRenderer = fileTree.getCellRenderer();
            }

            fileTree.setCellRenderer(new Highlighter(foundNodes));
            fileTree.repaint();

            FileTreeNode[] path;
            for (FileTreeNode node : foundNodes) {
                path = fileTree.getModel().buildPath(node.getFile());
                fileTree.makeVisible(new TreePath(path));
            }
        }

        for (SearchBarListener listener : listeners) {
            listener.searched(command, text, !foundNodes.isEmpty());
        }
    }

    private boolean containsText(FileTreeNode node, SearchContext context) {

        if (!node.isFile()) {
            return false;
        }

        JTextArea textArea;

        try {
            textArea = new RSyntaxTextArea(new String(Files.readAllBytes(node.getFile().toPath())));
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            System.err.println("Could not search through the contents of " + node.getFile().getName());
            return false;
        }

        return SearchEngine.find(textArea, context).wasFound();
    }

    public JCheckBox getRegexCB() {
        return regexCB;
    }
}
