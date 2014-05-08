package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode;

import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class LoggingTreeNode extends DefaultMutableTreeNode {

    public final static String TYPE_LOGFILE = "logfile";
    public final static String TYPE_FILE = "file";
    public final static String TYPE_NOFILE = "nofile";
    public final static String ATTRIBUTE_TIME = "time";

    private String type = "(default)";

    private TreeMap<String, String> attributes = new TreeMap<>();

    long answerTime = 0;

    public LoggingTreeNode() {
        this("");
    }

    public LoggingTreeNode(String t) {
        if (!(t.trim().equals(""))) {
            type = t;
        }
        attributes.put(ATTRIBUTE_TIME, "" + System.currentTimeMillis());
    }

    public boolean isLogFile() {
        return type.equals(TYPE_LOGFILE);
    }

    public boolean isFile() {
        return type.equals(TYPE_FILE);
    }

    public TreeMap<String, String> getAttributes() {
        return attributes;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getType() {
        return type;
    }

    public boolean setType(String t) {
        if (t.trim().equals("")) {
            return false;
        }
        type = t;
        return true;
    }
}
