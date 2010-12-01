package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class SearchAction implements Action{
	
	private String actionCommand;
	private int startTime;
	private String path;
	private String text;
	private int currentPosition;
	boolean regex;
	boolean caseSensitive;
	//boolean forward;
	
	public SearchAction(String action, int start, String path, String text, int currentPos, boolean regex, boolean caseSensitive /*, boolean forward*/) {
		this.actionCommand = action;
		this.startTime = start;
		this.text = text;
		this.currentPosition = currentPos;
		this.regex = regex;
		this.caseSensitive = caseSensitive;
		//this.forward = forward;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public int getStartTime() {
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
		return startTime-start;
	}
}