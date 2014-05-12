package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.util.Scanner;
import java.util.TreeMap;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextArea;

public class AnswerRequiredPlugin implements Plugin {

    private static final String KEY = "answers_required";
    private static final String KEY_NAMES = "names";

    @Override
    public Setting getSetting(QTreeNode node) {

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.MENU_TAB_SETTINGS_REQUIRED_ANSWERS);

        Attribute subAttribute = mainAttribute.getSubAttribute(KEY_NAMES);
        Setting subSetting = new SettingsTextArea(subAttribute, null);
        subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
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
        boolean enabled = Boolean.parseBoolean(currentNode.getAttributeValue(KEY));
        if (enabled) {
            String requiredAnswers = currentNode.getAttribute(KEY).getAttributeValue(KEY_NAMES);

            TreeMap<String, String> answers = currentNode.getAnswers();

            Scanner sc = new Scanner(requiredAnswers);
            while (sc.hasNext()) {
                String requiredAnswerKey = sc.next();
                if (!answers.containsKey(requiredAnswerKey) || answers.get(requiredAnswerKey).equals("")) {
                    return UIElementNames.MENU_TAB_SETTINGS_MESSAGE_FILL_ALL_FIELDS;
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
