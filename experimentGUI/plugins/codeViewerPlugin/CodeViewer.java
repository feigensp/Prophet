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
	
	private JMenuBar menuBar;
	private JSplitPane splitPane;
	private FileTree myTree;
	private EditorTabbedPane tabbedPane;

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
		setBounds(100, 100, 800, 600);
		if (selected==null) {
			selected = new QuestionTreeNode();
		}
		
		//read settings, store in variables
		
		String path = selected.getAttributeValue(KEY_PATH);
		path = path==null || path.length()==0 ? "." : path;
		//System.out.println(path);
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		//TODO: in editable Plugin		
		JMenu menu = new JMenu("Datei");
		menuBar.add(menu);

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		menu.add(saveMenuItem);
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.saveActiveFile("UserMods");
			}
		});
		JMenuItem saveAllMenuItem = new JMenuItem("Alle Speichern");
		menu.add(saveAllMenuItem);
		saveAllMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.saveAllFiles("UserMods");
			}
		});
		//end TODO
		splitPane = new JSplitPane();
		splitPane.setBorder(null);
		
		myTree = new FileTree(new File(path));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		splitPane.setLeftComponent(myTree);
		
		tabbedPane = new EditorTabbedPane(selected);
		splitPane.setRightComponent(tabbedPane);
		
		setContentPane(splitPane);	
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		tabbedPane.openFile(arg0.getFile());
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
}
