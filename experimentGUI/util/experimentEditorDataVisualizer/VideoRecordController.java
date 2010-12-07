package experimentGUI.util.experimentEditorDataVisualizer;

import java.io.File;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class VideoRecordController {

	private LoggingTreeNode data;
	private int currentFile;
	private int currentAction;
	private int totalActionNumber;
	private FileTree fileTree;
	private EditorTabbedPane tabbedPane;

	public VideoRecordController(LoggingTreeNode data, CodeViewer codeViewer) {
		this.currentFile = -1;
		this.currentAction = -1;
		this.totalActionNumber = getTotalActionNumber(data);
		this.data = data;
		this.fileTree = codeViewer.getFileTree();
		this.tabbedPane = codeViewer.getTabbedPane();
	}
	
	private int getTotalActionNumber(LoggingTreeNode data) {
		//TODO: actionanzahl zählen
		return 0;
	}

	public void nextAction() {
		if(currentAction < totalActionNumber-1) {
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
