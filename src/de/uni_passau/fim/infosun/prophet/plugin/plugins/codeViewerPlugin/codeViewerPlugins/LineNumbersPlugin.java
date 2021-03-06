package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.CheckBoxSetting;

/**
 * A <code>Plugin</code> that enables/disables displaying line numbers in the <code>EditorPanel</code>s of the
 * <code>CodeViewer</code>.
 */
public class LineNumbersPlugin implements Plugin {

    public static final String KEY = "linenumbers";

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new CheckBoxSetting(attribute, getClass().getSimpleName());
        setting.setCaption(UIElementNames.getLocalized("LINE_NUMBER_SHOW_LINE_NUMBERS"));

        return setting;
    }

    @Override
    public void onCreate(CodeViewer viewer) {

    }

    @Override
    public void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel) {
        Attribute attr = codeViewer.getAttribute();
        boolean enabled = attr.containsSubAttribute(KEY) && Boolean.parseBoolean(attr.getSubAttribute(KEY).getValue());

        editorPanel.getScrollPane().setLineNumbersEnabled(enabled);
    }

    @Override
    public void onClose(CodeViewer codeViewer) {

    }

    @Override
    public void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel) {

    }
}
