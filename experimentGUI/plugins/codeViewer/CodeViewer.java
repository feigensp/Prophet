package experimentGUI.plugins.codeViewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import experimentGUI.plugins.codeViewer.fileTree.FileEvent;
import experimentGUI.plugins.codeViewer.fileTree.FileListener;
import experimentGUI.plugins.codeViewer.fileTree.FileTree;
import experimentGUI.plugins.codeViewer.tabbedPane.EditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;



@SuppressWarnings("serial")
public class CodeViewer extends JFrame implements FileListener {
	private EditorTabbedPane tabbedPane;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeViewer frame = new CodeViewer(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CodeViewer(QuestionTreeNode selected) {
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		if (selected==null) {
			selected = new QuestionTreeNode();
		}
		
		//read settings, store in variables
		
		String path = selected.getAttributeValue("codeviewer_path");
		path = path==null ? "." : path;
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Menu dummy");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Menu item dummy");
		menu.add(menuItem);
		
		splitPane = new JSplitPane();
		splitPane.setBorder(null);
		
		FileTree myTree = new FileTree(new File(path));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		splitPane.setLeftComponent(myTree);
		
		tabbedPane = new EditorTabbedPane(selected);
		splitPane.setRightComponent(tabbedPane);
		
		setContentPane(splitPane);	
		
		for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onFrameCreate(selected, this);
		}
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		tabbedPane.openFile(arg0.getFile());
	}
}
