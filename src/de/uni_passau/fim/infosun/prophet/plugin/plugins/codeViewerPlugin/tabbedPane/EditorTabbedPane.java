package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewerPluginList;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * The right-side <code>JTabbedPane</code> of the <code>CodeViewer</code>. Displays tabs containing
 * <code>EditorPanel</code> instances.
 */
public class EditorTabbedPane extends JTabbedPane {

	private CodeViewer codeViewer;
    private Map<File, EditorPanel> panels;

    /**
     * Constructs a new <code>EditorTabbedPane</code> using the given <code>Recorder</code>.
     *
     * @param codeViewer
     *         the <code>CodeViewer</code> this <code>EditorPanel</code> belongs to
     */
    public EditorTabbedPane(CodeViewer codeViewer) {
        super(SwingConstants.TOP);

		this.codeViewer = codeViewer;
        this.panels = new HashMap<>();

        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    /**
     * Opens a new <code>EditorPanel</code> tab displaying the text content of the given <code>File</code>. If a
     * tab for <code>file</code> is already open it will be selected.
     *
     * @param file
     *         the <code>File</code> to display
     */
    public void openFile(File file) {
        EditorPanel panel;

        if (file.exists()) {
            panel = panels.get(file);

            if (panel == null) {
                panel = new EditorPanel(file);

                CodeViewerPluginList.onEditorPanelCreate(codeViewer, panel);

                panels.put(file, panel);
                add(file.getName(), panel);
                setTabComponentAt(indexOfComponent(panel), new ButtonTabComponent(this, panel));
            }

            setSelectedComponent(panel);
            panel.grabFocus();
        } else {
            String msg = getLocalized("EDITOR_TABBED_PANE_MESSAGE_ERROR_COULD_NOT_OPEN_FILE") + " : " + file.getPath();
            String title = getLocalized("EDITOR_TABBED_PANE_MESSAGE_TITLE_ERROR");

            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Closes the tab displaying <code>file</code> if there is one.
     *
     * @param file
     *         the <code>File</code> whose tab is to be closed
     */
    public void closeFile(File file) {
        closeEditorPanel(panels.get(file));
    }

    /**
     * Closes the given <code>EditorPanel</code> if it is currently being displayed in a tab by this
     * <code>EditorTabbedPane</code>.
     *
     * @param panel
     *         the <code>EditorPanel</code> to close
     */
    public void closeEditorPanel(EditorPanel panel) {
        boolean panelFound;

        if (panel != null) {
            synchronized (getTreeLock()) {
                panelFound = Arrays.asList(getComponents()).contains(panel);
            }

            if (panelFound) {
                CodeViewerPluginList.onEditorPanelClose(codeViewer, panel);

                panels.remove(panel.getFile());
                remove(panel);
            }
        }
    }
}
