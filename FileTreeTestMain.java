

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import FileTree.FileEvent;
import FileTree.FileListener;
import FileTree.FileTree;


@SuppressWarnings("serial")
public class FileTreeTestMain extends JFrame implements FileListener {

	private JPanel contentPane;
	private JTabbedPane tabbi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileTreeTestMain frame = new FileTreeTestMain();
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
	public FileTreeTestMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabbi = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbi, BorderLayout.CENTER);
		FileTree myTree = new FileTree(new File("."));
		myTree.setMinimumSize(new Dimension(200, 400));
		myTree.setPreferredSize(new Dimension(200, 400));
		myTree.addFileListener(this);
		contentPane.add(myTree, BorderLayout.WEST);
	}

	@Override
	public void fileOpened(FileEvent arg0) {
		System.out.println(arg0.getFile().getPath());
	}

}
