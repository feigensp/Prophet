package experimentGUI.plugins.codeViewerPlugin.fileTree;

import java.awt.AWTEvent;
import java.io.File;

@SuppressWarnings("serial")
public class FileEvent extends AWTEvent{
	public static final int FILE_OPENED = RESERVED_ID_MAX + 1;
	File file;
	FileEvent(Object source, int id, File f) {
		super(source, id);
		file=f;
	}
	public File getFile() {
		return file;
	}
}
