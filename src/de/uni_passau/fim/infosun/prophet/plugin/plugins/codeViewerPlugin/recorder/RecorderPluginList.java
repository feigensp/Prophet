package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins.ChangePlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins.ScrollingPlugin;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class RecorderPluginList {

    private static List<RecorderPlugin> plugins = new LinkedList<>();

    static {
        add(new ChangePlugin());
        add(new ScrollingPlugin());
    }

    /**
     * Returns the settings for all plugins.
     *
     * @param attribute
     *          the <code>Attribute</code> under which the plugins should store their settings
     *
     * @return the <code>Setting</code> objects for all plugins
     */
    public static List<Setting> getAllSettings(Attribute attribute) {
        return plugins.stream().map(p -> p.getSetting(attribute)).filter(s -> s != null).collect(Collectors.toList());
    }

    public static List<RecorderPlugin> getPlugins() {
        return plugins;
    }

    public static void add(RecorderPlugin plugin) {
        plugins.add(plugin);
    }

    public static boolean remove(RecorderPlugin plugin) {
        return plugins.remove(plugin);
    }
}
