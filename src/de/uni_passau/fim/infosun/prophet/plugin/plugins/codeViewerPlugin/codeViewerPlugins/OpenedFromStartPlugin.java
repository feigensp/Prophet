package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.io.File;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsTextField;

/**
 * A <code>Plugin</code> that enables having a file opened immediately after the a <code>CodeViewer</code> was
 * opened.
 */
public class OpenedFromStartPlugin implements Plugin {

    public static final String KEY = "openedByStart";
    public static final String KEY_PATH = "startPath";

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        SettingsList settingsList = new SettingsList(attribute, getClass().getSimpleName(), true);
        settingsList.setCaption(UIElementNames.getLocalized("OPENED_FROM_START_OPEN_FILE_ON_START"));

        Attribute subAttribute = attribute.getSubAttribute(KEY_PATH);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("OPENED_FROM_START_FILE_TO_OPEN") + ":");
        settingsList.addSetting(subSetting);

        return settingsList;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        Attribute attr = viewer.getAttribute();

        if (attr.containsSubAttribute(KEY) && Boolean.parseBoolean(attr.getSubAttribute(KEY).getValue())) {
            String path = attr.getSubAttribute(KEY).getSubAttribute(KEY_PATH).getValue();
            File file = new File(path);

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
