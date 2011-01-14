package experimentGUI.plugins.codeViewerPlugin.tabbedPane;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaHighlighter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import test.Triple;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class EditorTabbedPane extends JTabbedPane {
	// /////////////////
//	public static final String NODE_PROJECT = "project";
//	public static final String ATTRIBUE_NAME = "name";
//	public static final String NODE_FILE = "file";
//	public static final String NODE_FOLDER = "folder";
//	public static final String NODE_FRAGMENT = "fragment";
//	public static final String ATTRIBUE_LENGTH = "length";
//	public static final String ATTRIBUE_OFFSET = "offset";
//	public static final String NODE_FEATURE = "feature";
//
//	final static int rectWidth = 5;
//
//	HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> coloringInfos;
//	JPanel drawPanel;
//	String whitespaces = "          ";
//	int addToOffset = 0;
	// ///////////////////////
	private QuestionTreeNode selected;
	private File showDir, saveDir;

	public EditorTabbedPane(QuestionTreeNode selected, File showDir, File saveDir) {
		super(JTabbedPane.TOP);
		this.selected = selected;
		this.showDir = showDir;
		this.saveDir = saveDir;
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// ///////////////////
//		try {
//			coloringInfos = loadXMLTree("ExpressionPL\\annotations.xml");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// /////////////
	}

	public void openFile(String path) {
		if (!path.startsWith(System.getProperty("file.separator"))) {
			path = System.getProperty("file.separator") + path;
		}
		EditorPanel e = getEditorPanel(path);
		if (e != null) {
			this.setSelectedComponent(e);
			e.grabFocus();
			return;
		}
		File file = new File(saveDir.getPath() + path);
		if (!file.exists()) {
			file = new File(showDir.getPath() + path);
		}
		if (file.exists()) {
			EditorPanel myPanel = new EditorPanel(file, path, selected);
			add(file.getName(), myPanel);
			setSelectedIndex(indexOfComponent(myPanel));
			this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(this, myPanel));
			myPanel.grabFocus();
			// //////////////////
//			System.out.println(path);
//			ArrayList<Triple<Integer, Integer, ArrayList<String>>> fileColoringInfos = coloringInfos
//					.get(path);
//			// RTextScrollPane scrollPane = myPanel.getScrollPane();
//
//			// Component lineNumbers =
//			// scrollPane.getRowHeader().getComponent(0);
//			// drawPanel = new JPanel();
//			// JPanel rowHeader = new JPanel();
//			// rowHeader.setLayout(new BorderLayout());
//			// rowHeader.add(lineNumbers, BorderLayout.WEST);
//			// rowHeader.add(drawPanel, BorderLayout.CENTER);
//
//			// scrollPane.setRowHeaderView(rowHeader);
//			/**
//			 * 
//			int lineOffset = textArea.getLineStartOffset(i + addToOffset);
//			textArea.getDocument().insertString(lineOffset, whitespaces, null);
//			 */
//			
//			int usedSize = 0;
//			for (int i = 0; i < fileColoringInfos.size(); i++) {
//				Triple<Integer, Integer, ArrayList<String>> infos = fileColoringInfos.get(i);
//				int offset = infos.getKey();
//				int length = infos.getValue1();
//				ArrayList<String> features = infos.getValue2();
//				if (features.size() > usedSize) {
//					usedSize = features.size();
//				}
//				try {
//					drawRect(myPanel.getTextArea(), offset, length, j * (rectWidth + 1), features);
//				} catch (BadLocationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}

			// int lineNumberWidth = lineNumbers.getWidth();
			// int colorWidth = (rectWidth+1)*usedSize;
			// rowHeader.setPreferredSize(new Dimension(50,
			// rowHeader.getHeight()));
			// //////////////////
		} else {
			JOptionPane.showMessageDialog(this, "Datei " + path
					+ " konnte nicht automatisch geöffnet werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}

	// //////////////////
//	private void drawRect(RSyntaxTextArea textArea, int offset, int length, int xPos,
//			ArrayList<String> features) throws BadLocationException {
//		int startLineNumber = textArea.getLineOfOffset(offset + addToOffset);
//		int endLineNumber = textArea.getLineOfOffset(offset + addToOffset + length);
//		addToOffset += whitespaces.length();
//		for (int i = startLineNumber; i <= endLineNumber; i++) {
//			for (int j = 0; j < features.size(); j++) {				
//				RSyntaxTextAreaHighlighter hilit = new RSyntaxTextAreaHighlighter();
//				DefaultHighlightPainter painterYellow = new DefaultHighlighter.DefaultHighlightPainter(
//						Color.YELLOW);
//				textArea.setHighlighter(hilit);
//				try {
//					hilit.addHighlight(1, 2, painterYellow);
//				} catch (BadLocationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}
//	}

	// ///////////////////

	public void closeFile(String path) {
		closeEditorPanel(getEditorPanel(path));
	}

	public void closeEditorPanel(EditorPanel editorPanel) {
		if (editorPanel.isChanged()) {
			int n = JOptionPane.showConfirmDialog(null, "Änderungen speichern?", "Speichern?",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				saveEditorPanel(editorPanel);
			}
		}
		for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onEditorPanelClose(editorPanel);
		}
		this.remove(editorPanel);
	}

	public EditorPanel getEditorPanel(String path) {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if ((myComp instanceof EditorPanel) && ((EditorPanel) myComp).getFilePath().equals(path)) {
				return (EditorPanel) myComp;
			}
		}
		return null;
	}

	public File getShowDir() {
		return showDir;
	}

	public File getSaveDir() {
		return saveDir;
	}

	public void saveActiveFile() {
		Component activeComp = getSelectedComponent();
		if (activeComp != null && activeComp instanceof EditorPanel) {
			saveEditorPanel((EditorPanel) activeComp);
		}
	}

	public void saveAllFiles() {
		for (int i = 0; i < getTabCount(); i++) {
			Component myComp = getComponentAt(i);
			if (myComp instanceof EditorPanel) {
				saveEditorPanel((EditorPanel) myComp);
			}
		}
	}

	protected void saveEditorPanel(EditorPanel editorPanel) {
		File file = new File(getSaveDir().getPath() + editorPanel.getFilePath());
		FileWriter fileWriter = null;
		try {
			file.getParentFile().mkdirs();
			fileWriter = new FileWriter(file);
			fileWriter.write(editorPanel.getTextArea().getText());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////

//	private HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> loadXMLTree(String path)
//			throws FileNotFoundException {
//		HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos = new HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>>();
//		File file = new File(path);
//		if (!file.exists()) {
//			throw new FileNotFoundException();
//		}
//		try {
//			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
//			// projekte
//			NodeList projectList = doc.getChildNodes();
//			for (int i = 0; i < projectList.getLength(); i++) {
//				// dateien und ordner
//				if (projectList.item(i).getChildNodes().getLength() > 0) {
//					filesAndDirs(projectList.item(i).getChildNodes(), "", infos);
//				}
//			}
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//		return infos;
//	}
//
//	private void filesAndDirs(NodeList list, String path,
//			HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
//		for (int i = 0; i < list.getLength(); i++) {
//			if (list.item(i).getNodeName().equals(NODE_FOLDER)) {
//				String folder = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
//				filesAndDirs(list.item(i).getChildNodes(), path + System.getProperty("file.separator")
//						+ folder, infos);
//			} else if (list.item(i).getNodeName().equals(NODE_FILE)) {
//				String file = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
//				fragments(list.item(i).getChildNodes(), path + System.getProperty("file.separator") + file,
//						infos);
//			}
//		}
//	}
//
//	private void fragments(NodeList list, String path,
//			HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
//		for (int i = 0; i < list.getLength(); i++) {
//			Node fragment = list.item(i);
//			if (!fragment.getNodeName().equals("#text")) {
//				int offset = -1;
//				int length = -1;
//				NamedNodeMap fragAttributes = fragment.getAttributes();
//				if (fragAttributes.getLength() > 0) {
//					try {
//						offset = Integer
//								.parseInt(fragAttributes.getNamedItem(ATTRIBUE_OFFSET).getNodeValue());
//						length = Integer
//								.parseInt(fragAttributes.getNamedItem(ATTRIBUE_LENGTH).getNodeValue());
//					} catch (NumberFormatException e0) {
//						System.err.print("corrupt xml file");
//					}
//					// Features
//					ArrayList<String> features = new ArrayList<String>();
//					NodeList featureList = fragment.getChildNodes();
//					for (int j = 0; j < featureList.getLength(); j++) {
//						if (!featureList.item(j).getNodeName().equals("#text")) {
//							features.add(featureList.item(j).getTextContent());
//						}
//					}
//					if (infos.get(path) == null) {
//						infos.put(path, new ArrayList<Triple<Integer, Integer, ArrayList<String>>>());
//					}
//					infos.get(path).add(
//							new Triple<Integer, Integer, ArrayList<String>>(offset, length, features));
//				}
//			}
//		}
//	}
	// /////////////////////////////
}
