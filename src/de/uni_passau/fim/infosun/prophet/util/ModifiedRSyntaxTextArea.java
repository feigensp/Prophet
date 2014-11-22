package de.uni_passau.fim.infosun.prophet.util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.Utilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * An <code>RSyntaxTextArea</code> that adds an Strg + Del <code>Action</code> that deletes the word at the caret.
 */
public class ModifiedRSyntaxTextArea extends RSyntaxTextArea {

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     */
    public ModifiedRSyntaxTextArea() {
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param doc
     *         The document for the editor.
     */
    public ModifiedRSyntaxTextArea(RSyntaxDocument doc) {
        super(doc);
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param text
     *         The initial text to display.
     */
    public ModifiedRSyntaxTextArea(String text) {
        super(text);
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param rows
     *         The number of rows to display.
     * @param cols
     *         The number of columns to display.
     *
     * @throws IllegalArgumentException
     *         If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public ModifiedRSyntaxTextArea(int rows, int cols) {
        super(rows, cols);
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param text
     *         The initial text to display.
     * @param rows
     *         The number of rows to display.
     * @param cols
     *         The number of columns to display.
     *
     * @throws IllegalArgumentException
     *         If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public ModifiedRSyntaxTextArea(String text, int rows, int cols) {
        super(text, rows, cols);
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param doc
     *         The document for the editor.
     * @param text
     *         The initial text to display.
     * @param rows
     *         The number of rows to display.
     * @param cols
     *         The number of columns to display.
     *
     * @throws IllegalArgumentException
     *         If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public ModifiedRSyntaxTextArea(RSyntaxDocument doc, String text, int rows, int cols) {
        super(doc, text, rows, cols);
        overrideCtrlDel();
    }

    /**
     * Creates a new <code>RSyntaxTextArea</code>.
     *
     * @param textMode
     *         Either <code>INSERT_MODE</code> or
     *         <code>OVERWRITE_MODE</code>.
     */
    public ModifiedRSyntaxTextArea(int textMode) {
        super(textMode);
        overrideCtrlDel();
    }

    private void overrideCtrlDel() {
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK);
        final String actionName = "delWord";

        getInputMap().put(stroke, actionName);
        getActionMap().put(actionName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent event) {
                ModifiedRSyntaxTextArea textArea = ModifiedRSyntaxTextArea.this;

                if (!textArea.isEditable() || !textArea.isEnabled()) {
                    return;
                }

                try {
                    int caret = textArea.getCaretPosition();
                    int start = Utilities.getPreviousWord(textArea, caret);
                    int end = Utilities.getWordEnd(textArea, start);

                    if ((start <= caret) && (caret < end)) {
                        textArea.getDocument().remove(start, end - start);
                    }
                } catch (Exception ignored) {
                    // occurs when there are no 'words' in the textArea
                }
            }
        });
    }
}
