package de.uni_passau.fim.infosun.prophet.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.AnswerRequiredPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.ExperimentExportPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.ExternalProgramsPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.InactivityPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.MaxTimePlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.ValidSubjectCodePlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.mailPlugin.MailPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.phpExportPlugin.PHPExportPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.questionListPlugin.QuestionListPlugin;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * The top level <code>Plugin</code> repository. Allows adding and removing <code>Plugin</code>s as well as calling the
 * methods of the <code>Plugin</code> interface on all currently active <code>Plugin</code>s.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class PluginList {

    private static List<Plugin> plugins = new ArrayList<>();

    static {
        add(new ExperimentExportPlugin());
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
     * Calls {@link Plugin#experimentViewerRun(EViewer)} of all currently active <code>Plugin</code>s with
     * the given <code>ExperimentViewer</code>.
     *
     * @param experimentViewer
     *         the <code>ExperimentViewer</code> that has been started
     */
    public static void experimentViewerRun(EViewer experimentViewer) {

        for (Plugin plugin : plugins) {
            try {
                plugin.experimentViewerRun(experimentViewer);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls {@link Plugin#denyEnterNode(QTreeNode)} of all currently active <code>Plugin</code>s with
     * the given <code>QTreeNode</code>.
     *
     * @param node
     *         the node to be entered
     *
     * @return <code>true</code> iff any <code>Plugin</code> denies entrance to the given node
     */
    public static boolean denyEnterNode(QTreeNode node) {

        for (Plugin plugin : plugins) {
            try {
                if (plugin.denyEnterNode(node)) {
                    return true;
                }
            } catch (RuntimeException e) {
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
            } catch (RuntimeException e) {
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
     * @return the resulting <code>String</code> from the first plugin denying exiting the node or <code>null</code> if
     * no plugin returned a non empty message
     */
    public static String denyNextNode(QTreeNode currentNode) {

        for (Plugin plugin : plugins) {
            try {
                String message = plugin.denyNextNode(currentNode);
                if (message != null && !message.trim().isEmpty()) {
                    return message;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return null;
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
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls {@link Plugin#exitNode(QTreeNode)} of all currently active <code>Plugin</code>s.
     *
     * @return an HTML string comprised of 'p' tags containing the messages returned by the <code>Plugin</code>s
     */
    public static String finishExperiment() {
        Element html = new Element(Tag.valueOf("html"), "");

        for (Plugin plugin : plugins) {
            try {
                String pluginMessage = plugin.finishExperiment();

                if (pluginMessage != null && !pluginMessage.isEmpty()) {
                    html.appendElement("p").text(pluginMessage);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return html.html();
    }
}
