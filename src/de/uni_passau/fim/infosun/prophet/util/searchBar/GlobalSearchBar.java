package de.uni_passau.fim.infosun.prophet.util.searchBar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;
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
 * A <code>JToolBar</code> containing controls to enable searching for strings or regular expressions globally in every
 * <code>File</code> displayed by the <code>FileTree</code> of a given <code>CodeViewer</code>. After a search
 * all nodes in which the search expression was found will be highlighted and made visible in the <code>FileTree</code>.
 *
 * @author Robert Futrell
 * @author Markus Köppen
 * @author Andreas Hasselberg
 * @author Georg Seibt
 */
public class GlobalSearchBar extends JToolBar implements ActionListener {

    public static final String CAPTION_HIDE = "X";
    public static final String CAPTION_FIND = getLocalized("GLOBAL_SEARCH_BAR_SEARCH");
    public static final String CAPTION_REGEX = getLocalized("GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION");
    public static final String CAPTION_MATCH_CASE = getLocalized("GLOBAL_SEARCH_BAR_CASE_SENSITIVE");

    public static final String ACTION_HIDE = "Hide";
    public static final String ACTION_FIND = "Global";

    private JButton hideButton;
    private JButton findButton;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;
    private JTextField searchField;

    private CodeViewer codeViewer;
    private List<SearchBarListener> listeners;
    private TreeCellRenderer oldRenderer;

    /**
     * A <code>DefaultTreeCellRenderer</code> highlights cells if the their nodes are in a given <code>Set</code>.
     * It will be used with the <code>FileTree</code> that is part of a <code>CodeViewer</code> and therefore
     * accepts sets of <code>FileTreeNode</code>s.
     */
    private static class Highlighter extends DefaultTreeCellRenderer {

        private Set<FileTreeNode> highlightedNodes;

        /**
         * Constructs a new <code>Highlighter</code> highlighting the nodes in the given set.
         *
         * @param highlightedNodes the nodes to highlight
         */
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

    /**
     * Constructs a new <code>GlobalSearchBar</code> searching through the files displayed by the given
     * <code>CodeViewer</code>.
     *
     * @param codeViewer the <code>CodeViewer</code> whose nodes are to be searched through
     */
    public GlobalSearchBar(CodeViewer codeViewer) {
        setFloatable(false);

        this.codeViewer = codeViewer;
        this.listeners = new ArrayList<>();

        Dimension sepDim = new Dimension(5, 0);

        hideButton = new JButton(CAPTION_HIDE);
        hideButton.setActionCommand(ACTION_HIDE);
        hideButton.addActionListener(this);
        add(hideButton);

        findButton = new JButton(CAPTION_FIND);
        findButton.setActionCommand(ACTION_FIND);
        findButton.addActionListener(this);
        add(findButton);
        addSeparator(sepDim);

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
        addSeparator(sepDim);

        regexCB = new JCheckBox(CAPTION_REGEX);
        add(regexCB);
        addSeparator(sepDim);

        matchCaseCB = new JCheckBox(CAPTION_MATCH_CASE);
        add(matchCaseCB);
        addSeparator(sepDim);
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
        Predicate<FileTreeNode> textFilter = node -> contains(node, searchContext);
        Set<FileTreeNode> foundNodes = fileNodes.filter(textFilter).collect(Collectors.toSet());

        if (!foundNodes.isEmpty()) {
            if (oldRenderer == null) {
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

    /**
     * Checks whether using the given <code>context</code> on the text of the file represented by <code>node</code>
     * yields a result.
     *
     * @param node the node whose text content is to be searched through
     * @param context the <code>SearchContext</code> to pass to the <code>SearchContext</code>
     * @return <code>true</code> iff <code>SearchEngine</code> finds a result in the <code>FileTreeNode</code>s text
     */
    private boolean contains(FileTreeNode node, SearchContext context) {

        if (!node.isFile()) {
            return false;
        }

        JTextArea textArea;

        try {
            textArea = new RSyntaxTextArea(new String(Files.readAllBytes(node.getFile().toPath()), StandardCharsets.UTF_8));
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            System.err.println("Could not search through the contents of " + node.getFile().getName());
            return false;
        }

        return SearchEngine.find(textArea, context).wasFound();
    }

    /**
     * Returns the <code>JCheckBox</code> determining whether to use regular expressions when searching.
     *
     * @return the <code>JCheckBox</code>
     */
    public JCheckBox getRegexCB() {
        return regexCB;
    }
}
