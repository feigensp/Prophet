package experimentGUI.util.experimentEditorDataVisualizer.Actions;

import experimentGUI.experimentEditor.ExperimentEditor;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;

public class InsertAction implements Action{
	
	public static final String ACTION_COMMAND = "InsertAction";
	private long startTime;
	private String path;
	private int offset;
	private String value;
	
	public InsertAction(long start, String path, int offset, String value) {
		this.startTime = start;
		this.offset = offset;
		this.value = value;
	}
	
	public void execute(CodeViewer codeViewer) {
		
	}

	public String getActionCommand() {
		return ACTION_COMMAND;
	}

	public long getStartTime() {
		return startTime;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public String getValue() {
		return value;
	}

	public int compareTo(Integer start) {
		return (int) startTime-start;
	}
}