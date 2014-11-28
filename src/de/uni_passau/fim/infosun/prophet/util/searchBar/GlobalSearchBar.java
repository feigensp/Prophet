package de.uni_passau.fim.infosun.prophet.util.searchBar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultTreeModel;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTreeModel;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTreeNode;

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
    public static final String ACTION_NEXT = "Global";

    private JButton hideButton;
    private JTextField searchField;
    private JButton forwardButton;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;

    private File file;
    private FileTree tree;
    private CodeViewer codeViewer;

    private List<SearchBarListener> listeners;

    public GlobalSearchBar(File file, CodeViewer codeViewer) {
        setFloatable(false);

        this.file = file;
        this.codeViewer = codeViewer;
        this.listeners = new ArrayList<>();

        this.tree = new FileTree(null);
        this.tree.addFileListener(event -> this.codeViewer.getTabbedPane().openFile(event.getFile()));

        hideButton = new JButton(CAPTION_HIDE);
        hideButton.setActionCommand(ACTION_HIDE);
        hideButton.addActionListener(this);
        add(hideButton);

        searchField = new JTextField(30);
        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    forwardButton.doClick();
                }
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.add(searchField);

        forwardButton = new JButton(CAPTION_FIND);
        forwardButton.setActionCommand(ACTION_NEXT);
        forwardButton.addActionListener(this);
        northPanel.add(forwardButton);

        regexCB = new JCheckBox(CAPTION_REGEX);
        northPanel.add(regexCB);

        matchCaseCB = new JCheckBox(CAPTION_MATCH_CASE);
        northPanel.add(matchCaseCB);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        add(mainPanel);
    }

    /**
     * Adds a <code>SearchBarListener</code> to this <code>GlobalSearchBar</code>.
     *
     * @param listener the <code>SearchBarListener</code> to add
     */
    public void addSearchBarListener(SearchBarListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a <code>SearchBarListener</code> from this <code>GlobalSearchBar</code>.
     *
     * @param listener the <code>SearchBarListener</code> to remove
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

        if (command.equals(ACTION_HIDE)) {
            setVisible(false);
            return;
        }

        String text = searchField.getText();
        if (text.length() == 0) {
            return;
        }

        FileTreeNode root;

        if (file.exists()) {
            root = new FileTreeNode(file);
        } else {
            tree.setModel(new DefaultTreeModel(null));
            return;
        }

//        if (getNextLeaf(root) == null) {
//            root.removeAllChildren();
//        } else {
//            boolean forward = true;
//            boolean matchCase = matchCaseCB.isSelected();
//            boolean wholeWord = false;
//            boolean regex = regexCB.isSelected();
//
//            FileTreeNode current = getNextLeaf(root);
//            FileTreeNode delete = null;
//
//            RSyntaxTextArea textArea = new RSyntaxTextArea();
//
//            while (current != null) {
//
//                if (current.isFile()) {
//                    try {
//                        String path = file.getPath() + current.getFilePath();
//                        File currentFile = new File(path);
//                        byte[] buffer = new byte[(int) (currentFile).length()];
//                        FileInputStream fileStream = new FileInputStream(currentFile);
//                        fileStream.read(buffer);
//                        textArea.setText(new String(buffer));
//                        textArea.setCaretPosition(0);
//
//                        SearchContext searchContext = new SearchContext();
//                        searchContext.setSearchFor(text);
//                        searchContext.setSearchForward(forward);
//                        searchContext.setMatchCase(matchCase);
//                        searchContext.setWholeWord(wholeWord);
//                        searchContext.setRegularExpression(regex);
//
//                        if (!SearchEngine.find(textArea, searchContext).wasFound()) {
//                            delete = current;
//                        }
//                    } catch (Exception e) {
//                        delete = current;
//                    }
//                } else {
//                    delete = current;
//                }
//
//                current = getNextLeaf(current);
//                while (delete != null) {
//                    FileTreeNode parent = delete.getParent();
//
//                    delete.removeFromParent();
//                    if (parent != null && parent.getChildCount() == 0) {
//                        delete = parent;
//                    } else {
//                        delete = null;
//                    }
//                }
//            }
//        }

        tree.setModel(new FileTreeModel(root));

        for (SearchBarListener l : listeners) {
            l.searched(command, text, root.getChildCount() > 0);
        }
    }

    public JCheckBox getRegexCB() {
        return regexCB;
    }

//    private FileTreeNode getNextLeaf(FileTreeNode node) {
//        do {
//            node = node.getNextNode();
//        } while (node != null && !node.isFile());
//        return node;
//    }
}
