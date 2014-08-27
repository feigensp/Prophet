package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsFilePathChooser;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextArea;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;

/**
 * Plugin that is used to check the subject code the user input against a list of valid codes.
 * Will only allow advancing past the first node of the experiment of the code matches one of the valid codes.
 */
public class ValidSubjectCodePlugin implements Plugin {

    private static final String KEY = "valid_code";
    private static final String KEY_CODES = "codes";
    private static final String KEY_PATH = "path";
    private static final String KEY_IGNORE_CASE = "ignorecase";

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("SUBJECT_CODE_CHECK_SUBJECT_CODE"));

        SettingsTextArea settingsTextArea = new SettingsTextArea(mainAttribute.getSubAttribute(KEY_CODES), null);
        settingsTextArea.setCaption(UIElementNames.getLocalized("SUBJECT_CODE_VALID_CODES"));
        pluginSettings.addSetting(settingsTextArea);

        SettingsFilePathChooser filePathChooser = new SettingsFilePathChooser(mainAttribute.getSubAttribute(KEY_PATH), null);
        filePathChooser.setCaption(UIElementNames.getLocalized("SUBJECT_CODE_CODE_FILE"));
        pluginSettings.addSetting(filePathChooser);

        SettingsCheckBox settingsCheckBox = new SettingsCheckBox(mainAttribute.getSubAttribute(KEY_IGNORE_CASE), null);
        settingsCheckBox.setCaption(UIElementNames.getLocalized("SUBJECT_CODE_IGNORE_CASE"));
        pluginSettings.addSetting(settingsCheckBox);

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
        Attribute mainAttribute = currentNode.getAttribute(KEY);

        if (currentNode.getType() != EXPERIMENT || !Boolean.parseBoolean(mainAttribute.getValue())) {
            return null;
        }

        String[] answers = currentNode.getAnswers(Constants.KEY_SUBJECT);

        if (answers == null || answers.length < 1) {
            return UIElementNames.getLocalized("SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND");
        }

        String subjectCode = answers[0];
        boolean ignoreCase = Boolean.parseBoolean(mainAttribute.getSubAttribute(KEY_IGNORE_CASE).getValue());

        String codes = mainAttribute.getSubAttribute(KEY_CODES).getValue();
        if (codes != null && !codes.isEmpty()) {
            if (contains(subjectCode, new Scanner(codes), ignoreCase)) {
                return null;
            }
        }

        String path = mainAttribute.getSubAttribute(KEY_PATH).getValue();
        if (path != null && !path.isEmpty()) {
            try (FileReader file = new FileReader(path)) {
                if (contains(subjectCode, new Scanner(file), ignoreCase)) {
                    return null;
                }
            } catch (FileNotFoundException e) {
                return UIElementNames.getLocalized("SUBJECT_CODE_MESSAGE_FILE_NOT_FOUND");
            } catch (IOException e) {
                // may occur when trying to close the FileReader
            }
        }

        return UIElementNames.getLocalized("SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND");
    }

    /**
     * Checks whether any <code>String</code> returned by the given <code>Scanner</code> matches the given
     * <code>subjectCode</code>.
     *
     * @param subjectCode the subject code to search for
     * @param sc the <code>Scanner</code> to search through
     * @param ignoreCase whether to ignore case considerations
     * @return true iff the <code>Scanner</code> returned the <code>subjectCode</code>
     */
    private boolean contains(String subjectCode, Scanner sc, boolean ignoreCase) {

        while (sc.hasNext()) {
            String line = sc.next();
            if (ignoreCase) {
                if (line.equalsIgnoreCase(subjectCode)) return true;
            } else {
                if (line.equals(subjectCode)) return true;
            }
        }

        return false;
    }

    @Override
    public void exitNode(QTreeNode node) {

    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
