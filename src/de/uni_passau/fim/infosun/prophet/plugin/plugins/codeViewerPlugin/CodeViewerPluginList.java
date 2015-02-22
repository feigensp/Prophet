package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import java.util.ArrayList;
import java.util.Collections;
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

/**
 * The <code>Plugin</code> repository of the <code>CodeViewer</code>. Allows adding and removing <code>Plugin</code>s as
 * well as calling the methods of the <code>Plugin</code> interface on all currently active <code>Plugin</code>s.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
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
	 * 		the <code>Attribute</code> to obtain sub-attributes from
	 *
	 * @return the <code>Setting</code> objects for all plugins
	 */
    public static List<Setting> getAllSettings(Attribute attribute) {
        return plugins.stream().map(p -> p.getSetting(attribute)).filter(s -> s != null).collect(Collectors.toList());
    }

	/**
	 * Calls the {@link Plugin#onCreate(CodeViewer)} method of all currently active plugins with the given
	 * <code>CodeViewer</code>.
	 *
	 * @param viewer the <code>CodeViewer</code> that was created
	 */
    public static void onCreate(CodeViewer viewer) {

		for (Plugin plugin : plugins) {
            try {
                plugin.onCreate(viewer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * Calls the {@link Plugin#onEditorPanelCreate(EditorPanel)} method of all currently active plugins with the given
	 * <code>EditorPanel</code>.
	 *
	 * @param editorPanel
	 * 		the created <code>EditorPanel</code>
	 */
    public static void onEditorPanelCreate(EditorPanel editorPanel) {

		for (Plugin plugin : plugins) {
            try {
                plugin.onEditorPanelCreate(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * Calls the {@link Plugin#onEditorPanelClose(EditorPanel)} method of all currently active plugins with the given
	 * <code>EditorPanel</code>.
	 *
	 * @param editorPanel
	 * 		the closed <code>EditorPanel</code>
	 */
    public static void onEditorPanelClose(EditorPanel editorPanel) {

		for (Plugin plugin : plugins) {
            try {
                plugin.onEditorPanelClose(editorPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * Calls the {@link Plugin#onClose()} method of all currently active plugins.
	 */
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
