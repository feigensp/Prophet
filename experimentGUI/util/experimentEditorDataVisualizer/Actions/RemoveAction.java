package experimentGUI.util.experimentEditorDataVisualizer.Actions;

import experimentGUI.experimentEditor.ExperimentEditor;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;

public class RemoveAction implements Action{
	
	public static final String ACTION_COMMAND = "RemoveAction";
	private long startTime;
	private String path;
	private int offset;
	private int length;
	
	public RemoveAction(long start, String path, int offset, int length) {
		this.startTime = start;
		this.offset = offset;
		this.length = length;
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
	
	public int getLength() {
		return length;
	}

	public int compareTo(Integer start) {
		return (int) startTime-start;
	}
}