package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.awt.Component;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {

    private File showDir;
    private Recorder recorder;
    Set<EditorPanel> editorPanels;

    public EditorTabbedPane(File showDir, Recorder recorder) {
        super(JTabbedPane.TOP);

        this.showDir = showDir;
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.editorPanels = new HashSet<>();
        this.recorder = recorder;
    }

    public void openFile(String path) {
        if (!path.startsWith(System.getProperty("file.separator"))) {
            path = System.getProperty("file.separator") + path;
        }
        EditorPanel e = getEditorPanel(path);
        if (e != null) {
            this.setSelectedComponent(e);
            e.grabFocus();
            return;
        }
        File file = new File(showDir.getPath() + path);
        if (file.exists()) {
            EditorPanel myPanel = new EditorPanel(file, path);
            recorder.onEditorPanelCreate(myPanel);
            CodeViewerPluginList.onEditorPanelCreate(myPanel);
            add(file.getName(), myPanel);
            this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(this, myPanel));
            this.setSelectedComponent(myPanel);
            myPanel.grabFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                    UIElementNames.get("EDITOR_TABBED_PANE_MESSAGE_ERROR_COULD_NOT_OPEN_FILE") + ": " + path,
                    UIElementNames.get("EDITOR_TABBED_PANE_MESSAGE_TITLE_ERROR"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeFile(String path) {
        closeEditorPanel(getEditorPanel(path));
    }

    public void closeEditorPanel(EditorPanel editorPanel) {
        if (editorPanel != null) {
            CodeViewerPluginList.onEditorPanelClose(editorPanel);
            recorder.onEditorPanelClose(editorPanel);
            this.remove(editorPanel);
        }
    }

    public EditorPanel getEditorPanel(String path) {
        for (int i = 0; i < getTabCount(); i++) {
            Component myComp = getComponentAt(i);
            if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFilePath().equals(path)) {
                return (EditorPanel) myComp;
            }
        }
        return null;
    }

    public File getShowDir() {
        return showDir;
    }
}
