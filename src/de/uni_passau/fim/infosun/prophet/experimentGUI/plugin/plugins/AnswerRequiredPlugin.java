package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextArea;

/**
 * Plugin that enables the experiment creator to specify the names of answers that are required.
 */
public class AnswerRequiredPlugin implements Plugin {

    private static final String KEY = "answers_required";
    private static final String KEY_NAMES = "names";

    @Override
    public Setting getSetting(QTreeNode node) {

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_REQUIRED_ANSWERS"));

        Attribute subAttribute = mainAttribute.getSubAttribute(KEY_NAMES);
        Setting subSetting = new SettingsTextArea(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS") + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        boolean enabled = Boolean.parseBoolean(currentNode.getAttribute(KEY).getValue());

        if (enabled) {
            String requiredAnswers = currentNode.getAttribute(KEY).getSubAttribute(KEY_NAMES).getValue();
            Map<String, String[]> answers = currentNode.getAnswers();
            Scanner sc = new Scanner(requiredAnswers);

            while (sc.hasNext()) {
                String requiredAnswerKey = sc.next();
                boolean missing = !answers.containsKey(requiredAnswerKey);
                boolean empty = missing || Arrays.stream(answers.get(requiredAnswerKey)).allMatch(String::isEmpty);

                if (missing || empty) {
                    return UIElementNames.getLocalized("MENU_TAB_SETTINGS_MESSAGE_FILL_ALL_FIELDS");
                }
            }
        }

        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
