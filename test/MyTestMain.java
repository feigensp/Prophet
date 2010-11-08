package test;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import EditorTabbedPane.EditorTabbedPane;
import FileTree.FileEvent;
import FileTree.FileListener;
import FileTree.FileTree;


@SuppressWarnings("serial")
public class MyTestMain extends JFrame implements FileListener {
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
					MyTestMain frame = new MyTestMain();
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
	public MyTestMain() {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("New menu");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("New menu item");
		menu.add(menuItem);
		
		splitPane = new JSplitPane();
		splitPane.setBorder(null);
		
		FileTree myTree = new FileTree(new File("."));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		splitPane.setLeftComponent(myTree);
		
		tabbedPane = new EditorTabbedPane(true, true);
		splitPane.setRightComponent(tabbedPane);
		
		setContentPane(splitPane);
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		tabbedPane.openFile(arg0.getFile());
	}
}
