package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class ScrollAction implements Action{
	
	private String actionCommand;
	private int startTime;
	private int startLine;
	private int endLine;
	
	public ScrollAction(String action, int start, int startLine, int endLine) {
		this.actionCommand = action;
		this.startTime = start;
		this.startLine = startLine;
		this.endLine = endLine;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public int getStartTime() {
		return startTime;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public int getEndLine() {
		return endLine;
	}

	public int compareTo(Integer start) {
		return startTime-start;
	}
}