package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextAreaSetting;

/**
 * <code>Plugin</code> that enables the experiment creator to specify the names of answers that are required.
 * Will deny exiting any node in which required answers are missing.
 */
public class AnswerRequiredPlugin implements Plugin {

    private static final String KEY = "answers_required";
    private static final String KEY_NAMES = "names";

    @Override
    public Setting getSetting(QTreeNode node) {

        Attribute mainAttribute = node.getAttribute(KEY);
        SettingsList settingsList = new SettingsList(mainAttribute, getClass().getSimpleName(), true);
        settingsList.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_REQUIRED_ANSWERS"));

        Attribute subAttribute = mainAttribute.getSubAttribute(KEY_NAMES);
        Setting subSetting = new TextAreaSetting(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS") + ":");
        settingsList.addSetting(subSetting);

        return settingsList;
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

            while (sc.hasNextLine()) {
                String requiredAnswerKey = sc.nextLine();
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
