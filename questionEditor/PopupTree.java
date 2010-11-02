package questionEditor;

/**
 * This Class inherits from JTree.
 * It interacts with EditorData and build there a DataTree (from DataTreeNode) like the data in JTree.
 * It permit limited Drag and Drop, such as Renaming and a 3 Level System.
 * The first Level is the root, the child of the root could have childs again, no more Levels are allowed.
 * All Childs of the second Level get a SettingDialog and EditorData will save the informations.
 * All nodes will have a unique name.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class PopupTree extends JTree implements ActionListener, MouseListener,
		DropTargetListener {

	private JPopupMenu treePopup; // the popup-menu of the tree

	private int x, y; // position when the mouse was released

	private String[] sourcePath; // path in the tree wenn the mouse was pressed

	private JTextPane textPane; // there we sill set the content of the lvl 3
	// nodes
	private JTextPane viewPane; // show the same content as above in html
	private String[] selectedNodePath;

	/**
	 * The Constructor of the class it creates the popup menu and some settings,
	 * initialising
	 * 
	 * @param root
	 *            root of the jtree
	 * @param textPane
	 *            pane for showing level 3 content
	 * @param viewPane
	 *            pane for showing level 3 content in html
	 */
	public PopupTree(DefaultMutableTreeNode root, final JTextPane textPane,
			final JTextPane viewPane) {
		super(root);
		EditorData.getDataRoot().setName(root.toString());

		this.viewPane = viewPane;
		this.textPane = textPane;
		// create popup menu
		JMenuItem mi;
		treePopup = new JPopupMenu();
		mi = new JMenuItem("Neu");
		mi.addActionListener(this);
		mi.setActionCommand("new");
		treePopup.add(mi);
		mi = new JMenuItem("Löschen");
		mi.addActionListener(this);
		mi.setActionCommand("remove");
		treePopup.add(mi);
		mi = new JMenuItem("Umbenennen");
		mi.addActionListener(this);
		mi.setActionCommand("rename");
		treePopup.add(mi);
		mi = new JMenuItem("Einstellungen");
		mi.addActionListener(this);
		mi.setActionCommand("settings");
		treePopup.add(mi);
		mi = new JMenuItem("HTML-Datei importieren");
		mi.addActionListener(this);
		mi.setActionCommand("import");
		treePopup.add(mi);

		textPane.setEditable(false);
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				// save the content to the right node
				EditorData.getNode(selectedNodePath).setContent(
						textPane.getText());
				viewPane.setText(EditorData.HTMLSTART + textPane.getText()
						+ EditorData.HTMLEND);
			}
		});

		setDragEnabled(true);
		setDropTarget(new DropTarget(this, this));

		this.addMouseListener(this);

		this.setEditable(false);

		x = y = 0;
	}

	/**
	 * if you will show a new DataTreeNode (as Root) you should call this
	 * method. It rebuild the JTree so that the new data is shown.
	 */
	public void rootUpdated() {
		// get Root of the JTree
		TreePath tp = this.getClosestPathForLocation(0, 0);
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		for (DataTreeNode categorie : EditorData.getDataRoot().getChildren()) {
			// insert level 1 nodes (categories)
			DefaultMutableTreeNode cat = new DefaultMutableTreeNode(categorie
					.getName());
			SettingsDialog dia = new SettingsDialog(categorie.getName());

			Boolean[] newSettings = new Boolean[Settings.settings.size()];
			for (ElementAttribute categorieAttribute : categorie
					.getAttributes()) {
				int pos = Settings.settings.indexOf(categorieAttribute
						.getName());
				if (pos != -1 && pos < newSettings.length) {
					newSettings[pos] = categorieAttribute.getContent().equals(
							"true") ? true : false;
				}
			}
			String path = (String) categorie.getAttribute("path").getContent();
			dia.setSettings(path, newSettings);

			EditorData.addSettingsDialog(dia);
			rootNode.add(cat);

			// insert level 3 nodes (childs)
			for (DataTreeNode question : categorie.getChildren()) {
				DefaultMutableTreeNode quest = new DefaultMutableTreeNode(
						question.getName());
				cat.add(quest);
				String con = "";
				// tries to get the content out of the corresponding files of
				// the node
				try {
					Scanner scanner = new Scanner(new File(question.getName()
							+ ".html"));
					while (scanner.hasNextLine()) {
						con += scanner.nextLine() + "\n";
					}
					con = con.substring(EditorData.HTMLSTART.length(), con
							.length()
							- EditorData.HTMLEND.length() - 1);
				} catch (FileNotFoundException e) {
					System.out.println("Quelldatei nicht gefunden");
				}
				question.setContent(con);
			}
		}
	}

	/**
	 * this method will rename a level 2 or level 3 node the node is selected by
	 * the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a proposal for the name of the node
	 */
	private void renameChild(String nameProposal) {
		String name = getFreeName(nameProposal); // get a free name if it is not
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// renaming in data
		switch (pathElements.length) {
		case 2: // level 2 (categorie)
			EditorData.getDataRoot().getChild(pathElements[1]).setName(name);
			EditorData.getSettingsDialogs(pathElements[1]).setId(name);
			break;
		case 3: // level 3 (child)
			EditorData.getDataRoot().getChild(pathElements[1]).getChild(
					pathElements[2]).setName(name);
			break;
		}
		// renaming in jtree
		currentNode.setUserObject(name);
		((DefaultTreeModel) this.getModel())
				.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
	}

	/**
	 * this method will rename a child the renamed node will be selected after
	 * the last mouse-button release
	 */
	private void removeChild() {
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// remove child from data
		switch (pathElements.length) {
		case 2: // level 2...
			EditorData.getDataRoot().removeChild(pathElements[1]);
			EditorData.removeSettingsDialog(pathElements[1]);
			break;
		case 3: // level 3...
			EditorData.getDataRoot().getChild(pathElements[1]).removeChild(
					pathElements[2]);
			break;
		}
		// remove child from JTree
		if (currentNode.getParent() != null) {
			int nodeIndex = currentNode.getParent().getIndex(currentNode);
			currentNode.removeAllChildren();
			((DefaultMutableTreeNode) currentNode.getParent())
					.remove(nodeIndex);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
		}
	}

	/**
	 * this method will add a Child to the tree the position of the new child
	 * will be selected after the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a name proposal for the new Child
	 */
	private void addChild(String nameProposal) {
		String name = getFreeName(nameProposal); // free name if it is not
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// adding the child (Data and JTree)
		switch (pathElements.length) {
		case 1: // new level 2 (categorie)
			EditorData.getDataRoot().addChild(new DataTreeNode(name),
					EditorData.getDataRoot().getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
			SettingsDialog dia = new SettingsDialog(name);
			EditorData.addSettingsDialog(dia);
			dia.setVisible(true);
			break;
		case 2: // new level 3 (question)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode);
			break;
		case 3: // as in case 2 (only different path, so slightly differend
			// code)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			((DefaultMutableTreeNode) currentNode.getParent()).add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode.getParent());
			break;
		}
	}

	/**
	 * This methode will get you a unique name from the tree it searches after
	 * the nameProposal, if it exists (or is blank) it will add an "_" this
	 * method eliminates all whitespace-characters too
	 * 
	 * @param name
	 *            a proposal for the name
	 * @return unique name
	 */
	public String getFreeName(String name) {
		String ret = name.replaceAll(" ", "");
		ret = ret.replaceAll("\t", "");
		if (ret.equals("")) {
			ret += "_";
		}
		while (EditorData.getDataRoot().nameExist(ret)) {
			ret += "_";
		}

		return ret;
	}

	/**
	 * this method imports an html file to the categorie most near to the mouse click
	 * if the file doesn't consist of a right body it is not importet
	 * if the file will be importet, it will be tests, if she ends with EditorData.HTMLEnd
	 * if yes the end ist cuted off
	 */
	public void importHTML() {
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		
		
		if(pathElements.length < 2 || pathElements.length > 3) {
			JOptionPane
			.showMessageDialog(null,
					"HTML-Dateien können nicht als Kategorie importiert werden.");			
		} else {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String htmlContent = "";
				try {
					Scanner sc = new Scanner(file);
					while(sc.hasNextLine()) {
						htmlContent += sc.nextLine();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				int begin = htmlContent.indexOf("<body>");
				int end = htmlContent.lastIndexOf("</body>");
				if(begin == -1 || end == -1) {
					JOptionPane.showMessageDialog(null, "Keine Datei mit gültigem Aufbau");
				} else {
					int start = file.toString().lastIndexOf("\\") == -1 ? 0 : file.toString().lastIndexOf("\\")+1;
					String name = getFreeName(file.toString().substring(start, file.toString().lastIndexOf(".")));
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);					
					//test if the footer is the same as our html-file standart
					if(htmlContent.endsWith(EditorData.HTMLEND)) {
						htmlContent = htmlContent.substring(begin+6, htmlContent.length()-EditorData.HTMLEND.length());
					} else {
						htmlContent = htmlContent.substring(begin+6, end);
					}
					DataTreeNode dtn = new DataTreeNode(name);
					dtn.setContent(htmlContent);
					
					switch (pathElements.length) {
					case 2: // new level 3 (question)
						EditorData.getDataRoot().getChild(pathElements[1]).addChild(
								dtn,
								EditorData.getDataRoot().getChild(pathElements[1])
										.getChildCount());
						currentNode.add(child);
						((DefaultTreeModel) this.getModel())
								.nodeStructureChanged((TreeNode) currentNode);
						break;
					case 3: // as in case 2 (only different path, so slightly differend
						// code)
						EditorData.getDataRoot().getChild(pathElements[1]).addChild(
								dtn,
								EditorData.getDataRoot().getChild(pathElements[1])
										.getChildCount());
						((DefaultMutableTreeNode) currentNode.getParent()).add(child);
						((DefaultTreeModel) this.getModel())
								.nodeStructureChanged((TreeNode) currentNode.getParent());
						break;
					}
				}
			}
		}
	}

	/**
	 * this method is called if you click on a menuItem of the popupmenu it will
	 * call the corresponding method oder show the settings dialog
	 * 
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		String name;
		// remove
		if (ae.getActionCommand().equals("remove")) {
			removeChild();
		} else
		// new
		if (ae.getActionCommand().equals("new")) {
			if ((name = JOptionPane.showInputDialog(this, "Name:")) != null) {
				addChild(name);
			}
		} else
		// rename
		if (ae.getActionCommand().equals("rename")) {
			if ((name = JOptionPane.showInputDialog(this, "Neuer Name:")) != null) {
				renameChild(name);
			}
		} else
		// settings
		if (ae.getActionCommand().equals("settings")) {
			TreePath tp = this.getClosestPathForLocation(x, y);
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
					.getLastPathComponent();
			SettingsDialog dia = EditorData.getSettingsDialogs(currentNode
					.toString());
			if (dia != null) {
				dia.setVisible(true);
			}
		} else
		//import
		if(ae.getActionCommand().equals("import")) {
			importHTML();
		}
	}

	/**
	 * this method is called if the mouse-button ist released (on the JTree) It
	 * will show either the popupmenu or show some Data on the textPane/viewPane
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			x = e.getX();
			y = e.getY();
			treePopup.show((JComponent) e.getSource(), e.getX(), e.getY());
		} else {
			TreePath tp = this.getSelectionPath();
			if (tp != null) {
				String path = tp.toString();
				path = path.substring(1, path.length() - 1);
				selectedNodePath = path.split(", ");
				if (selectedNodePath.length != 3) {
					textPane.setEditable(false);
					textPane.setText("");
					viewPane.setText("");
					// keine Frage ausgewählt
					if (selectedNodePath.length == 2) {
						textPane.setText(EditorData.getSettingsDialogs(
								selectedNodePath[1]).getSettingsString());
					}
				} else {
					textPane.setText(EditorData.getNode(selectedNodePath)
							.getContent().replaceAll("\r\n", "\n"));
					viewPane.setText(EditorData.HTMLSTART + textPane.getText()
							+ EditorData.HTMLEND);
					textPane.setEditable(true);
				}
			}
		}
	}

	/**
	 * this method will save the sourcePath when the mouse-button is pressed (as
	 * String array)
	 */
	public void mousePressed(MouseEvent me) {
		TreePath tp = getClosestPathForLocation(me.getX(), me.getY());
		String s = tp.toString();
		s = s.substring(1, s.length() - 1);
		sourcePath = s.split(", ");
	}

	/**
	 * this method will test if you can drop the node on the current mouse
	 * position
	 * 
	 * @param dtde
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		DefaultMutableTreeNode source = (DefaultMutableTreeNode) getSelectionPath()
				.getLastPathComponent();
		Point p = dtde.getLocation();
		TreePath path = getClosestPathForLocation(p.x, p.y);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		// only accept some actions
		// move from level to same level (not important which parent)
		if ((node.getParent() != null && node.getParent() == source.getParent())
				|| node == source.getParent()
				|| (source.getLevel() == 2 && node.getLevel() == 1)) {
			dtde.acceptDrag(dtde.getDropAction());
		} else {
			dtde.rejectDrag();
		}
	}

	/**
	 * in this method the dropping action is executed it is called automatically
	 * 
	 * @param dtde
	 */
	public void drop(DropTargetDropEvent dtde) {
		// it shouldnt work... but it IS getting the source node (or looks like)
		DefaultMutableTreeNode source = (DefaultMutableTreeNode) getSelectionPath()
				.getLastPathComponent();
		Point p = dtde.getLocation();
		TreePath path = getClosestPathForLocation(p.x, p.y);
		DefaultMutableTreeNode target = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) source
				.getParent();
		DefaultTreeModel dtm = (DefaultTreeModel) getModel();
		// remove from JTree
		dtm.removeNodeFromParent(source);
		int targetIndex = 0;
		if (target != parent) {
			targetIndex = parent.getIndex(target) + 1;
		}
		// moving from level 3 to 2 to a differend parent
		if (sourcePath.length == 3 && target.getLevel() == 1
				&& target != parent) {
			System.out.println("bin ich schon hier");
			// remove from data
			EditorData.getDataRoot().getChild(sourcePath[1]).removeChild(
					sourcePath[2]);
			// insert to new position in data
			EditorData.getDataRoot().getChild(target.toString()).addChild(
					new DataTreeNode(sourcePath[2]), 0);
			// inser to new position in JTree
			dtm.insertNodeInto(source, target, targetIndex);
		} else {
			// remove node from data
			if (sourcePath.length == 2) {
				EditorData.getDataRoot().removeChild(sourcePath[1]);
			} else {
				EditorData.getDataRoot().getChild(sourcePath[1]).removeChild(
						sourcePath[2]);
			}
			// new position in data
			if (sourcePath.length == 2) {
				EditorData.getDataRoot().addChild(
						new DataTreeNode(sourcePath[1]), targetIndex);
			} else {
				EditorData.getDataRoot().getChild(sourcePath[1]).addChild(
						new DataTreeNode(sourcePath[2]), targetIndex);
			}
			// insert to new position in JTree
			dtm.insertNodeInto(source, parent, targetIndex);
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void dropActionChanged(DropTargetDragEvent arg0) {
	}

	public void dragExit(DropTargetEvent dte) {
	}

	public void dragEnter(DropTargetDragEvent arg0) {
	}
}
