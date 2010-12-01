package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class FileAction implements Action{
	
	private String actionCommand;
	private int startTime;
	private int endTime;
	private String path;
	
	public FileAction(String action, int start, int end, String path) {
		this.actionCommand = action;
		this.startTime = start;
		this.endTime = end;
		this.path = path;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public String getPath() {
		return path;
	}

	public int compareTo(Integer start) {
		return startTime-start;
	}
}
