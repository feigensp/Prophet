package plugins.codeViewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import plugins.codeViewer.fileTree.FileEvent;
import plugins.codeViewer.fileTree.FileListener;
import plugins.codeViewer.fileTree.FileTree;
import plugins.codeViewer.tabbedPane.EditorTabbedPane;


import util.StringTuple;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;


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
					CodeViewer frame = new CodeViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CodeViewer(ArrayList<StringTuple> settings) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		//read settings, store in variables
		StringTuple st = getKey(settings, "path");
		String path = st != null ? st.getValue() : ".";
		st = getKey(settings, "editable");
		boolean editable = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "searchable");
		boolean searchable = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "undoredo");
		boolean undoredo = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "highlighting");
		boolean highlighting = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "highlightswitching");
		boolean highlightswitching = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "linenumbers");
		boolean linenumbers = st != null ? (st.getValue().equals("true") ? true : false) : false;
		st = getKey(settings, "linenumbersswitching");
		boolean linenumbersswitching = st != null ? (st.getValue().equals("true") ? true : false) : false;
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("New menu");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("New menu item");
		menu.add(menuItem);
		
		splitPane = new JSplitPane();
		splitPane.setBorder(null);
		
		FileTree myTree = new FileTree(new File(path));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		splitPane.setLeftComponent(myTree);
		
		tabbedPane = new EditorTabbedPane(true, true);
		splitPane.setRightComponent(tabbedPane);
		
		setContentPane(splitPane);		
	}
	
	private StringTuple getKey(ArrayList<StringTuple> settings, String key) {
		for(StringTuple st : settings) {
			if(st.getKey().equals(key)) {
				return st;
			}
		}
		return null;
	}

	/**
	 * Create the frame.
	 */
	public CodeViewer() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
