package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.EditAndSavePlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.LineNumbersPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.OpenedFromStartPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.SearchBarPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.SyntaxHighlightingPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class CodeViewerPluginList {

    private static List<CodeViewerPlugin> plugins = new LinkedList<>();

    static {
        add(new EditAndSavePlugin());
        add(new LineNumbersPlugin());
        add(new SearchBarPlugin());
        add(new SyntaxHighlightingPlugin());
//		add(new ShowCIDECodePlugin());
        add(new OpenedFromStartPlugin());
    }

    public static void add(CodeViewerPlugin plugin) {
        plugins.add(plugin);
    }

    public static boolean remove(CodeViewerPlugin plugin) {
        return plugins.remove(plugin);
    }

    public static List<Setting> getAllSettings(Attribute attribute) {
        return plugins.stream().map(p -> p.getSetting(attribute)).filter(s -> s != null).collect(Collectors.toList());
    }

    public static void init(Attribute selected) {
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
