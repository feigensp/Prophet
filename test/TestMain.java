package test;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import EditorTabbedPane.EditorTabbedPane;
import FileTree.FileEvent;
import FileTree.FileListener;
import FileTree.FileTree;


@SuppressWarnings("serial")
public class TestMain extends JFrame implements FileListener {

	private JPanel contentPane;
	private EditorTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestMain frame = new TestMain();
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
	public TestMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new EditorTabbedPane();
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		FileTree myTree = new FileTree(new File("."));
		myTree.setMinimumSize(new Dimension(200, 400));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		contentPane.add(myTree, BorderLayout.WEST);
	}

	@Override
	public void fileEventOccured(FileEvent arg0) {
		tabbedPane.openFile(arg0.getFile());
	}
}
