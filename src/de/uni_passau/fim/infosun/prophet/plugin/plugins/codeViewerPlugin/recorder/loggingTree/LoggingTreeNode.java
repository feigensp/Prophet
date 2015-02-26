package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggingTreeNode {

    public static final String TYPE_LOGFILE = "logfile";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_NOFILE = "nofile";

    public static final String ATTRIBUTE_TIME = "time";

	private List<LoggingTreeNode> children;
	private Map<String, String> attributes;
	private String type;

    public LoggingTreeNode(String type) {
        this.children = new ArrayList<>();
		this.attributes = new HashMap<>();
		this.type = type;

        attributes.put(ATTRIBUTE_TIME, String.valueOf(System.currentTimeMillis()));
    }

    public Map<String, String> getAttributes() {
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

	public void add(LoggingTreeNode child) {
		children.add(child);
	}

	public int getChildCount() {
		return children.size();
	}

	public LoggingTreeNode getChildAt(int index) {
		return children.get(index);
	}

	public LoggingTreeNode getLastChild() {
		return children.isEmpty() ? null : children.get(children.size() - 1);
	}
}
