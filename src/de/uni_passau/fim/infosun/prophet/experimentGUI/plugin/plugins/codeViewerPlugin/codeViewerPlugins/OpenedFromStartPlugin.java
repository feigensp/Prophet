package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

public class OpenedFromStartPlugin implements CodeViewerPlugin {

    public static final String KEY = "openedByStart";
    public static final String KEY_PATH = "startPath";
    private Attribute selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(attribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.get("OPENED_FROM_START_OPEN_FILE_ON_START"));

        Attribute subAttribute = attribute.getSubAttribute(KEY_PATH);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.get("OPENED_FROM_START_FILE_TO_OPEN") + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void init(Attribute selected) {
        this.selected = selected;
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
        if (Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue())) {
            Attribute attributes = selected.getSubAttribute(KEY);
            String path =
                    attributes.getSubAttribute(KEY_PATH).getValue().replace('/',
                            System.getProperty("file.separator").charAt(0));
            viewer.getTabbedPane().openFile(path);
            viewer.getFileTree().selectFile(path);
        }
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
    }

    @Override
    public void onClose() {
    }
}
