package de.uni_passau.fim.infosun.prophet.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ModifiedRSyntaxTextArea extends RSyntaxTextArea {

    private static final long serialVersionUID = 1L;

    public ModifiedRSyntaxTextArea() {
        super();
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(int textMode) {
        super(textMode);
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(int rows, int cols) {
        super(rows, cols);
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(RSyntaxDocument doc) {
        super(doc);
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(RSyntaxDocument doc, java.lang.String text, int rows, int cols) {
        super(doc, text, rows, cols);
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(java.lang.String text) {
        super(text);
        overrideCtrlDel();
    }

    public ModifiedRSyntaxTextArea(java.lang.String text, int rows, int cols) {
        super(text, rows, cols);
        overrideCtrlDel();
    }

    private void overrideCtrlDel() {
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK);
        this.getInputMap().put(stroke, "");
        final ModifiedRSyntaxTextArea textArea = this;
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (!textArea.isEditable() || !textArea.isEnabled()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                        return;
                    }
                    try {
                        int start = textArea.getSelectionEnd();
                        int end;
                        try {
                            end = Utilities.getNextWord(textArea, start);
                        } catch (BadLocationException e) {
                            end = textArea.getDocument().getLength();
                        }
                        if (end > start) {
                            textArea.getDocument().remove(start, end - start);
                        }
                    } catch (Exception ex) {
                        UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {

            }

            @Override
            public void keyTyped(KeyEvent arg0) {

            }
        });
    }
}
