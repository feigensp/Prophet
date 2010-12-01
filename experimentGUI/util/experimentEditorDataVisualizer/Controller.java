package experimentGUI.util.experimentEditorDataVisualizer;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileEvent;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.Action;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.FileAction;

public class Controller {

	Action[] actions;
	int currentAction;
	CodeViewer codeViewer;

	public Controller(Vector<Action> vectorActions, CodeViewer codeViewer) {
		this.actions = (Action[]) vectorActions.toArray();
		Arrays.sort(actions);
		currentAction = -1;
		
		this.codeViewer = codeViewer;
	}

	public void jumpToAction(int actionIndex) {
		while (currentAction < actionIndex && currentAction < actions.length - 1) {
			nextAction();
		}
		while (currentAction > actionIndex && currentAction > 0) {
			lastAction();
		}
	}

	public void jumpToTime(int millis) {
		int currentTime = actions[currentAction].getStartTime();
		while (currentTime > millis) {
			lastAction();
			currentTime = actions[currentAction].getStartTime();
		}
		while (currentTime < millis) {
			if (currentAction == actions.length - 1 || millis < actions[currentAction + 1].getStartTime()) {
				return;
			} else {
				nextAction();
				currentTime = actions[currentAction].getStartTime();
			}
		}
	}

	public void nextAction() {
		currentAction++;
		handleAction();
	}

	public void lastAction() {
		currentAction--;
		handleAction();
	}
	
	private void handleAction() {		
		String actionCommand = actions[currentAction].getActionCommand();
		if(actionCommand.equals("file")) {
			handleFileAction();			
		} else if(actionCommand.equals("insert")) {
			handleInsertAction();
		} else if(actionCommand.equals("remove")) {
			handleRemoveAction();
		} else if(actionCommand.equals("scroll")) {
			handleScrollAction();
		} else if(actionCommand.equals("search")) {
			handleSearchAction();
		}		
	}
	
	private void handleFileAction() {
		File fireFile = new File(((FileAction)actions[currentAction]).getPath());
		//codeViewer.fileEventOccured(new FileEvent(null, FileEvent.FILE_OPENED, fireFile));
	}
	
	private void handleInsertAction() {
		
	}
	
	private void handleRemoveAction() {
		
	}
	
	private void handleScrollAction() {
		
	}
	
	private void handleSearchAction() {
		
	}
}
