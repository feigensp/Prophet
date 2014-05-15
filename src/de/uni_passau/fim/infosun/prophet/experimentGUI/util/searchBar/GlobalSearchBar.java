package de.uni_passau.fim.infosun.prophet.experimentGUI.util.searchBar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileTreeModel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * This class adds a JTextPane to a searchbar which is created. With this
 * searchBar the user can search through the text in the JTextPane User
 *
 * @author Robert Futrell, Markus Köppen, Andreas Hasselberg
 */

public class GlobalSearchBar extends JToolBar implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final String CAPTION_HIDE = "X";
    public static final String CAPTION_FIND = UIElementNames.GLOBAL_SEARCH_BAR_SEARCH;
    public static final String CAPTION_REGEX = UIElementNames.GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION;
    public static final String CAPTION_MATCH_CASE = UIElementNames.GLOBAL_SEARCH_BAR_CASE_SENSITIVE;

    public static final String ACTION_HIDE = "Hide";
    public static final String ACTION_NEXT = "Global";

    private JButton hideButton = new JButton(CAPTION_HIDE);
    private JTextField searchField = new JTextField(30);
    private JButton forwardButton = new JButton(CAPTION_FIND);
    private JCheckBox regexCB = new JCheckBox(CAPTION_REGEX);
    private JCheckBox matchCaseCB = new JCheckBox(CAPTION_MATCH_CASE);

    private File file;
    private FileTree tree;
    private CodeViewer viewer;

    private Vector<SearchBarListener> listeners = new Vector<>();

    public void addSearchBarListener(SearchBarListener l) {
        listeners.add(l);
    }

    public void removeSearchBarListener(SearchBarListener l) {
        listeners.remove(l);
    }

    /**
     * Grabs the focus
     */
    public void grabFocus() {
        searchField.grabFocus();
    }

    public GlobalSearchBar(File file, CodeViewer v) {
        viewer = v;
        this.setFloatable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        // Create a toolbar with searching options.
        hideButton.setActionCommand(ACTION_HIDE);
        hideButton.addActionListener(this);
        add(hideButton);
        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    forwardButton.doClick();
                }
            }
        });
        northPanel.add(searchField);
        forwardButton.setActionCommand(ACTION_NEXT);
        forwardButton.addActionListener(this);

        tree = new FileTree(null);
        tree.addFileListener(event -> viewer.getTabbedPane().openFile(event.getFilePath()));
        this.file = file;

        northPanel.add(forwardButton);
        northPanel.add(regexCB);
        northPanel.add(matchCaseCB);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tree, BorderLayout.CENTER);
        add(mainPanel);
    }

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
        try {
            root = new FileTreeNode(file);
        } catch (FileNotFoundException e1) {
            tree.getTree().setModel(new DefaultTreeModel(null));
            return;
        }

        if (getNextLeaf(root) == null) {
            root.removeAllChildren();
        } else {
            boolean forward = true;
            boolean matchCase = matchCaseCB.isSelected();
            boolean wholeWord = false;
            boolean regex = regexCB.isSelected();

            FileTreeNode current = getNextLeaf(root);
            FileTreeNode delete = null;

            RSyntaxTextArea textArea = new RSyntaxTextArea();

            while (current != null) {
//				System.out.println("CURR: "+current.getFilePath());
                if (current.isFile()) {
                    try {
                        String path = file.getPath() + current.getFilePath();
                        File currentFile = new File(path);
                        byte[] buffer = new byte[(int) (currentFile).length()];
                        FileInputStream fileStream = new FileInputStream(currentFile);
                        fileStream.read(buffer);
                        textArea.setText(new String(buffer));
                        textArea.setCaretPosition(0);

                        SearchContext searchContext = new SearchContext();
                        searchContext.setSearchFor(text);
                        searchContext.setSearchForward(forward);
                        searchContext.setMatchCase(matchCase);
                        searchContext.setWholeWord(wholeWord);
                        searchContext.setRegularExpression(regex);

                        boolean found = SearchEngine.find(textArea, searchContext);
//					    System.out.println("-- found: "+found);
                        if (!found) {
                            delete = current;
                        }
                    } catch (Exception e) {
//						System.out.println("-- exception");
                        delete = current;
                    }
                } else {
//					System.out.println("-- no file");
                    delete = current;
                }
                current = getNextLeaf(current);
                while (delete != null) {
                    FileTreeNode parent = (FileTreeNode) delete.getParent();
//					System.out.println("-- DEL: "+delete.getFilePath());
                    delete.removeFromParent();
                    if (parent != null && parent.getChildCount() == 0) {
                        delete = parent;
                    } else {
                        delete = null;
                    }
                }
            }
        }

        tree.getTree().setModel(new FileTreeModel(root));

        for (SearchBarListener l : listeners) {
            l.searched(command, text, root.getChildCount() > 0);
        }
    }

    public JButton getHideButton() {
        return hideButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getForwardButton() {
        return forwardButton;
    }

    public JCheckBox getRegexCB() {
        return regexCB;
    }

    public JCheckBox getMatchCaseCB() {
        return matchCaseCB;
    }

    public FileTree getTree() {
        return tree;
    }

    private FileTreeNode getNextLeaf(FileTreeNode node) {
        do {
            node = (FileTreeNode) node.getNextNode();
        } while (node != null && !node.isLeaf());
        return node;
    }
}
