package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class RemoveAction implements Action{
	
	private String actionCommand;
	private int startTime;
	private int offset;
	private int length;
	
	public RemoveAction(String action, int start, int offset, int length) {
		this.actionCommand = action;
		this.startTime = start;
		this.offset = offset;
		this.length = length;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public int getStartTime() {
		return startTime;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}

	public int compareTo(Integer start) {
		return startTime-start;
	}
}