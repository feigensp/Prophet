package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.ModifiedRSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * A <code>JPanel</code> displaying the text content of a file in a <code>RSyntaxTextArea</code> enclosed in a
 * <code>RTextScrollPane</code>.
 */
public class EditorPanel extends JPanel {

    private static final Font FONT = new Font("monospaced", Font.PLAIN, 12);

    private String path;
    private RSyntaxTextArea textArea;
    private RTextScrollPane scrollPane;

    public EditorPanel(File file) {
        this.path = path;
        this.textArea = new ModifiedRSyntaxTextArea();
        this.scrollPane = new RTextScrollPane(textArea);

        this.textArea.setEditable(false);
        this.textArea.setFont(FONT);

        RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);

        try {
            doc.insertString(0, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8), null);
        } catch (BadLocationException | IOException e) {
            System.err.println("Could not read " + file.getName());
            System.err.println(e.getMessage());
        }

        this.textArea.setDocument(doc);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void grabFocus() {
        textArea.grabFocus();
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public RTextScrollPane getScrollPane() {
        return scrollPane;
    }

    public String getPath() {
        return path;
    }
}
