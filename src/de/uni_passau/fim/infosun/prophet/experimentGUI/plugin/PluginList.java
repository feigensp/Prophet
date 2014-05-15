package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.*;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;

/**
 * Class to store active plugins, allows calls to all plugins within one line of code.
 *
 * @author Andreas Hasselberg
 * @author Markus K�ppen
 */
public class PluginList {

    static {
        add(new InactivityPlugin());
        add(new ValidSubjectCodePlugin());
        add(new CodeViewerPlugin());
        add(new QuestionListPlugin());
        add(new MailPlugin());
        add(new MaxTimePlugin());
        add(new ExternalProgramsPlugin());
        add(new AnswerRequiredPlugin());
//		add(new KeyPressedPlugin());
        add(new PHPExportPlugin());
    }

    private static List<Plugin> plugins = new LinkedList<>();

    /**
     * @return Vector describing all active plugins
     */
    public static List<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Adds and activates a plugin
     *
     * @param plugin
     *         Plugin to be added and activated
     */
    public static void add(Plugin plugin) {
        plugins.add(plugin);
    }

    /**
     * Removes and deactives a plugin
     *
     * @param plugin
     *         Plugin to be deactivated
     *
     * @return <b>true</b> if a plugin is removed<br>
     * <b>false</b> if it wasn't active
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
     * Calls the experimentViewerRun() function of all plugins at once
     *
     * @param experimentViewer
     *         ExperimentViewer that has been started
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
     * Calls the denyEnterNode() function for all plugins at once
     *
     * @param node
     *         The node to be entered
     *
     * @return <b>true</b> if the node may be entered<br/>
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
     * Calls the enterNode() function for all plugins at once
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
     * Calls the denyExitNode() for all plugins at once.
     *
     * @param currentNode
     *         the node to be exited
     *
     * @return The resulting String from the first plugin denying exiting the node,
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
     * Calls the exitNode() function for all plugins at once
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
     * Calls the finishExperiment() function for all plugins at once
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
