package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .ChangeRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .FileRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .ScrollingRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorders
        .TabSwitchRecorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;

public class RecorderPlugin implements Plugin {
    
    Map<CodeViewer, List<Recorder>> recorders;

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
        return null;
    }

    @Override
    public void onCreate(CodeViewer viewer) {
        
    }

    @Override
    public void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel) {
        
    }

    @Override
    public void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel) {
        
    }

    @Override
    public void onClose(CodeViewer codeViewer) {
        
    }
}
