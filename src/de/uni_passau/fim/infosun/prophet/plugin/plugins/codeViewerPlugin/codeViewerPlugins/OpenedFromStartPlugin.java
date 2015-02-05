package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.io.File;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsTextField;

public class OpenedFromStartPlugin implements Plugin {

    public static final String KEY = "openedByStart";
    public static final String KEY_PATH = "startPath";
    private Attribute selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(attribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("OPENED_FROM_START_OPEN_FILE_ON_START"));

        Attribute subAttribute = attribute.getSubAttribute(KEY_PATH);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("OPENED_FROM_START_FILE_TO_OPEN") + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void init(Attribute selected) {
        this.selected = selected;
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {

        if (selected.containsSubAttribute(KEY) && Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue())) {
            Attribute attributes = selected.getSubAttribute(KEY);
            String path = attributes.getSubAttribute(KEY_PATH).getValue();
            File file = new File(viewer.getShowDir(), path);

            viewer.getTabbedPane().openFile(file);
            viewer.getFileTree().selectFile(file);
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
