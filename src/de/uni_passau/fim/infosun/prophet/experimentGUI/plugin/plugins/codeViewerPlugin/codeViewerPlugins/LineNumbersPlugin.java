package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;

public class LineNumbersPlugin implements CodeViewerPlugin {

    public static final String KEY = "linenumbers";
    private QuestionTreeNode selected;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        Setting setting = new SettingsCheckBox(attribute, getClass().getSimpleName());
        setting.setCaption(UIElementNames.LINE_NUMBER_SHOW_LINE_NUMBERS);

        return setting;
    }

    @Override
    public void init(QuestionTreeNode selected) {
        this.selected = selected;
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        boolean lineNumbers = Boolean.parseBoolean(selected.getAttributeValue("linenumbers_default"));
        editorPanel.getScrollPane().setLineNumbersEnabled(lineNumbers);
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        // TODO Auto-generated method stub

    }
}
