package experimentGUI.util.experimentEditorDataVisualizer.Actions;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;

public interface Action extends Comparable<Integer>{
	
	public String getActionCommand();
	
	public long getStartTime();
	
	public String getPath();
	
	public void execute(CodeViewer codeViewer);
}
