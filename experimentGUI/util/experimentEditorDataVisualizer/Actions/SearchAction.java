package experimentGUI.util.experimentEditorDataVisualizer.Actions;

import experimentGUI.experimentEditor.ExperimentEditor;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;

public class SearchAction implements Action{
	
	public static final String ACTION_COMMAND = "SearchAction";
	private long startTime;
	private String path;
	private String text;
	private int currentPosition;
	boolean regex;
	boolean caseSensitive;
	//boolean forward;
	
	public SearchAction(long start, String path, String text, int currentPos, boolean regex, boolean caseSensitive /*, boolean forward*/) {
		this.startTime = start;
		this.text = text;
		this.currentPosition = currentPos;
		this.regex = regex;
		this.caseSensitive = caseSensitive;
		//this.forward = forward;
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
	
	public String getText() {
		return text;
	}
	
	public int getCurrentPos() {
		return currentPosition;
	}
	
	public boolean isRegex() {
		return regex;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
//	public boolean isForward() {
//		return forward;
//	}

	public int compareTo(Integer start) {
		return (int) startTime-start;
	}
}