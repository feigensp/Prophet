package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class ScrollAction implements Action{
	
	private String actionCommand;
	private long startTime;
	private String path;
	private int startLine;
	private int endLine;
	
	public ScrollAction(String action, long start, String path, int startLine, int endLine) {
		this.actionCommand = action;
		this.startTime = start;
		this.startLine = startLine;
		this.endLine = endLine;
	}

	public String getActionCommand() {
		return actionCommand;
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