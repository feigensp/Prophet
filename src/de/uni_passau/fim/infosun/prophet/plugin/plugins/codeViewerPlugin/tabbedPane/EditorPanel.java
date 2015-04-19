package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.ModifiedRSyntaxTextArea;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
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

    private final File file;

    private CodeViewer viewer;
    private RSyntaxTextArea textArea;
    private RTextScrollPane scrollPane;

    /**
     * Constructs a new <code>EditorPanel</code> displaying the text content of the given <code>File</code>.
     *
     * @param viewer
     *         the <code>CodeViewer</code> this <code>EditorPanel</code> belongs to
     * @param file
     *         the <code>File</code> to display
     */
    public EditorPanel(CodeViewer viewer, File file) {
        this.viewer = viewer;
        this.file = file;
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

    /**
     * Returns the <code>CodeViewer</code> this <code>EditorPanel</code> belongs to.
     *
     * @return the <code>CodeViewer</code> this <code>EditorPanel</code> belongs to
     */
    public CodeViewer getCodeViewer() {
        return viewer;
    }

    /**
     * Returns the <code>RSyntaxTextArea</code> this <code>EditorPanel</code> uses.
     *
     * @return the <code>RSyntaxTextArea</code>
     */
    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    /**
     * Returns the <code>RTextScrollPane</code> this <code>EditorPanel</code> uses.
     *
     * @return the <code>RTextScrollPane</code>
     */
    public RTextScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Returns the <code>File</code> this <code>EditorPanel</code> is displaying.
     *
     * @return the <code>File</code>
     */
    public File getFile() {
        return file;
    }
}
