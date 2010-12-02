package experimentGUI.util.experimentEditorDataVisualizer;

import java.util.Arrays;
import java.util.Vector;

import experimentGUI.experimentEditor.ExperimentEditor;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.Action;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.FileAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.InsertAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.RemoveAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.ScrollAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.SearchAction;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class VideoRecordController {

	Action[] actions;
	int currentAction;
	CodeViewer codeViewer;

	public VideoRecordController(QuestionTreeNode data, CodeViewer codeViewer) {
		this.actions = extractActions(data);
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

	public void jumpToTime(long millis) {
		long currentTime = actions[currentAction].getStartTime();
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
		actions[currentAction].execute(codeViewer);
	}

	public void lastAction() {
		currentAction--;
		actions[currentAction].execute(codeViewer);
	}	

	private Action[] extractActions(QuestionTreeNode data) {
		Vector<Action> actionVector = new Vector<Action>();
		for (int i = 0; i < data.getChildCount(); i++) {
			QuestionTreeNode fileNode = (QuestionTreeNode) data.getChildAt(i);
			if (fileNode.getType().equals("file")) {
				try {
					long start = Long.parseLong(fileNode.getAttributeValue("start"));
					long end = Long.parseLong(fileNode.getAttributeValue("end"));
					String path = fileNode.getAttributeValue("path");
					actionVector.add(new FileAction("file", start, end, path));
					for (int j = 0; j < fileNode.getChildCount(); j++) {
						QuestionTreeNode actionNode = (QuestionTreeNode) fileNode.getChildAt(j);
						String actionCommand = actionNode.getType();
						Action act = null;
						start = Long.parseLong(actionNode.getAttributeValue("time"));
						if (actionCommand.equals("insert")) {
							int offset = Integer.parseInt(actionNode.getAttributeValue("offset"));
							String value = actionNode.getAttributeValue("value");
							act = new InsertAction("insert", start, path, offset, value);
						} else if (actionCommand.equals("remove")) {
							int offset = Integer.parseInt(actionNode.getAttributeValue("offset"));
							int length = Integer.parseInt(actionNode.getAttributeValue("length"));
							act = new RemoveAction("remove", start, path, offset, length);
						} else if (actionCommand.equals("scroll")) {
							int startLine = Integer.parseInt(actionNode.getAttributeValue("startLine"));
							int endLine = Integer.parseInt(actionNode.getAttributeValue("endLine"));
							act = new ScrollAction("scroll", start, path, startLine, endLine);
						} else if (actionCommand.equals("search")) {
							String text = actionNode.getAttributeValue("text");
							int currentPos = Integer.parseInt(actionNode.getAttributeValue("currentPosition"));
							boolean regex = actionNode.getAttributeValue("regex").equals("true") ? true : false;
							boolean caseSensitive = actionNode.getAttributeValue("case").equals("true") ? true
									: false;
							act = new SearchAction("search", start, path, text, currentPos, regex, caseSensitive);
						}
						if (act != null) {
							actionVector.add(act);
						}
					}
				} catch(NumberFormatException e) {
					System.out.println("Fehler bei der Datenauswertung der log-Datei");
				}
			}
		}
		Action[] ret = (Action[]) actionVector.toArray();
		Arrays.sort(ret);
		return ret;
	}
}
