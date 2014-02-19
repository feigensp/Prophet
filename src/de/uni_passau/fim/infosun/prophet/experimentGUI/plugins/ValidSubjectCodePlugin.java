package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsFilePathChooser;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsTextArea;

public class ValidSubjectCodePlugin implements Plugin {

    private static final String KEY = "valid_code";
    private static final String KEY_CODES = "codes";
    private static final String KEY_PATH = "path";
    private static final String KEY_IGNORE_CASE = "ignorecase";

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
        if (node.isExperiment()) {
            SettingsPluginComponentDescription result =
                    new SettingsPluginComponentDescription(KEY, UIElementNames.SUBJECT_CODE_CHECK_SUBJECT_CODE, true);
            result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY_CODES,
                    UIElementNames.SUBJECT_CODE_VALID_CODES));
            result.addSubComponent(new SettingsComponentDescription(SettingsFilePathChooser.class, KEY_PATH,
                    UIElementNames.SUBJECT_CODE_CODE_FILE));
            result.addSubComponent(new SettingsComponentDescription(SettingsCheckBox.class, KEY_IGNORE_CASE,
                    UIElementNames.SUBJECT_CODE_IGNORE_CASE));
            return result;
        }
        return null;
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
