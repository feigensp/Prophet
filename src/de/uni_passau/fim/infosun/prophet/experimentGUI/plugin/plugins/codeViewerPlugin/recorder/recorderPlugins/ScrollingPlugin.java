package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JViewport;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ScrollingPlugin implements RecorderPlugin {

    public final static String KEY = "scrolling";
    public final static String KEY_JOIN = "join";
    public final static String KEY_JOIN_TIME = "jointime";

    public final static String TYPE_SCROLLING = "scrolling";
    public final static String ATTRIBUTE_STARTLINE = "startline";
    public final static String ATTRIBUTE_ENDLINE = "endline";

    private boolean enabled;
    private boolean join;
    private long joinTime;

    private LoggingTreeNode currentNode;
    private JViewport currentViewPort;
    private RSyntaxTextArea currentTextArea;

    private ChangeListener myListener;

    @Override
    public Setting getSetting(Attribute mainAttribute) {
//        SettingsPluginComponentDescription resultDesc =
//                new SettingsPluginComponentDescription(KEY, UIElementNames.RECORDER_SCROLL_SCROLLING_BEHAVIOR, true);
//        SettingsPluginComponentDescription joinDesc =
//                new SettingsPluginComponentDescription(KEY_JOIN, UIElementNames.RECORDER_SCROLL_SUMMARIZE_SCROLLING,
//                        true);
//        SettingsComponentDescription joinTimeDesc =
//                new SettingsComponentDescription(SettingsTextField.class, KEY_JOIN_TIME,
//                        UIElementNames.RECORDER_TIME_INTERVAL_FOR_SUMMARY);
//        joinDesc.addSubComponent(joinTimeDesc);
//        resultDesc.addSubComponent(joinDesc);

        Attribute rDescAttribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings resultDesc = new PluginSettings(rDescAttribute, getClass().getSimpleName(), true);
        resultDesc.setCaption(UIElementNames.get("RECORDER_SCROLL_SCROLLING_BEHAVIOR"));

        Attribute joinDescAttribute = rDescAttribute.getSubAttribute(KEY_JOIN);
        PluginSettings joinDesc = new PluginSettings(joinDescAttribute, null, true);
        joinDesc.setCaption(UIElementNames.get("RECORDER_SCROLL_SUMMARIZE_SCROLLING"));

        Attribute joinTimeDescAttribute = joinDescAttribute.getSubAttribute(KEY_JOIN_TIME);
        Setting joinTimeDesc = new SettingsTextField(joinTimeDescAttribute, null);
        joinTimeDesc.setCaption(UIElementNames.get("RECORDER_TIME_INTERVAL_FOR_SUMMARY"));

        joinDesc.addSetting(joinTimeDesc);
        resultDesc.addSetting(joinDesc);

        return resultDesc;
    }

    @Override
    public void onFrameCreate(Attribute selected, CodeViewer viewer, LoggingTreeNode newNode) {
        enabled = Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue());
        if (enabled) {
            join = Boolean.parseBoolean(selected.getSubAttribute(KEY).getSubAttribute(KEY_JOIN).getValue());
            if (join) {
                try {
                    joinTime = Long.parseLong(
                            selected.getSubAttribute(KEY).getSubAttribute(KEY_JOIN).getSubAttribute(KEY_JOIN_TIME).getValue());
                } catch (Exception e) {
                    joinTime = 0;
                }
                if (joinTime == 0) {
                    join = false;
                }
            }
            currentNode = null;
            currentViewPort = null;
            currentTextArea = null;
            myListener = arg0 -> {
                Rectangle rect = currentViewPort.getViewRect();
                Point topLeft = rect.getLocation();
                int x = Math.min(currentTextArea.getWidth(), topLeft.x + rect.width);
                int y = Math.min(currentTextArea.getHeight(), topLeft.y + rect.height);
                Point bottomRight = new Point(x, y);
                int startLine;
                int endLine;
                int startOffset = currentTextArea.viewToModel(topLeft);
                int endOffset = currentTextArea.viewToModel(bottomRight);
                try {
                    startLine = currentTextArea.getLineOfOffset(startOffset) + 1;
                    endLine = currentTextArea.getLineOfOffset(endOffset) + 1;
                } catch (BadLocationException e) {
                    return;
                }
                if (currentNode.getChildCount() > 0) {
                    LoggingTreeNode lastNode = (LoggingTreeNode) currentNode.getLastChild();
                    boolean wasScrolling = lastNode.getType().equals(TYPE_SCROLLING);
                    if (wasScrolling) {
                        if (join) {
                            //Knoten zusammenf�hren?
                            long timeDiff = System.currentTimeMillis() - Long
                                    .parseLong(lastNode.getAttribute(LoggingTreeNode.ATTRIBUTE_TIME));
                            if (timeDiff < joinTime) {
                                lastNode.setAttribute(ATTRIBUTE_STARTLINE, "" + startLine);
                                lastNode.setAttribute(ATTRIBUTE_ENDLINE, "" + endLine);
                                lastNode.setAttribute(LoggingTreeNode.ATTRIBUTE_TIME,
                                        "" + System.currentTimeMillis());
                                return;
                            }
                        }
                        //Wenn Zeilen nicht ge�ndert wurden: nichts machen
                        int lastStartLine = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_STARTLINE));
                        int lastEndLine = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_ENDLINE));
                        if (startLine == lastStartLine && endLine == lastEndLine) {
                            return;
                        }
                    }
                }
                //nichts zu joinen, neue Zeilen: log!
                LoggingTreeNode node = new LoggingTreeNode(TYPE_SCROLLING);
                node.setAttribute(ATTRIBUTE_STARTLINE, "" + startLine);
                node.setAttribute(ATTRIBUTE_ENDLINE, "" + endLine);
                currentNode.add(node);
            };
        }
    }

    @Override
    public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab) {
        if (enabled) {
            if (currentViewPort != null) {
                currentViewPort.removeChangeListener(myListener);
                currentViewPort = null;
                currentTextArea = null;
            }
            currentNode = newNode;
            if (newTab != null) {
                currentViewPort = newTab.getScrollPane().getViewport();
                currentTextArea = newTab.getTextArea();
                currentViewPort.addChangeListener(myListener);
            }
        }
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
