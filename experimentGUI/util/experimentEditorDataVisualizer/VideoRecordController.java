package experimentGUI.util.experimentEditorDataVisualizer;

import java.io.File;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class VideoRecordController {

	QuestionTreeNode data;
	int currentFile;
	int currentAction;
	long maxAction;
	long startTime;
	long currentTime;
	long maxTime;
	FileTree fileTree;
	EditorTabbedPane tabbedPane;

	public VideoRecordController(QuestionTreeNode data, CodeViewer codeViewer) {
		this.currentTime = -1;
		this.maxTime = 0;
		this.startTime = 0;
		this.currentFile = -1;
		this.maxAction = 0;
		this.currentAction = -1;
		this.data = data;
		this.fileTree = codeViewer.getFileTree();
		this.tabbedPane = codeViewer.getTabbedPane();
	}

	private void analyseData() {
		QuestionTreeNode testNode = null;
		for (int i = 0; i < data.getChildCount(); i++) {
			testNode = (QuestionTreeNode) data.getChildAt(0);
			maxAction += testNode.getChildCount();
		}
		if (testNode == null) {
			// TODO: Nutzermeldung und abbrechen
			System.out.println("Keine Daten vorhanden.");
		} else {
			try {
				startTime = Long.parseLong(data.getAttributeValue("start"));
				maxTime = Long.parseLong(data.getAttributeValue("end")) - startTime;
			} catch (NumberFormatException e) {
				// TODO: Nutzermeldung und abbrechen
				System.out.println("Fehler im Dateiformat.");
			}
		}
	}

	// public void jumpToAction(int actionIndex) {
	// while (currentAction < actionIndex && currentAction < actions.length - 1)
	// {
	// nextAction();
	// }
	// while (currentAction > actionIndex && currentAction > 0) {
	// lastAction();
	// }
	// }
	//
	// public void jumpToTime(long millis) {
	// long currentTime = actions[currentAction].getStartTime();
	// while (currentTime > millis) {
	// lastAction();
	// currentTime = actions[currentAction].getStartTime();
	// }
	// while (currentTime < millis) {
	// if (currentAction == actions.length - 1 || millis < actions[currentAction
	// + 1].getStartTime()) {
	// return;
	// } else {
	// nextAction();
	// currentTime = actions[currentAction].getStartTime();
	// }
	// }
	// }

	public void nextAction() {
		currentAction++;
		if (data.getChildAt(currentFile) != null
				&& data.getChildAt(currentFile).getChildAt(currentAction) != null) {
			// Wenn nächste Aktion in der selben file --> ausführen
			executeAction((QuestionTreeNode) data.getChildAt(currentFile).getChildAt(currentAction));
		} else {
			// Wenn nicht die nächsten files durchgehen
			currentAction = 0;
			while (data.getChildAt(currentFile + 1) != null) {
				// wenn keine weitere vorhanden --> am ende
				if (currentFile >= data.getChildCount()) {
					// TODO: Ende
					System.out.println("Letzte Action erreicht.");
					return;
				} else {
					// neues öffnen
					executeAction((QuestionTreeNode) data.getChildAt(++currentFile));
					// wenn es kinder hat hier verweilen
					if (data.getChildAt(currentFile).getChildCount() > 0) {
						break;
					}
				}
			}
			// nächste aktion ausführen
			executeAction((QuestionTreeNode) data.getChildAt(currentFile).getChildAt(currentAction));
		}
	}

	// public void lastAction() {
	// currentAction--;
	// actions[currentAction].execute(codeViewer);
	// }

	private void executeAction(QuestionTreeNode action) {
		String actionCommand = action.getType();
		File file = new File(((QuestionTreeNode) data.getChildAt(currentFile)).getAttributeValue("path"));
		if (actionCommand.equals("file")) {
			// schauen ob letztes Tab zu schließen ist
			String closed = ((QuestionTreeNode) data.getChildAt(currentFile - 1)).getAttributeValue("closed");
			if (closed != null && closed.equals("true")) {
				tabbedPane.removeFile(new File(((QuestionTreeNode) data.getChildAt(currentFile - 1))
						.getAttributeValue("path")));
			}
			// neue Datei
			fileTree.fireFileEvent(file);
		} else if (actionCommand.equals("insert")) {
			// Text eingefügt
			RSyntaxTextArea textArea = tabbedPane.getEditorPanel(file).getTextArea();
			String oldText = textArea.getText();
			try {
				int offset = Integer.parseInt(action.getAttributeValue("offset"));
				String insertion = action.getAttributeValue("value");
				textArea.setText(oldText.substring(0, offset) + insertion + oldText.substring(offset + 1));
			} catch (NumberFormatException e) {
				System.out.println("Fehler im Dateiformat");
			}
		} else if (actionCommand.equals("remove")) {
			// Text entfernt
			RSyntaxTextArea textArea = tabbedPane.getEditorPanel(file).getTextArea();
			String oldText = textArea.getText();
			try {
				int offset = Integer.parseInt(action.getAttributeValue("offset"));
				int length = Integer.parseInt(action.getAttributeValue("length"));
				textArea.setText(oldText.substring(0, offset) + oldText.substring(offset + length + 1));
			} catch (NumberFormatException e) {
				System.out.println("Fehler im Dateiformat");
			}
		} else if (actionCommand.equals("scroll")) {
			// TODO: scroll-Action einfügen
		} else if (actionCommand.equals("search")) {
			// TODO: search-Action einfügen
		}
	}

	private void undoAction(QuestionTreeNode action) {
		String actionCommand = action.getType();
		File file = new File(((QuestionTreeNode) data.getChildAt(currentFile)).getAttributeValue("path"));
		if(actionCommand.equals("file")) {
			String path = ((QuestionTreeNode) data.getChildAt(currentFile)).getAttributeValue("path");
			//schauen ob davor schonmal diese file geöffnet wurde, aber nicht geschlossen
			for(int i=currentFile-1; i>0; i--) {
				if(((QuestionTreeNode) data.getChildAt(i)).getAttributeValue("path").equals(path)) {
					String closed = ((QuestionTreeNode) data.getChildAt(i)).getAttributeValue("closed");
					if(closed != null && closed.equals("true")) {
						//tab schliessen
						tabbedPane.removeFile(file);
					} else {
						//tab muss nicht geschlossen werden
						return;
					}
				}
			}
		} else if(actionCommand.equals("insert")) {
			// Text eingefügt
			RSyntaxTextArea textArea = tabbedPane.getEditorPanel(file).getTextArea();
			String oldText = textArea.getText();
			try {
				int offset = Integer.parseInt(action.getAttributeValue("offset"));
				String insertion = action.getAttributeValue("value");
				textArea.setText(oldText.substring(0, offset) + oldText.substring(offset + insertion.length() + 1));
			} catch (NumberFormatException e) {
				System.out.println("Fehler im Dateiformat");
			}			
		} else if (actionCommand.equals("remove")) {
			// Text entfernt
			RSyntaxTextArea textArea = tabbedPane.getEditorPanel(file).getTextArea();
			//nicht einfach herauszufinden welcher Text wiederhergestellt werden muss
			//schauen wo file geöffnet wurde und dann alle insert/remove commands bis zu diesem zeitpunkt abarbeiten?
			// TODO: mach das!
		} else if (actionCommand.equals("scroll")) {
			// TODO: scroll-Action einfügen
		} else if (actionCommand.equals("search")) {
			// TODO: search-Action einfügen
		}
	}
}
