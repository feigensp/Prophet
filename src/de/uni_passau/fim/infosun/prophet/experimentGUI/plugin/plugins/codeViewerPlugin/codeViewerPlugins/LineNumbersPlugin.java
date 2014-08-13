package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

public class LineNumbersPlugin implements CodeViewerPlugin {

    public static final String KEY = "linenumbers";
    private Attribute selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new SettingsCheckBox(attribute, getClass().getSimpleName());
        setting.setCaption(UIElementNames.get("LINE_NUMBER_SHOW_LINE_NUMBERS"));

        return setting;
    }

    @Override
    public void init(Attribute selected) {
        this.selected = selected;
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        boolean lineNumbers = Boolean.parseBoolean(selected.getSubAttribute("linenumbers_default").getValue());
        editorPanel.getScrollPane().setLineNumbersEnabled(lineNumbers);
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
    }
}
