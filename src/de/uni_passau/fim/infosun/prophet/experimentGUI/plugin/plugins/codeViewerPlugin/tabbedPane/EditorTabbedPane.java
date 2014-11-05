package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.Recorder;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

public class EditorTabbedPane extends JTabbedPane {

    private Recorder recorder;
    private Map<File, EditorPanel> panels;

    public EditorTabbedPane(Recorder recorder) {
        super(JTabbedPane.TOP);

        this.recorder = recorder;
        this.panels = new HashMap<>();

        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public void openFile(File file) {
        EditorPanel panel;

        if (file.exists()) {
            panel = panels.get(file);

            if (panel == null) {
                panel = new EditorPanel(file);

                recorder.onEditorPanelCreate(panel);
                CodeViewerPluginList.onEditorPanelCreate(panel);

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

    public void closeFile(File file) {
        closeEditorPanel(panels.get(file));
    }

    public void closeEditorPanel(EditorPanel panel) {
        boolean panelFound;

        if (panel != null) {
            synchronized (getTreeLock()) {
                panelFound = Arrays.asList(getComponents()).contains(panel);
            }

            if (panelFound) {
                CodeViewerPluginList.onEditorPanelClose(panel);
                recorder.onEditorPanelClose(panel);

                panels.remove(panel.getFile());
                remove(panel);
            }
        }
    }
}
