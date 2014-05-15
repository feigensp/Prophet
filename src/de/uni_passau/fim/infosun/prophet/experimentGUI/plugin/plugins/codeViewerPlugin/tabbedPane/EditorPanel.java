package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.JPanel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.ModifiedRSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

    private String filePath;
    private RTextScrollPane scrollPane;
    private RSyntaxTextArea textArea;

    /**
     * Create the panel.
     */
    public EditorPanel(File file, String path) {
        this.filePath = path;
        RSyntaxDocument doc = new RSyntaxDocument("text/plain");
        try {
            byte[] buffer = new byte[(int) file.length()];
            FileInputStream fileStream = new FileInputStream(file);
            fileStream.read(buffer);
            doc.insertString(0, new String(buffer), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        textArea = new ModifiedRSyntaxTextArea(doc);
        textArea.setEditable(false);
        textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        scrollPane = new RTextScrollPane(textArea);

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

    public String getFilePath() {
        return filePath;
    }
}
