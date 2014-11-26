package de.uni_passau.fim.infosun.prophet.util.searchBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * A <code>JToolBar</code> containing a controls to enable searching for strings or regular expressions in a
 * given <code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @author Markus Köppen
 * @author Andreas Hasselberg
 * @author Georg Seibt
 */
public class SearchBar extends JToolBar implements ActionListener {

    private static final String CAPTION_HIDE = "X";
    private static final String CAPTION_NEXT = UIElementNames.getLocalized("SEARCH_BAR_SEARCH_FORWARD");
    private static final String CAPTION_PREVIOUS = UIElementNames.getLocalized("SEARCH_BAR_SEARCH_BACKWARD");
    private static final String CAPTION_REGEX = UIElementNames.getLocalized("SEARCH_BAR_REGULAR_EXPRESSION");
    private static final String CAPTION_MATCH_CASE = UIElementNames.getLocalized("SEARCH_BAR_CASE_SENSITIVE");
    private static final String MESSAGE_NOT_FOUND = UIElementNames.getLocalized("SEARCH_BAR_MESSAGE_TEXT_NOT_FOUND");

    public static final String ACTION_HIDE = "Hide";
    public static final String ACTION_NEXT = "FindNext";
    public static final String ACTION_PREVIOUS = "FindPrevious";

    private JButton hideButton;
    private JButton nextButton;
    private JButton previousButton;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;
    private JTextField searchField;

    private RSyntaxTextArea textArea;
    private List<SearchBarListener> listeners;

    /**
     * Constructs a new <code>SearchBar</code> searching through the contents of the given <code>RSyntaxTextArea</code>.
     *
     * @param textArea the <code>RSyntaxTextArea</code> to search through
     */
    public SearchBar(RSyntaxTextArea textArea) {
        setFloatable(false);

        this.textArea = textArea;
        this.listeners = new ArrayList<>();

        hideButton = new JButton(CAPTION_HIDE);
        hideButton.setActionCommand(ACTION_HIDE);
        hideButton.addActionListener(this);
        add(hideButton);

        nextButton = new JButton(CAPTION_NEXT);
        nextButton.setActionCommand(ACTION_NEXT);
        nextButton.addActionListener(this);
        add(nextButton);

        previousButton = new JButton(CAPTION_PREVIOUS);
        previousButton.setActionCommand(ACTION_PREVIOUS);
        previousButton.addActionListener(this);
        add(previousButton);

        searchField = new JTextField(30);
        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    nextButton.doClick();
                }
            }
        });
        add(searchField);

        regexCB = new JCheckBox(CAPTION_REGEX);
        add(regexCB);

        matchCaseCB = new JCheckBox(CAPTION_MATCH_CASE);
        add(matchCaseCB);
    }

    /**
     * Adds a <code>SearchBarListener</code> to this <code>SearchBar</code>.
     *
     * @param listener the <code>SearchBarListener</code> to add
     */
    public void addSearchBarListener(SearchBarListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a <code>SearchBarListener</code> from this <code>SearchBar</code>.
     *
     * @param listener the <code>SearchBarListener</code> to remove
     */
    public void removeSearchBarListener(SearchBarListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        String text = searchField.getText();

        if (command.equals(ACTION_HIDE)) {
            setVisible(false);
            return;
        }

        if (text.isEmpty()) {
            return;
        }

        boolean matchCase = matchCaseCB.isSelected();
        boolean regex = regexCB.isSelected();
        boolean forward;

        switch (command) {
            case ACTION_NEXT:
                forward = true;
                break;
            case ACTION_PREVIOUS:
                forward = false;
                break;
            default:
                return;
        }

        SearchContext searchContext = new SearchContext();
        searchContext.setSearchFor(text);
        searchContext.setSearchForward(forward);
        searchContext.setMatchCase(matchCase);
        searchContext.setWholeWord(false);
        searchContext.setRegularExpression(regex);

        boolean found = SearchEngine.find(textArea, searchContext).wasFound();

        if (!found) {
            JOptionPane.showMessageDialog(this, MESSAGE_NOT_FOUND);
        }

        for (SearchBarListener listener : listeners) {
            listener.searched(command, text, found);
        }
    }

    @Override
    public void grabFocus() {
        searchField.grabFocus();
    }

    public JCheckBox getRegexCB() {
        return regexCB;
    }
}
