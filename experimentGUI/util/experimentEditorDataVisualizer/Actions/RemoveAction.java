package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class RemoveAction implements Action{
	
	private String actionCommand;
	private long startTime;
	private String path;
	private int offset;
	private int length;
	
	public RemoveAction(String action, long start, String path, int offset, int length) {
		this.actionCommand = action;
		this.startTime = start;
		this.offset = offset;
		this.length = length;
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