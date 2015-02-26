package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.recorderPlugins;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextFieldSetting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

public class ChangePlugin implements Plugin {

    public static final String KEY = "change";
    public static final String KEY_JOIN = "join";
    public static final String KEY_JOIN_TIME = "jointime";
    public static final String TYPE_INSERT = "insert";
    public static final String TYPE_REMOVE = "remove";

    public static final String ATTRIBUTE_OFFSET = "offset";
    public static final String ATTRIBUTE_LENGTH = "length";
    public static final String ATTRIBUTE_CONTENT = "content";

    private boolean enabled;
    private boolean join;
    private long joinTime;

    private LoggingTreeNode currentNode;
    private Document currentDocument;

    private DocumentListener myListener;

    @Override
    public Setting getSetting(Attribute mainAttribute) {
        Attribute rDescAttribute = mainAttribute.getSubAttribute(KEY);
        SettingsList resultDesc = new SettingsList(rDescAttribute, getClass().getSimpleName(), true);
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
            currentDocument = null;
            myListener = new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent arg0) {

                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    int offset = arg0.getOffset();
                    int length = arg0.getLength();
                    String content = "";
                    try {
                        content = arg0.getDocument().getText(offset, length);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    if (join && currentNode.getChildCount() > 0) {
                        //Knoten zusammenf�hren?
                        LoggingTreeNode lastNode = (LoggingTreeNode) currentNode.getLastChild();
                        boolean wasInsert = lastNode.getType().equals(TYPE_INSERT);
                        if (wasInsert) {
                            long timeDiff = System.currentTimeMillis() - Long
                                    .parseLong(lastNode.getAttribute(LoggingTreeNode.ATTRIBUTE_TIME));
                            int lastEndOffset = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_OFFSET)) + Integer
                                    .parseInt(lastNode.getAttribute(ATTRIBUTE_LENGTH));
                            if (timeDiff < joinTime && lastEndOffset == offset) {
                                int newLength = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_LENGTH)) + length;
                                String newContent = lastNode.getAttribute(ATTRIBUTE_CONTENT) + content;
                                lastNode.setAttribute(ATTRIBUTE_LENGTH, "" + newLength);
                                lastNode.setAttribute(ATTRIBUTE_CONTENT, newContent);
                                lastNode.setAttribute(LoggingTreeNode.ATTRIBUTE_TIME, "" + System.currentTimeMillis());
                                return;
                            }
                        }
                    }
                    LoggingTreeNode node = new LoggingTreeNode(TYPE_INSERT);
                    node.setAttribute(ATTRIBUTE_OFFSET, "" + offset);
                    node.setAttribute(ATTRIBUTE_LENGTH, "" + length);
                    node.setAttribute(ATTRIBUTE_CONTENT, "" + content);
                    currentNode.addChild(node);
                }

                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    int offset = arg0.getOffset();
                    int length = arg0.getLength();
                    if (join && currentNode.getChildCount() > 0) {
                        //Knoten zusammenf�hren?
                        LoggingTreeNode lastNode = (LoggingTreeNode) currentNode.getLastChild();
                        boolean wasRemove = lastNode.getType().equals(TYPE_REMOVE);
                        if (wasRemove) {
                            System.out.println("was remove");
                            long timeDiff = System.currentTimeMillis() - Long
                                    .parseLong(lastNode.getAttribute(LoggingTreeNode.ATTRIBUTE_TIME));
                            int lastOffset = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_OFFSET));
                            int lastLength = Integer.parseInt(lastNode.getAttribute(ATTRIBUTE_LENGTH));
                            boolean wasBackspace = offset + length == lastOffset;
                            boolean wasDelete = lastOffset == offset;
                            if (timeDiff < joinTime && (wasBackspace || wasDelete)) {
                                int newLength = lastLength + length;
                                lastNode.setAttribute(ATTRIBUTE_OFFSET, "" + offset);
                                lastNode.setAttribute(ATTRIBUTE_LENGTH, "" + newLength);
                                lastNode.setAttribute(LoggingTreeNode.ATTRIBUTE_TIME, "" + System.currentTimeMillis());
                                return;
                            }
                        }
                    }
                    LoggingTreeNode node = new LoggingTreeNode(TYPE_REMOVE);
                    node.setAttribute(ATTRIBUTE_OFFSET, "" + offset);
                    node.setAttribute(ATTRIBUTE_LENGTH, "" + length);
                    currentNode.addChild(node);
                }
            };
        }
    }

    @Override
    public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab) {
        if (enabled) {
            if (currentDocument != null) {
                currentDocument.removeDocumentListener(myListener);
                currentDocument = null;
            }
            currentNode = newNode;
            if (newTab != null) {
                currentDocument = newTab.getTextArea().getDocument();
                currentDocument.addDocumentListener(myListener);
            }
        }
    }
}
