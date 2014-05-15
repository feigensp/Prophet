package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.*;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * A <code>Plugin</code> repository. Allows adding and removing <code>Plugin</code>s as well as calling the methods
 * of the <code>Plugin</code> interface on all currently active <code>Plugin</code>s.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class PluginList {

    private static List<Plugin> plugins = new LinkedList<>();

    static {
        add(new InactivityPlugin());
        add(new ValidSubjectCodePlugin());
        add(new CodeViewerPlugin());
        add(new QuestionListPlugin());
        add(new MailPlugin());
        add(new MaxTimePlugin());
        add(new ExternalProgramsPlugin());
        add(new AnswerRequiredPlugin());
        add(new PHPExportPlugin());
    }

    /**
     * Returns all currently active <code>Plugin</code>s.
     *
     * @return the list of plugins
     */
    public static List<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Adds a <code>Plugin</code> to the currently active plugins.
     *
     * @param plugin
     *         the plugin to be added
     */
    public static void add(Plugin plugin) {
        plugins.add(plugin);
    }

    /**
     * Removes a <code>Plugin</code> from the currently active plugins.
     *
     * @param plugin
     *         the plugin to be removed
     *@return true iff the <code>Plugin</code> was active and has been removed
     */
    public static boolean remove(Plugin plugin) {
        return plugins.remove(plugin);
    }

    /**
     * Returns the settings for all plugins.
     *
     * @param node the <code>QTreeNode</code> to get the <code>Setting</code> objects for
     * @return the <code>Setting</code> objects for all plugins
     */
    public static List<Setting> getAllSettings(QTreeNode node) {
        return plugins.stream().map(p -> p.getSetting(node)).filter(s -> s != null).collect(Collectors.toList());
    }

    /**
     * Calls {@link Plugin#experimentViewerRun(ExperimentViewer)} of all currently active <code>Plugin</code>s with
     * the given <code>ExperimentViewer</code>.
     *
     * @param experimentViewer
     *         the <code>ExperimentViewer</code> that has been started
     */
    public static void experimentViewerRun(ExperimentViewer experimentViewer) {
        for (Plugin plugin : plugins) {

            try {
                plugin.experimentViewerRun(experimentViewer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls {@link Plugin#experimentViewerRun(ExperimentViewer)} of all currently active <code>Plugin</code>s with
     * the given <code>QTreeNode</code>.
     *
     * @param node
     *         the node to be entered
     *
     * @return <b>true</b> if the node may be entered<br/> //TODO suspect its the other way around.. check this
     * <b>false</b> if any plugin denies the entrance
     */
    public static boolean denyEnterNode(QTreeNode node) {
        for (Plugin plugin : plugins) {
            try {
                if (plugin.denyEnterNode(node)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Calls {@link Plugin#enterNode(QTreeNode)} of all currently active <code>Plugin</code>s with the given
     * <code>QTreeNode</code>.
     *
     * @param node
     *         the node to be entered
     */
    public static void enterNode(QTreeNode node) {
        for (Plugin plugin : plugins) {
            try {
                plugin.enterNode(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls {@link Plugin#denyNextNode(QTreeNode)} of all currently active <code>Plugin</code>s with the given
     * <code>QTreeNode</code>.
     *
     * @param currentNode
     *         the node to be exited
     *
     * @return the resulting <code>String</code> from the first plugin denying exiting the node,
     * an empty String if a plugin returned an empty String and no real messages were returned,
     * null if all plugins allow exiting the node
     */
    public static String denyNextNode(QTreeNode currentNode) {
        String result = null;

        for (Plugin plugin : plugins) {
            try {
                String ret = plugin.denyNextNode(currentNode);
                if (ret != null) {
                    result = "";
                    if (ret.length() > 0) {
                        return ret;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Calls {@link Plugin#exitNode(QTreeNode)} of all currently active <code>Plugin</code>s with the given
     * <code>QTreeNode</code>.
     *
     * @param node
     *         the node to be exited
     */
    public static void exitNode(QTreeNode node) {
        for (Plugin plugin : plugins) {
            try {
                plugin.exitNode(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls {@link Plugin#exitNode(QTreeNode)} of all currently active <code>Plugin</code>s.
     *
     * @return The finishing messages of all plugins in HTML coding, with an own p-tag for every plugin.
     */
    public static String finishExperiment() {
        String result = "";
        for (Plugin plugin : plugins) {
            try {
                String ret = plugin.finishExperiment();
                if (ret != null && ret.length() > 0) {
                    result += "<p>" + ret + "</p>";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}