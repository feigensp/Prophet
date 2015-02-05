package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import java.util.ArrayList;
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

    private static List<Plugin> plugins = new ArrayList<>();

    static {
        add(new EditAndSavePlugin());
        add(new LineNumbersPlugin());
        add(new SearchBarPlugin());
        add(new SyntaxHighlightingPlugin());
//		add(new ShowCIDECodePlugin());
        add(new OpenedFromStartPlugin());
    }

    public static void add(Plugin plugin) {
        plugins.add(plugin);
    }

    public static boolean remove(Plugin plugin) {
        return plugins.remove(plugin);
    }

    public static List<Setting> getAllSettings(Attribute attribute) {
        return plugins.stream().map(p -> p.getSetting(attribute)).filter(s -> s != null).collect(Collectors.toList());
    }

    public static void onCreate(CodeViewer viewer) {
        for (Plugin plugin : plugins) {
            try {
                plugin.onCreate(viewer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onEditorPanelCreate(EditorPanel editorPanel) {
        for (Plugin plugin : plugins) {
            try {
                plugin.onEditorPanelCreate(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onEditorPanelClose(EditorPanel editorPanel) {
        for (Plugin plugin : plugins) {
            try {
                plugin.onEditorPanelClose(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void onClose() {
        for (Plugin plugin : plugins) {
            try {
                plugin.onClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
