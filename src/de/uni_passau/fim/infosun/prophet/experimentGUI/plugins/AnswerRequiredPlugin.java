package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import java.util.Scanner;
import java.util.TreeMap;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsTextArea;

public class AnswerRequiredPlugin implements Plugin {

    private static final String KEY = "answers_required";
    private static final String KEY_NAMES = "names";

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
        SettingsPluginComponentDescription result =
                new SettingsPluginComponentDescription(KEY, UIElementNames.MENU_TAB_SETTINGS_REQUIRED_ANSWERS, true);
        result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY_NAMES,
                UIElementNames.MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS + ":"));
        return result;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QuestionTreeNode node) {
    }

    @Override
    public String denyNextNode(QuestionTreeNode currentNode) {
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
    public void exitNode(QuestionTreeNode node) {
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
