package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import experimentGUI.util.experimentEditorDataVisualizer.Actions.Action;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.FileAction;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ColorStore {
	
	//Farbe im Baum: String --> path, Color die dazugehört
	private HashMap<String, Color> treeColor;
	//Farbe im Text: String --> path, Integer --> Zeile, Color --> zugehörige Farbe
	private HashMap<String, HashMap<Integer, Color>> fileColor;
	
	public ColorStore(QuestionTreeNode data) {
		treeColor = new HashMap<String, Color>();
		fileColor = new HashMap<String, HashMap<Integer, Color>>();
		
		long totalTime = 1;
		//get all actions in sorted order
		Action[] actions = VideoRecordController.extractActions(data);
		//get the start time of the first file event and the ende time of the last to compute the overall time
		long start = 0;
		int i=0;
		while(!actions[i].getActionCommand().equals(FileAction.ACTION_COMMAND)) {
			i++;
		}
		start = actions[i].getStartTime();
		long end = 0;
		i = actions.length-1;
		while(!actions[i].getActionCommand().equals(FileAction.ACTION_COMMAND)) {
			i--;
		}
		end = ((FileAction)actions[i]).getEndTime();
		totalTime = end - start;
		//fill treeColor
		//TODO: Problem filePath vs. TreePath
		
		//fill fileColor
	}
	
	public Color getTreePathColor(String tp) {
		return treeColor.get(tp);
	}

}
