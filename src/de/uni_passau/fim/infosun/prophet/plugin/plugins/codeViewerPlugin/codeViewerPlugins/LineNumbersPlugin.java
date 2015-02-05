package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsCheckBox;

public class LineNumbersPlugin implements Plugin {

    public static final String KEY = "linenumbers";
    private Attribute selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new SettingsCheckBox(attribute, getClass().getSimpleName());
        setting.setCaption(UIElementNames.getLocalized("LINE_NUMBER_SHOW_LINE_NUMBERS"));

        return setting;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        this.selected = viewer.getAttribute();        
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
