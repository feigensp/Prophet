package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin;

import java.util.Vector;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins.*;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public class CodeViewerPluginList {

    private static Vector<CodeViewerPlugin> plugins = new Vector<CodeViewerPlugin>() {

        private static final long serialVersionUID = 1L;

        {
            add(new EditAndSavePlugin());
            add(new LineNumbersPlugin());
            add(new SearchBarPlugin());
            add(new SyntaxHighlightingPlugin());
//			add(new ShowCIDECodePlugin());
            add(new OpenedFromStartPlugin());
        }
    };

    public static void add(CodeViewerPlugin plugin) {
        plugins.add(plugin);
    }

    public static boolean remove(CodeViewerPlugin plugin) {
        return plugins.remove(plugin);
    }

    public static SettingsComponentDescription getSettingsComponentDescription() {
        SettingsComponentDescription result = null;
        for (CodeViewerPlugin plugin : plugins) {
            try {
                SettingsComponentDescription desc = plugin.getSettingsComponentDescription();
                if (desc != null) {
                    if (result == null) {
                        result = desc;
                    } else {
                        result.addNextComponent(desc);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void init(QuestionTreeNode selected) {
        for (CodeViewerPlugin plugin : plugins) {
            try {
                plugin.init(selected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onFrameCreate(CodeViewer viewer) {
        for (CodeViewerPlugin plugin : plugins) {
            try {
                plugin.onFrameCreate(viewer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onEditorPanelCreate(EditorPanel editorPanel) {
        for (CodeViewerPlugin plugin : plugins) {
            try {
                plugin.onEditorPanelCreate(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onEditorPanelClose(EditorPanel editorPanel) {
        for (CodeViewerPlugin plugin : plugins) {
            try {
                plugin.onEditorPanelClose(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onClose() {
        for (CodeViewerPlugin plugin : plugins) {
            try {
                plugin.onClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
