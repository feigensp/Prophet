package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.fileTree;

import java.awt.AWTEvent;

@SuppressWarnings("serial")
public class FileEvent extends AWTEvent{
	public static final int FILE_OPENED = RESERVED_ID_MAX + 1;
	public static final int FILE_CLOSED = FILE_OPENED + 1;
	private String filePath;
	public FileEvent(Object source, int id, String filePath) {
		super(source, id);
		this.filePath=filePath;
	}
	public String getFilePath() {
		return filePath;
	}
}
