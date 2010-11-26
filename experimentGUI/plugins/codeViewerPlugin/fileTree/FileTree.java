package experimentGUI.plugins.codeViewerPlugin.fileTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTree;
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
	private File file, fireFile;
	private Vector<FileListener> fileListeners;

	public FileTree(File f) {
		file = f;
		tree = new JTree(new FileTreeModel(file));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if ((e.getClickCount() == 2)
						&& (selPath != null)
						&& ((FileTreeNode) selPath.getLastPathComponent())
								.getFile().isFile()) {
					fireFile = ((FileTreeNode) selPath.getLastPathComponent())
							.getFile();
					fireFileEvent();
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

	private void fireFileEvent() {
		if (fileListeners == null)
			return;
		FileEvent event = new FileEvent(this, FileEvent.FILE_OPENED, fireFile);
		for (Enumeration<FileListener> e = fileListeners.elements(); e
				.hasMoreElements();)
			((FileListener) e.nextElement()).fileEventOccured(event);			
	}
}