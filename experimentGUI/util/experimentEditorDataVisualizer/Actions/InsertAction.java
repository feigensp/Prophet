package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public class InsertAction implements Action{
	
	private String actionCommand;
	private int startTime;
	private int offset;
	private String value;
	
	public InsertAction(String action, int start, int offset, String value) {
		this.actionCommand = action;
		this.startTime = start;
		this.offset = offset;
		this.value = value;
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
	
	public String getValue() {
		return value;
	}

	public int compareTo(Integer start) {
		return startTime-start;
	}
}