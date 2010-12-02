package experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsCheckBox;
import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.RecorderPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ScrollingPlugin implements RecorderPluginInterface {
	public final static String KEY = "scrolling";
	
	public final static String TYPE_SCROLLING = "scrolling";
	public final static String ATTRIBUTE_STARTLINE = "startline";
	public final static String ATTRIBUTE_ENDLINE = "endline";
	
	boolean enabled;
	int lastStartLine;
	int lastEndLine;

	LoggingTreeNode currentNode;
	JViewport currentViewPort;
	RSyntaxTextArea currentTextArea;
	
	ChangeListener myListener;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription() {
		return new SettingsComponentDescription(SettingsCheckBox.class, KEY, "Scrollverhalten");
	}

	@Override
	public void onFrameCreate(QuestionTreeNode selected, CodeViewer viewer,
			LoggingTreeNode newNode) {
		enabled = Boolean.parseBoolean(selected.getAttributeValue(KEY));
		if (enabled) {
			currentNode = null;
			currentViewPort=null;
			currentTextArea=null;
			lastStartLine=-1;
			lastEndLine=-1;
			myListener = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					Rectangle rect = currentViewPort.getViewRect();
					Point topLeft = rect.getLocation();
					int x = Math.min(currentTextArea.getWidth(), topLeft.x+rect.width);
					int y = Math.min(currentTextArea.getHeight(), topLeft.y+rect.height);
					Point bottomRight = new Point(x,y);
					int startLine = -1;
					int endLine = -1;
					int startOffset = currentTextArea.viewToModel(topLeft);
					int endOffset = currentTextArea.viewToModel(bottomRight);
					if (startOffset!=-1 && endOffset!=-1) {
						try {
							startLine = currentTextArea.getLineOfOffset(startOffset);
							endLine = currentTextArea.getLineOfOffset(endOffset);
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (startLine!=lastStartLine || endLine!=lastEndLine) {
						lastStartLine=startLine;
						lastEndLine=endLine;
						LoggingTreeNode node = new LoggingTreeNode(TYPE_SCROLLING);
						node.setAttribute(ATTRIBUTE_STARTLINE, ""+startLine);
						node.setAttribute(ATTRIBUTE_ENDLINE, ""+endLine);
						currentNode.add(node);
					}
				}				
			};
		}
	}

	@Override
	public void onNodeChange(LoggingTreeNode newNode, EditorPanel newTab) {
		if (enabled) {
			if (currentViewPort!=null) {
				currentViewPort.removeChangeListener(myListener);
				currentViewPort=null;
				currentTextArea=null;
			}
			currentNode=newNode;
			if (newTab!=null) {
				currentViewPort = newTab.getScrollPane().getViewport();
				currentTextArea = newTab.getTextArea();
				currentViewPort.addChangeListener(myListener);
			}
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
