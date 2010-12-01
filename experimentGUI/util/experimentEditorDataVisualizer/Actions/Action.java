package experimentGUI.util.experimentEditorDataVisualizer.Actions;

public interface Action extends Comparable<Integer>{
	
	public String getActionCommand();
	
	public int getStartTime();
	
	public String getPath();
}
