package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsFilePathChooser;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextArea;

public class ValidSubjectCodePlugin implements Plugin {

    private static final String KEY = "valid_code";
    private static final String KEY_CODES = "codes";
    private static final String KEY_PATH = "path";
    private static final String KEY_IGNORE_CASE = "ignorecase";

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != QTreeNode.Type.EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.SUBJECT_CODE_CHECK_SUBJECT_CODE);

        SettingsTextArea settingsTextArea = new SettingsTextArea(mainAttribute.getSubAttribute(KEY_CODES), null);
        settingsTextArea.setCaption(UIElementNames.SUBJECT_CODE_VALID_CODES);
        pluginSettings.addSetting(settingsTextArea);

        SettingsFilePathChooser filePathChooser = new SettingsFilePathChooser(mainAttribute.getSubAttribute(KEY_PATH), null);
        filePathChooser.setCaption(UIElementNames.SUBJECT_CODE_CODE_FILE);
        pluginSettings.addSetting(filePathChooser);

        SettingsCheckBox settingsCheckBox = new SettingsCheckBox(mainAttribute.getSubAttribute(KEY_IGNORE_CASE), null);
        settingsCheckBox.setCaption(UIElementNames.SUBJECT_CODE_IGNORE_CASE);
        pluginSettings.addSetting(settingsCheckBox);

        return pluginSettings;
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
        if (!currentNode.isExperiment()) {
            return null;
        } else if (Boolean.parseBoolean(currentNode.getAttributeValue(KEY))) {
            String subjectCode = currentNode.getAnswer(Constants.KEY_SUBJECT);
            boolean ignoreCase = Boolean.parseBoolean(currentNode.getAttribute(KEY).getAttributeValue(KEY_IGNORE_CASE));
            if (ignoreCase) {
                subjectCode = subjectCode.toLowerCase();
            }

            String codes = currentNode.getAttribute(KEY).getAttributeValue(KEY_CODES);
            if (codes != null && !codes.equals("")) {
                Scanner sc = new Scanner(codes);
                while (sc.hasNext()) {
                    String line = sc.next();
                    if (ignoreCase) {
                        line = line.toLowerCase();
                    }
                    if (subjectCode.equals(line)) {
                        return null;
                    }
                }
            }

            String path = currentNode.getAttribute(KEY).getAttributeValue(KEY_PATH);
            if (path != null && !path.equals("")) {
                try {
                    Scanner sc = new Scanner(new FileReader(path));
                    while (sc.hasNext()) {
                        String line = sc.next();
                        if (ignoreCase) {
                            line = line.toLowerCase();
                        }
                        if (subjectCode.equals(line)) {
                            return null;
                        }
                    }
                } catch (FileNotFoundException e) {
                    return UIElementNames.SUBJECT_CODE_MESSAGE_FILE_NOT_FOUND;
                }
            }
            return UIElementNames.SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND;
        } else {
            return null;
        }
    }

    @Override
    public void exitNode(QuestionTreeNode node) {
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
