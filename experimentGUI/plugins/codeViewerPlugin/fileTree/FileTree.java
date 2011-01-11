package experimentGUI.plugins.codeViewerPlugin.fileTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
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
	
	private FileTreeNode root;

	public FileTree(File dir) {
		rootDir = dir;
//		FileTreeNode treeNode;
		try {
			root = new FileTreeNode(rootDir);
			tree = new JTree(new DefaultTreeModel(root));
		} catch (FileNotFoundException e1) {
			tree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode()));
			JOptionPane.showMessageDialog(this, "Der im Experiment angegebene Pfad ist nicht vorhanden.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
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
	
	public void selectFile(String path) {
		String myPath = path;
		ArrayList<String> pathElements = new ArrayList<String>();
		if(root!= null) {
			pathElements.add(root.toString());
			int pos = myPath.indexOf(System.getProperty("file.separator"));
			while(pos != -1) {
				pathElements.add(myPath.substring(0, pos));
				myPath = myPath.substring(pos+1);
				pos = myPath.indexOf(System.getProperty("file.separator"));
			}
			pathElements.add(myPath);
			ArrayList<Object> treePathList = new ArrayList<Object>();
			treePathList.add(root);
			FileTreeNode currentNode = root;
			for(int i=0; i<pathElements.size(); i++) {
				for(int j=0; j<currentNode.getChildCount(); j++) {
					if(currentNode.getChildAt(j).toString().equals(pathElements.get(i))) {
						currentNode = (FileTreeNode) currentNode.getChildAt(j);
						treePathList.add(currentNode);
						break;
					}
				}
			}
			tree.expandPath(new TreePath(treePathList.toArray()));
			tree.setSelectionPath(new TreePath(treePathList.toArray()));	
		}
	}
}