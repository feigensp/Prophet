package experimentGUI.util.experimentEditorDataVisualizer;

import java.io.File;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.RecorderPlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins.ChangePlugin;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.Conversion;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class VideoRecordController {

	private LoggingTreeNode data;
	private int totalActionNumber;
	private int currentActionNumber;
	private FileTree fileTree;
	private EditorTabbedPane tabbedPane;
	private File currentFile;
	private int currentParentNode;
	private int currentChildNode;

	public VideoRecordController(LoggingTreeNode data, CodeViewer codeViewer) {
		this.currentChildNode = -1;
		this.currentParentNode = -1;
		this.data = data;
		this.fileTree = codeViewer.getFileTree();
		this.tabbedPane = codeViewer.getTabbedPane();
		this.currentFile = null;
		this.totalActionNumber = 0;
		this.currentActionNumber = 0;

		computeTotalActionNumber(data);
	}

	private void computeTotalActionNumber(LoggingTreeNode data) {
		for (int i = 0; i < data.getChildCount(); i++) {
			for (int j = 0; j < data.getChildAt(i).getChildCount(); j++) {
				String type = ((LoggingTreeNode) data.getChildAt(i).getChildAt(j)).getType();
				if (type.equals(RecorderPlugin.TYPE_OPENED)) {
					totalActionNumber++;
				} else if (type.equals(RecorderPlugin.TYPE_CLOSED)) {
					totalActionNumber++;
				} else if (type.equals(ChangePlugin.TYPE_INSERT)) {
					totalActionNumber++;
				} else if (type.equals(ChangePlugin.TYPE_REMOVE)) {
					totalActionNumber++;
				} else if (type.equals("scroll")) {
					// do nothing
				} else if (type.equals("suchen")) {
					// do nothing
				}
			}
		}
	}

	public void nextAction() {
		if (currentActionNumber < totalActionNumber) {
			if (currentParentNode < 0) {
				currentParentNode = 0;
				currentChildNode = -1;
				nextAction();
			} else {
				if (currentChildNode < 0) {
					currentChildNode = 0;
				}
				int totalParentActionNumber = data.getChildAt(currentParentNode).getChildCount();
				if (currentChildNode < totalParentActionNumber) {
					executeAction((LoggingTreeNode) data.getChildAt(currentParentNode).getChildAt(
							currentChildNode));
				} else {
					currentParentNode++;
					currentChildNode = 0;
					nextAction();
				}
			}
		} else {
			//ende erreicht
		}
	}

	public void lastAction() {
		if (currentActionNumber > 0) {
			if(currentChildNode <= 0) {
				if(currentParentNode > 0) {
					currentParentNode--;
					currentChildNode = data.getChildAt(currentParentNode).getChildCount();
					lastAction();
				} else {
					//Anfang erreicht
					return;
				}
			} else {
				currentChildNode--;
				executeAction((LoggingTreeNode)data.getChildAt(currentParentNode).getChildAt(currentChildNode));
			}
		}
	}

	private void executeAction(LoggingTreeNode action) {
		String actionCommand = action.getType();
		if (actionCommand.equals(RecorderPlugin.TYPE_OPENED)) {
			currentActionNumber++;
			String path = Conversion.toWindowsPath(action.getAttribute(RecorderPlugin.ATTRIBUTE_PATH));
			if (path != null) {
				currentFile = new File(path);
				System.out.println(currentFile.exists());
				fileTree.fireFileEvent(currentFile);
				// TODO: expand Tree
			}
		} else if (actionCommand.equals(RecorderPlugin.TYPE_CLOSED)) {
			currentActionNumber++;
			String path = action.getAttribute(RecorderPlugin.ATTRIBUTE_PATH);
			tabbedPane.closeFile(new File(path));
		} else if (actionCommand.equals(ChangePlugin.TYPE_INSERT)) {
			currentActionNumber++;
			if (currentFile != null) {
				RSyntaxTextArea textArea = tabbedPane.getEditorPanel(currentFile).getTextArea();
				String oldText = textArea.getText();
				try {
					int offset = Integer.parseInt(action.getAttribute(ChangePlugin.ATTRIBUTE_OFFSET));
					String insertion = action.getAttribute(ChangePlugin.ATTRIBUTE_CONTENT);
					textArea.setText(oldText.substring(0, offset) + insertion + oldText.substring(offset + 1));
				} catch (NumberFormatException e) {
					System.out.println("Fehler im Dateiformat");
				}
			}
		} else if (actionCommand.equals(ChangePlugin.TYPE_REMOVE)) {
			currentActionNumber++;
			RSyntaxTextArea textArea = tabbedPane.getEditorPanel(currentFile).getTextArea();
			String oldText = textArea.getText();
			try {
				int offset = Integer.parseInt(action.getAttribute(ChangePlugin.ATTRIBUTE_OFFSET));
				int length = Integer.parseInt(action.getAttribute(ChangePlugin.ATTRIBUTE_LENGTH));
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
		if (actionCommand.equals(RecorderPlugin.TYPE_OPENED)) {
			currentActionNumber--;
			// TODO: Tab schliessen
		} else if (actionCommand.equals(RecorderPlugin.TYPE_CLOSED)) {
			currentActionNumber--;
			// TODO: Tab öffnen
		} else if (actionCommand.equals("insert")) {
			currentActionNumber--;
			// TODO: Text entfernen
		} else if (actionCommand.equals("remove")) {
			currentActionNumber--;
			// TODO: Text wiederherstellen (undo/redo)
		} else if (actionCommand.equals("scroll")) {
			// TODO: scroll-Action einfügen
		} else if (actionCommand.equals("search")) {
			// TODO: search-Action einfügen
		}
	}
	
	public int getTotalActionNumber() {
		return totalActionNumber;
	}
}
