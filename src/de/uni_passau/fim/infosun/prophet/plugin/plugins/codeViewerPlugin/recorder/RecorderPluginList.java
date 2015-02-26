package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins.ChangePlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins.ScrollingPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

/**
 * The <code>Plugin</code> repository of the <code>Recorder</code>. Allows adding and removing <code>Plugin</code>s as
 * well as calling the methods of the <code>Plugin</code> interface on all currently active <code>Plugin</code>s.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class RecorderPluginList {

    private static List<Plugin> plugins = new ArrayList<>();

    static {
        add(new ChangePlugin());
        add(new ScrollingPlugin());
    }

	/**
	 * Returns an unmodifiable list view of all currently active <code>Plugin</code>s.
	 *
	 * @return the list of plugins
	 */
	public static List<Plugin> getPlugins() {
		return Collections.unmodifiableList(plugins);
	}

	/**
	 * Adds a <code>Plugin</code> to the currently active plugins.
	 *
	 * @param plugin
	 * 		the plugin to be added
	 */
	public static void add(Plugin plugin) {
		plugins.add(plugin);
	}

	/**
	 * Removes a <code>Plugin</code> from the currently active plugins.
	 *
	 * @param plugin
	 * 		the plugin to be removed
	 *
	 * @return true iff the <code>Plugin</code> was active and has been removed
	 */
	public static boolean remove(Plugin plugin) {
		return plugins.remove(plugin);
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

	// TODO document the parameters below when they have been documented in the plugin interface

	/**
	 * Calls the {@link Plugin#onFrameCreate(Attribute, CodeViewer, LoggingTreeNode)} method of all currently active
	 * plugins.
	 *
	 * @param selected
	 * @param viewer
	 * @param currentNode
	 */
	public static void onFrameCreate(Attribute selected, CodeViewer viewer, LoggingTreeNode currentNode) {

		for (Plugin plugin : plugins) {
			try {
				plugin.onFrameCreate(selected, viewer, currentNode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calls the {@link Plugin#onNodeChange(LoggingTreeNode, EditorPanel)} method of all currently active plugins.
	 *
	 * @param newNode
	 * @param newTab
	 */
	public static void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab) {

		for (Plugin plugin : plugins) {
			try {
				plugin.onNodeChange(newNode, newTab);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
