package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin
        .recordEntries.CVEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .ChangeRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .FileRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .ScrollingRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .TabSwitchRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;

public class RecorderPlugin implements Plugin {

    private static final String KEY = "recorder";
    private static final String RECORD_FILENAME = "record.xml";

    private Map<CodeViewer, Pair<Record, List<Recorder>>> recorders;

    public RecorderPlugin() {
        recorders = new HashMap<>();
    }

    private List<Recorder> getAllRecorders(CodeViewer viewer) {
        List<Recorder> allRecorders = new ArrayList<>(4);
        allRecorders.add(new ChangeRecorder(this, viewer));
        allRecorders.add(new FileRecorder(this, viewer));
        allRecorders.add(new ScrollingRecorder(this, viewer));
        allRecorders.add(new TabSwitchRecorder(this, viewer));
        
        return allRecorders;
    }
    
    @Override
    public Setting getSetting(Attribute mainAttribute) {
        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        SettingsList setting = new SettingsList(attribute, getClass().getSimpleName(), true);
        setting.setCaption(UIElementNames.getLocalized("RECORDER_SETTING_CAPTION"));
        
        setting.addSetting(ChangeRecorder.getSetting(attribute));
        setting.addSetting(FileRecorder.getSetting(attribute));
        setting.addSetting(ScrollingRecorder.getSetting(attribute));
        setting.addSetting(TabSwitchRecorder.getSetting(attribute));
        
        return setting;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        Attribute attr = viewer.getAttribute();
        boolean enabled = attr.containsSubAttribute(KEY) && Boolean.parseBoolean(attr.getSubAttribute(KEY).getValue());
        
        if (enabled) {
            Record record = new Record();
            
            record.add(new CVEntry(true));
            recorders.put(viewer, Pair.of(record, getAllRecorders(viewer)));
        }
    }

    @Override
    public void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel) {
        
        if (recorders.containsKey(codeViewer)) {
            recorders.get(codeViewer).getSecond().forEach(r -> r.onEditorPanelCreate(editorPanel));
        }
    }

    @Override
    public void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel) {
        
        if (recorders.containsKey(codeViewer)) {
            recorders.get(codeViewer).getSecond().forEach(r -> r.onEditorPanelClose(editorPanel));
        }
    }

    @Override
    public void onClose(CodeViewer codeViewer) {
        
        if (!recorders.containsKey(codeViewer)) {
            return;
        }

        Pair<Record, List<Recorder>> cvRecord = recorders.remove(codeViewer);
        File saveDir = new File(codeViewer.getSaveDir(), getClass().getSimpleName());
        File saveFile = new File(saveDir, RECORD_FILENAME);
        
        cvRecord.getSecond().forEach(Recorder::onClose);
        
        try {
            cvRecord.getFirst().add(new CVEntry(false));
            cvRecord.getFirst().save(saveFile);
        } catch (IOException e) {
            System.err.println("Could not save the " + RECORD_FILENAME);
            System.err.println(e.getMessage());
        }
    }
    
    public void record(CodeViewer viewer, RecordEntry entry) {
        
        if (recorders.containsKey(viewer)) {
            recorders.get(viewer).getFirst().add(entry);
        }
    }
}
