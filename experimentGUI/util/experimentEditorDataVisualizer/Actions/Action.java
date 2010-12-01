package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public interface Action extends Comparable<Integer>{
	
	public String getActionCommand();
	
	public long getStartTime();
	
	public String getPath();
}
