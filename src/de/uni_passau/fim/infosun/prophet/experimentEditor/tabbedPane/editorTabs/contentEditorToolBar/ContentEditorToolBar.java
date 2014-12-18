package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <code>JToolBar</code> for the <code>ContentEditorPanel</code> that contains several <code>JComboBox</code>
 * subclasses. The <code>JComboBox</code> instances provide functionality for inserting generic HTML components into
 * the <code>RSyntaxTextArea</code> the <code>ExperimentEditor</code> uses.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ContentEditorToolBar extends JToolBar {

    /**
     * Constructs a new <code>ContentEditorToolBar</code> whose <code>JComboBox</code> children affect the given
     * <code>RSyntaxTextArea</code>.
     *
     * @param textArea
     *         the <code>RSyntaxTextArea</code> this <code>ContentEditorToolBar</code> affects
     */
    public ContentEditorToolBar(RSyntaxTextArea textArea) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

        add(new FontStyleComboBox(textArea));
        addSeparator();
        add(new FontSizeComboBox(textArea));
        addSeparator();
        add(new FormComboBox(textArea));
        addSeparator();
        add(new MacroComboBox(textArea));
        addSeparator();

        JButton prettyPrint = new JButton(UIElementNames.getLocalized("FORMAT_BUTTON_CAPTION"));
        prettyPrint.addActionListener(event -> {
            Document doc = Jsoup.parseBodyFragment(textArea.getText());
            doc.outputSettings().prettyPrint(true);
            textArea.setText(doc.body().html());
            textArea.setCaretPosition(0);
        });

        add(prettyPrint);
    }

    /**
     * Overwritten to enable/disable the sub-components of this <code>JToolBar</code> when it is enabled/disabled.
     *
     * @param enabled
     *         true if this component should be enabled, false otherwise
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (Component c : this.getComponents()) {
            c.setEnabled(enabled);
        }
    }
}
