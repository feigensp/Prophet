package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import experimentGUI.plugins.codeViewerPlugin.fileTree.FileEvent;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileListener;
import experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;

public class ScrollHistory extends JFrame {

	private JMenuItem loadMenuItem;
	private JMenuItem closeMenuItem;
	private FileTree fileTree;
	private EditorTabbedPane editorTabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScrollHistory frame = new ScrollHistory();
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
	public ScrollHistory() {
		initializeGUI();
		addListener();
	}
	
	public void addListener() {
		//fileTree
		fileTree.addFileListener(new FileListener() {
			public void fileEventOccured(FileEvent e) {
				editorTabbedPane.openFile(e.getFile());
			}
		});
		//menu-load
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		//menu-close
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	public void initializeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		
		loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);
		
		closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel fileTreePanel = new JPanel();
		fileTreePanel.setLayout(new BorderLayout());
		contentPane.add(fileTreePanel, BorderLayout.WEST);
		
		fileTree = new FileTree(new File("."));
		fileTreePanel.add(fileTree, BorderLayout.CENTER);
		
		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		contentPane.add(editorPanel, BorderLayout.CENTER);		
		
		editorTabbedPane = new EditorTabbedPane(null);
		editorPanel.add(editorTabbedPane, BorderLayout.CENTER);
	}

}
