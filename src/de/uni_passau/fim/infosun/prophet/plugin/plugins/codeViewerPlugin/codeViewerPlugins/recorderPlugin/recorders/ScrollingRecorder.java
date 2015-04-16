package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Record;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.ScrollingEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextFieldSetting;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScrollingRecorder extends Recorder {

    private static final String KEY = "scrolling";
    private static final String KEY_JOIN = "join";
    private static final String KEY_JOIN_TIME = "jointime";

    private boolean enabled;
    private boolean join;
    private long joinTime;

    private long lastJoin;
    private ScrollingEntry lastEntry;

    private Map<EditorPanel, ScrollListener> listeners;

    private class ScrollListener implements ChangeListener {

        private EditorPanel panel;

        public ScrollListener(EditorPanel panel) {
            this.panel = panel;
        }

        private boolean shouldJoin() {

            if (!join) {
                return false;
            }

            if (lastEntry == null) {
                return false;
            }

            return (System.currentTimeMillis() - ((lastJoin == 0) ? lastEntry.getTimestamp() : lastJoin)) < joinTime;
        }

        @Override
        public void stateChanged(ChangeEvent event) {
            JViewport viewport = (JViewport) event.getSource();
            RSyntaxTextArea view = (RSyntaxTextArea) viewport.getView();

            Rectangle viewRect = viewport.getViewRect();
            Point topLeft = viewRect.getLocation();

            int line;

            try {
                line = view.getLineOfOffset(view.viewToModel(topLeft));
            } catch (BadLocationException e) {
                return;
            }

            if (lastEntry != null && line == lastEntry.getStartLine()) {
                return;
            }

            if (shouldJoin()) {
                lastEntry.setEndLine(line);
                lastJoin = System.currentTimeMillis();
            } else {
                lastJoin = 0;
                lastEntry = new ScrollingEntry(panel, line);
                record.add(lastEntry);
            }
        }
    };

    public ScrollingRecorder(Record record, CodeViewer viewer, Attribute recorderPluginAttr) {
        super(record, viewer);

        Attribute enabledAttr = recorderPluginAttr.getSubAttribute(KEY);
        Attribute joinAttr = enabledAttr.getSubAttribute(KEY_JOIN);
        Attribute joinTimeAttr = joinAttr.getSubAttribute(KEY_JOIN_TIME);

        enabled = Boolean.parseBoolean(enabledAttr.getValue());

        if (enabled) {
            join = Boolean.parseBoolean(joinAttr.getValue());

            if (join) {
                joinTime = Long.parseLong(joinTimeAttr.getValue());
            }
        }

        listeners = new HashMap<>();
    }

    /**
     * Returns the <code>Setting</code> of this <code>Recorder</code>. <code>Attribute</code>s to store settings in may
     * be obtained from the given <code>mainAttribute</code>.
     *
     * @param mainAttribute
     *         the <code>Attribute</code> to obtain sub-attributes from
     *
     * @return a <code>Setting</code> object representing the settings of the recorder or <code>null</code> if there are
     *         none
     */
    public static Setting getSetting(Attribute mainAttribute) {
        Attribute rDescAttribute = mainAttribute.getSubAttribute(KEY);
        SettingsList resultDesc = new SettingsList(rDescAttribute, ScrollingRecorder.class.getSimpleName(), true);
        resultDesc.setCaption(UIElementNames.getLocalized("RECORDER_SCROLL_SCROLLING_BEHAVIOR"));

        Attribute joinDescAttribute = rDescAttribute.getSubAttribute(KEY_JOIN);
        SettingsList joinDesc = new SettingsList(joinDescAttribute, null, true);
        joinDesc.setCaption(UIElementNames.getLocalized("RECORDER_SCROLL_SUMMARIZE_SCROLLING"));

        Attribute joinTimeDescAttribute = joinDescAttribute.getSubAttribute(KEY_JOIN_TIME);
        Setting joinTimeDesc = new TextFieldSetting(joinTimeDescAttribute, null);
        joinTimeDesc.setCaption(UIElementNames.getLocalized("RECORDER_TIME_INTERVAL_FOR_SUMMARY"));

        joinDesc.addSetting(joinTimeDesc);
        resultDesc.addSetting(joinDesc);

        return resultDesc;
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {

        if (enabled) {
            ScrollListener listener = new ScrollListener(editorPanel);

            editorPanel.getScrollPane().getViewport().addChangeListener(listener);
            listeners.put(editorPanel, listener);
        }
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {

        if (enabled) {
            editorPanel.getScrollPane().getViewport().removeChangeListener(listeners.get(editorPanel));
        }
    }

    @Override
    public void onClose() {

    }
}
