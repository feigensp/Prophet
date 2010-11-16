package plugins.codeViewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import plugins.codeViewer.fileTree.FileEvent;
import plugins.codeViewer.fileTree.FileListener;
import plugins.codeViewer.fileTree.FileTree;
import plugins.codeViewer.tabbedPane.EditorTabbedPane;
import util.QuestionTreeNode;


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
		
		for (CodeViewerPlugin plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onFrameCreate(selected, this);
		}
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		tabbedPane.openFile(arg0.getFile());
	}
}
