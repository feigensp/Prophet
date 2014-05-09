package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;

public class OpenedFromStartPlugin implements CodeViewerPlugin {

    public static final String KEY = "openedByStart";
    public static final String KEY_PATH = "startPath";
    private QuestionTreeNode selected;

    @Override
    public SettingsComponentDescription getSettingsComponentDescription() {
        SettingsPluginComponentDescription result =
                new SettingsPluginComponentDescription(KEY, UIElementNames.OPENED_FROM_START_OPEN_FILE_ON_START, true);
        result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, KEY_PATH,
                UIElementNames.OPENED_FROM_START_FILE_TO_OPEN + ":"));
        return result;
    }

    @Override
    public void init(QuestionTreeNode selected) {
        this.selected = selected;
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
        if (Boolean.parseBoolean(selected.getAttributeValue(KEY))) {
            QuestionTreeNode attributes = selected.getAttribute(KEY);
            String path =
                    attributes.getAttributeValue(KEY_PATH).replace('/', System.getProperty("file.separator").charAt(0));
            viewer.getTabbedPane().openFile(path);
            viewer.getFileTree().selectFile(path);
        }
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClose() {
        // TODO Auto-generated method stub

    }
}
