package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

public class OpenedFromStartPlugin implements CodeViewerPlugin {

    public static final String KEY = "openedByStart";
    public static final String KEY_PATH = "startPath";
    private QuestionTreeNode selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(attribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.OPENED_FROM_START_OPEN_FILE_ON_START);

        Attribute subAttribute = attribute.getSubAttribute(KEY_PATH);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.OPENED_FROM_START_FILE_TO_OPEN + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
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
