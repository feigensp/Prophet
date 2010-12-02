package experimentGUI.util.experimentEditorDataVisualizer.Actions;

import experimentGUI.experimentEditor.ExperimentEditor;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;

public class ScrollAction implements Action{
	
	public static final String ACTION_COMMAND = "ScrollAction";
	private long startTime;
	private String path;
	private int startLine;
	private int endLine;
	
	public ScrollAction(long start, String path, int startLine, int endLine) {
		this.startTime = start;
		this.startLine = startLine;
		this.endLine = endLine;
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
	
	public int getStartLine() {
		return startLine;
	}
	
	public int getEndLine() {
		return endLine;
	}

	public int compareTo(Integer start) {
		return (int) startTime-start;
	}
}