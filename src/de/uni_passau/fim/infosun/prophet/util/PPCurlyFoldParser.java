package de.uni_passau.fim.infosun.prophet.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;

public class PPCurlyFoldParser extends CurlyFoldParser {

    private static char[] C_if = "#if".toCharArray();
    private static char[] C_ifdef = "#ifdef".toCharArray();
    private static char[] C_ifndef = "#ifndef".toCharArray();
    private static char[] C_else = "#else".toCharArray();
    private static char[] C_endif = "#endif".toCharArray();

    @Override
    public List<Fold> getFolds(RSyntaxTextArea textArea) {
        List<Fold> folds = super.getFolds(textArea);
        Queue<Fold> stack = new LinkedList<>();

        for (int i = 0; i < textArea.getLineCount(); i++) {

            Token token = textArea.getTokenListForLine(i);

            while (token != null && token.isPaintable()) {
                if (token.getType() == TokenTypes.PREPROCESSOR) {

                    try {
                        if (token.is(C_if) || token.is(C_ifdef) || token.is(C_ifndef)) {
                            Fold stackTop = stack.poll();

                            if (stackTop == null) {
                                Fold newFold = new Fold(FoldType.CODE, textArea, token.getEndOffset());
                                folds.add(newFold);
                                stack.offer(newFold);
                            } else {
                                stack.offer(stackTop.createChild(FoldType.CODE, token.getEndOffset()));
                            }
                        }

                        if (token.is(C_else)) {
                            Fold newFold = new Fold(FoldType.CODE, textArea, token.getEndOffset());
                            folds.add(newFold);
                            stack.poll().setEndOffset(token.getOffset() - 1);
                            stack.offer(newFold);
                        }

                        if (token.is(C_endif)) {
                            stack.poll().setEndOffset(token.getOffset() - 1);
                        }

                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
                token = token.getNextToken();
            }
        }

        folds.sort((f1, f2) -> Integer.compare(f1.getStartOffset(), f2.getStartOffset()));

        return folds;
    }
}
