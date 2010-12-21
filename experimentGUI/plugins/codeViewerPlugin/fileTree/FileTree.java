package experimentGUI.plugins.codeViewerPlugin.fileTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Display a file system in a JTree view
 * 
 * @author Andreas Hasselberg, Markus Koeppen
 */
@SuppressWarnings("serial")
public class FileTree extends JScrollPane {
	private JTree tree;
	private File rootDir;
	private Vector<FileListener> fileListeners;

	public FileTree(File dir) {
		rootDir = dir;
		tree = new JTree(new DefaultTreeModel(new FileTreeNode(rootDir)));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if ((e.getClickCount() == 2)
						&& (selPath != null)
						&& ((FileTreeNode) selPath.getLastPathComponent())
								.isFile()) {
					fireFileEvent(((FileTreeNode) selPath.getLastPathComponent())
							.getFilePath());
				}
			}
		});
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setViewportView(tree);
	}

	/*
	 * Vorbereitungen zum Casten eines ActionEvents
	 */
	public void addFileListener(FileListener l) {
		if (fileListeners == null)
			fileListeners = new Vector<FileListener>();
		fileListeners.addElement(l);
	}

	public void removeFileListener(FileListener l) {
		if (fileListeners != null)
			fileListeners.removeElement(l);
	}

	public void fireFileEvent(String filePath) {
		if (fileListeners == null)
			return;
		FileEvent event = new FileEvent(this, FileEvent.FILE_OPENED, filePath);
		for (Enumeration<FileListener> e = fileListeners.elements(); e
				.hasMoreElements();)
			((FileListener) e.nextElement()).fileEventOccured(event);			
	}
}