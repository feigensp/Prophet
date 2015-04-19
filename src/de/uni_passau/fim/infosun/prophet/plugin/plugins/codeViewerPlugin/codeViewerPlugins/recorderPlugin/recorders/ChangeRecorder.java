package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Record;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.ChangeEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextFieldSetting;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.HashMap;
import java.util.Map;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

public class ChangeRecorder extends Recorder {

    private static final String KEY = "change";
    private static final String KEY_JOIN = "join";
    private static final String KEY_JOIN_TIME = "jointime";

    private boolean enabled;
    private boolean join;
    private long joinTime;

    private long lastJoin;
    private ChangeEntry lastEntry;

    private Map<EditorPanel, ChangeListener> listeners;

    private class ChangeListener implements DocumentListener {

        private EditorPanel panel;

        public ChangeListener(EditorPanel panel) {
            this.panel = panel;
        }

        private boolean shouldJoin(ChangeEntry.Type eventType, DocumentEvent event) {

            if (!join) {
                return false;
            }

            if (lastEntry == null) {
                return false;
            }

            if (lastEntry.getType() != eventType) {
                return false;
            }

            if (eventType == ChangeEntry.Type.INSERT) {

                if ((lastEntry.getOffset() + lastEntry.getLength()) != event.getOffset()) {
                    return false;
                }
            } else if (eventType == ChangeEntry.Type.REMOVE) {
                int lastOffset = lastEntry.getOffset();
                int offset = event.getOffset();
                int length = event.getLength();

                boolean wasBackspace = offset + length == lastOffset;
                boolean wasDelete = lastOffset == offset;

                if (!(wasBackspace || wasDelete)) {
                    return false;
                }
            }

            return (System.currentTimeMillis() - ((lastJoin == 0) ? lastEntry.getTimestamp() : lastJoin)) < joinTime;
        }

        private String getContent(DocumentEvent event) {
            String content;

            try {
                content = event.getDocument().getText(event.getOffset(), event.getLength());
            } catch (BadLocationException e) {
                System.err.println("Could not get the text content of an update.");
                System.err.println(e.getMessage());
                return "error";
            }

            return content;
        }

        @Override
        public void insertUpdate(DocumentEvent event) {
            String content = getContent(event);

            if (shouldJoin(ChangeEntry.Type.INSERT, event)) {
                lastEntry.appendContent(content);
                lastJoin = System.currentTimeMillis();
            } else {
                lastJoin = 0;
                lastEntry = new ChangeEntry(panel, ChangeEntry.Type.INSERT);
                lastEntry.setContent(content);
                lastEntry.setOffset(event.getOffset());
                lastEntry.setLength(event.getLength());
                record.add(lastEntry);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent event) {

            if (shouldJoin(ChangeEntry.Type.REMOVE, event)) {
                lastEntry.setOffset(event.getOffset());
                lastEntry.setLength(lastEntry.getLength() + event.getLength());
            } else {
                lastJoin = 0;
                lastEntry = new ChangeEntry(panel, ChangeEntry.Type.REMOVE);
                lastEntry.setOffset(event.getOffset());
                lastEntry.setLength(event.getLength());
                record.add(lastEntry);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    };

    public ChangeRecorder(Record record, CodeViewer viewer, Attribute recorderPluginAttr) {
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

        if (enabled) {
            ChangeListener listener = new ChangeListener(editorPanel);

            listeners.put(editorPanel, listener);
            editorPanel.getTextArea().getDocument().addDocumentListener(listener);
        }
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {

        if (enabled) {
            editorPanel.getTextArea().getDocument().removeDocumentListener(listeners.remove(editorPanel));
        }
    }

    @Override
    public void onClose() {
        
    }
}
