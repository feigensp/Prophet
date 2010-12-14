package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.RecorderPlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recorderPlugins.ScrollingPlugin;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileEvent;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileListener;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.loggingTreeNode.LoggingTreeXMLHandler;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

public class ScrollHistory extends JFrame {

	private FileTree fileTree;
	private EditorTabbedPane editorTabbedPane;

	private String filePath;
	private LoggingTreeNode log;

	private HashMap<String, HashMap<Integer, Long>> colorScheme;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScrollHistory frame = new ScrollHistory(".", "recorder - Kopie.xml");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ScrollHistory(String filePath, String logPath) {
		this.filePath = filePath;
		this.log = LoggingTreeXMLHandler.loadXMLTree(logPath);

		colorScheme = new HashMap<String, HashMap<Integer, Long>>();

		initializeGUI();

		// openColorFiles();

		addListener();
	}

	/**
	 * Method who open files, which were looked at, and colour them
	 */
	private void openColorFiles() {
		// build up basic infos
		for (int i = 0; i < log.getChildCount(); i++) {
			LoggingTreeNode fileNode = (LoggingTreeNode) log.getChildAt(i);
			if (fileNode.getType().equals("file")) {
				String path = fileNode.getAttribute(RecorderPlugin.ATTRIBUTE_PATH);
				if (colorScheme.containsKey(path)) {
					HashMap<Integer, Long> fileColorScheme = updateScheme(colorScheme.get(path), fileNode);
					colorScheme.put(path, fileColorScheme);
				} else {
					colorScheme.put(path, updateScheme(new HashMap<Integer, Long>(), fileNode));
				}
			}
		}
		// iterate over the infos and color the lines in the files
		Iterator<HashMap<Integer, Long>> iterator = colorScheme.values().iterator();
		Iterator<String> pathIterator = colorScheme.keySet().iterator();
		while (iterator.hasNext()) {
			HashMap<Integer, Long> fileColorScheme = iterator.next();
			// get whole Time spend on this file
			Iterator<Long> timeIterator = fileColorScheme.values().iterator();
			long fileTime = 0;
			while (timeIterator.hasNext()) {
				fileTime += timeIterator.next();
			}
			// openFile
			String path = pathIterator.next();
			editorTabbedPane.openFile(new File(path));
			// color the lines
			RSyntaxTextArea textArea = editorTabbedPane.getEditorPanel(new File(path)).getTextArea();
			Iterator<Integer> lineIterator = fileColorScheme.keySet().iterator();
			timeIterator = fileColorScheme.values().iterator();
			Double factor = (double) (255 / fileTime);
			while (lineIterator.hasNext()) {
				Long lineTime = timeIterator.next();
				try {
					textArea.addLineHighlight(lineIterator.next(), new Color(0, (int) (lineTime * factor), 0));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private HashMap<Integer, Long> updateScheme(HashMap<Integer, Long> fileColorScheme,
			LoggingTreeNode fileNode) {
		HashMap<Integer, Long> ret = fileColorScheme;
		for (int i = 0; i < fileNode.getChildCount(); i++) {
			LoggingTreeNode actionNode = (LoggingTreeNode) fileNode.getChildAt(i);
			if (actionNode.getType().equals(ScrollingPlugin.TYPE_SCROLLING)) {
				int startLine = Integer
						.parseInt(actionNode.getAttribute(ScrollingPlugin.ATTRIBUTE_STARTLINE));
				int endLine = Integer.parseInt(actionNode.getAttribute(ScrollingPlugin.ATTRIBUTE_ENDLINE));
				long timeStart = Long.parseLong(actionNode.getAttribute(LoggingTreeNode.ATTRIBUTE_TIME));
				// ein knoten endet immer mit opened oder closed --> scrolling
				// kann nicht das letzte gewesen sein
				long timeEnd = Long.parseLong(((LoggingTreeNode) fileNode.getChildAt(i + 1))
						.getAttribute(LoggingTreeNode.ATTRIBUTE_TIME));
				long time = timeEnd - timeStart;
				for (int line = startLine; line <= endLine; line++) {
					if (ret.containsKey(line)) {
						Long newTime = ret.get(line) + time;
						ret.put(line, newTime);
					} else {
						ret.put(line, time);
					}
				}
			}
		}
		return ret;
	}

	public void addListener() {
		// fileTree
		fileTree.addFileListener(new FileListener() {
			public void fileEventOccured(FileEvent e) {
				editorTabbedPane.openFile(e.getFile());
			}
		});
	}

	public void initializeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel fileTreePanel = new JPanel();
		fileTreePanel.setLayout(new BorderLayout());
		contentPane.add(fileTreePanel, BorderLayout.WEST);

		fileTree = new FileTree(new File(filePath));
		fileTreePanel.add(fileTree, BorderLayout.CENTER);

		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		contentPane.add(editorPanel, BorderLayout.CENTER);

		editorTabbedPane = new EditorTabbedPane(QuestionTreeXMLHandler.loadXMLTree("test.xml"));
		editorPanel.add(editorTabbedPane, BorderLayout.CENTER);
	}

}
