package experimentGUI.plugins.codeViewerPlugin;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import experimentGUI.plugins.codeViewerPlugin.fileTree.FileEvent;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileListener;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class CodeViewer extends JFrame implements FileListener {
	public final static String KEY_PATH = "path";
	public final static String KEY_EDITABLE = "editable";
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JSplitPane splitPane;
	private FileTree myTree;
	private EditorTabbedPane tabbedPane;
	
	private File showDir;
	private File saveDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeViewer frame = new CodeViewer(null, null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CodeViewer(QuestionTreeNode selected, File saveDir) {
		setTitle("Quelltext");
		this.setSize(800, 600);
		
		if (selected==null) {
			selected = new QuestionTreeNode();
		}
		if (saveDir==null) {
			saveDir=new File(".");
		}
		this.saveDir=saveDir;
		
		//read settings, store in variables
		
		String showPath = selected.getAttributeValue(KEY_PATH).replace('/', System.getProperty("file.separator").charAt(0));
		showDir = new File(showPath==null || showPath.length()==0 ? "." : showPath);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);			
		fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		menuBar.setVisible(false);
		fileMenu.setVisible(false);
		
		splitPane = new JSplitPane();
		splitPane.setBorder(null);
		
		myTree = new FileTree(showDir);
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		splitPane.setLeftComponent(myTree);
		
		tabbedPane = new EditorTabbedPane(selected, showDir);
		splitPane.setRightComponent(tabbedPane);
		
		setContentPane(splitPane);	
		CodeViewerPluginList.init(selected);
		CodeViewerPluginList.onFrameCreate(this);
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		if (arg0.getID()==FileEvent.FILE_OPENED) {
			tabbedPane.openFile(arg0.getFilePath());
		}
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}

	public FileTree getFileTree() {
		return myTree;
	}

	public EditorTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public File getSaveDir() {
		return saveDir;
	}

	public JMenuBar getViewerMenuBar() {
		return menuBar;
	}

	public JMenu getFileMenu() {
		return fileMenu;
	}
}
