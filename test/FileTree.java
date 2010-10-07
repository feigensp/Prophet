package test;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * Modified by Markus Köppen and Andreas Hasselberg
 */

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Display a file system in a JTree view
 * 
 * @author Ian Darwin, Andreas Hasselberg, Markus Koeppen
 */
public class FileTree extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	
	private JTree tree;
	private File dir;
	ActionListener actionListener;
	
	/** Construct a FileTree */
	public FileTree(File d) {
		dir=d;
		setLayout(new BorderLayout());
		// Make a tree list with all the nodes, and make it a JTree
		TreeModel tm = new TreeModel() {
			File root = dir;
			Collection<TreeModelListener> listeners = new LinkedList<TreeModelListener>();
			public void addTreeModelListener(TreeModelListener l) {
				listeners.add( l );
			}
			public Object	getChild(Object parent, int index) {
				File f = (File)parent;
				File[] fileListing = f.listFiles() ;
				Arrays.sort( fileListing );
				return fileListing [ index ];
			}
			public int getChildCount(Object parent) {		
				File f = (File)parent;
				return f.listFiles().length;
			}
			public int getIndexOfChild(Object parent, Object child) {
				File f = (File)parent;
				File[] fileListing =  f.listFiles() ;
				Arrays.sort( fileListing );
				return Arrays.binarySearch( fileListing, child );
			}
			public Object	getRoot() {
				return root;
			}
 
			public boolean	isLeaf(Object node) {
				File f = (File)node;
				return !f.isDirectory();
			}
			public void	removeTreeModelListener(TreeModelListener l) {
				listeners.remove( l );
			}
			public void	valueForPathChanged(TreePath path, Object newValue) {
				return; 
			}
		};
		//tree = new JTree(addNodes(null, dir));
		tree = new JTree(tm);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// Add a listener
		//tree.addMouseListener(this);
		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(tree);
		add(BorderLayout.CENTER, scrollpane);
	}
	
	public void addMouseListener(MouseListener l) {
		tree.addMouseListener(l);
	}

	/** Add nodes from under "dir" into curTop. Highly recursive. */
	/*DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curDir, File dir) {
		String curPath = dir.getPath();
		if (curDir == null) { // should only be null at root
			curDir=new DefaultMutableTreeNode(curPath);
		}		
		
		Vector<String> entries = new Vector<String>();
		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++)
			entries.addElement(tmp[i]);
		Collections.sort(entries, String.CASE_INSENSITIVE_ORDER);
		
		File f;
		Vector<String> files = new Vector<String>();
		// Make two passes, one for Dirs and one for Files. This is #1.
		for (String entry: entries) {
			String newPath;
			newPath = curPath + File.separator + entry;
			if ((f = new File(newPath)).isDirectory()) {
				DefaultMutableTreeNode fDir = new DefaultMutableTreeNode(entry);
				curDir.add(fDir);
				addNodes(fDir, f);
			} else
				files.addElement(entry);
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		return curDir;
	} */

	public Dimension getMinimumSize() {
		return new Dimension(200, 400);
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}
	
	/*
	 * Vorbereitungen zum Casten eines ActionEvents
	 */
    public void addActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }
    public void removeActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }
    
    private void fireEvent(String actionCommand) {
        if (actionListener != null) {
            ActionEvent event = new ActionEvent( this, 
                                ActionEvent.ACTION_PERFORMED, actionCommand);
            actionListener.actionPerformed( event );
        }
    }
	
	/*
	 * Mouse Listener Methoden Hier wird getestet, ob auf eine Datei
	 * Doppelgeklickt wurde
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == tree) {
			if (((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())
					.isLeaf()) {
				if (me.getClickCount() == 2) {
					fireEvent(tree.getLastSelectedPathComponent().toString());
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}