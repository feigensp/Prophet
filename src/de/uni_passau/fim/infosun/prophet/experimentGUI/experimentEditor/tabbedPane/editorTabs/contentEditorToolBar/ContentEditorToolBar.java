package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.Component;
import javax.swing.JToolBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

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

        add(new FontStyleComboBox(textArea));
        add(new FontSizeComboBox(textArea));
        add(new FormComboBox(textArea));
        add(new MacroComboBox(textArea));
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
