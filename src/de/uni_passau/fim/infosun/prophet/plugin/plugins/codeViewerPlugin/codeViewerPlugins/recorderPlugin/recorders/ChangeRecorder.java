package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextFieldSetting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

public class ChangeRecorder extends Recorder {

    private static final String KEY = "change";
    private static final String KEY_JOIN = "join";
    private static final String KEY_JOIN_TIME = "jointime";

    private boolean enabled;
    private boolean join;
    private long joinTime;

    public ChangeRecorder(RecorderPlugin recorder, CodeViewer viewer) {
        super(recorder, viewer);

        Attribute enabledAttr = recorder.getAttribute().getSubAttribute(KEY);
        Attribute joinAttr = enabledAttr.getSubAttribute(KEY_JOIN);
        Attribute joinTimeAttr = joinAttr.getSubAttribute(KEY_JOIN_TIME);

        enabled = Boolean.parseBoolean(enabledAttr.getValue());

        if (enabled) {
            join = Boolean.parseBoolean(joinAttr.getValue());

            if (join) {
                joinTime = Long.parseLong(joinTimeAttr.getValue());
            }
        }
    }

    /**
     * Returns the <code>Setting</code> of this <code>Recorder</code>. <code>Attribute</code>s to store settings in may
     * be obtained from the given <code>mainAttribute</code>. <code>mainAttribute</code> will be an
     * <code>Attribute</code> returned by the {@link RecorderPlugin#getAttribute()} method. <code>Recorder</code>
     * instances can retrieve the values for their settings from the <code>RecorderPlugin</code> instance given
     * in the constructor.
     *
     * @param mainAttribute
     *         the <code>Attribute</code> to obtain sub-attributes from
     *
     * @return a <code>Setting</code> object representing the settings of the recorder or <code>null</code> if there are
     *         none
     */
    public static Setting getSetting(Attribute mainAttribute) {
        Attribute rDescAttribute = mainAttribute.getSubAttribute(KEY);
        SettingsList resultDesc = new SettingsList(rDescAttribute, ChangeRecorder.class.getSimpleName(), true);
        resultDesc.setCaption(getLocalized("RECORDER_CHANGE_SOURCE_CODE_EDITS"));

        Attribute joinDescAttribute = rDescAttribute.getSubAttribute(KEY_JOIN);
        SettingsList joinDesc = new SettingsList(joinDescAttribute, null, true);
        joinDesc.setCaption(getLocalized("RECORDER_CHANGE_SUMMARIZE_CHANGES"));

        Attribute joinTimeDescAttribute = joinDescAttribute.getSubAttribute(KEY_JOIN_TIME);
        Setting joinTimeDesc = new TextFieldSetting(joinTimeDescAttribute, null);
        joinTimeDesc.setCaption(getLocalized("RECORDER_TIME_INTERVAL_FOR_SUMMARY"));

        joinDesc.addSetting(joinTimeDesc);
        resultDesc.addSetting(joinDesc);

        return resultDesc;
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
